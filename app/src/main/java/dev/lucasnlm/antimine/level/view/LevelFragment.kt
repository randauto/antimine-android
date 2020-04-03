package dev.lucasnlm.antimine.level.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.lucasnlm.antimine.common.R
import dev.lucasnlm.antimine.common.level.view.UnlockedHorizontalScrollView
import dagger.android.support.DaggerFragment
import dev.lucasnlm.antimine.common.level.models.Difficulty
import dev.lucasnlm.antimine.common.level.models.Event
import dev.lucasnlm.antimine.common.level.view.AreaAdapter
import dev.lucasnlm.antimine.common.level.view.SpaceItemDecoration
import dev.lucasnlm.antimine.common.level.viewmodel.GameViewModel
import dev.lucasnlm.antimine.common.level.viewmodel.GameViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class LevelFragment : DaggerFragment() {
    @Inject
    lateinit var viewModelFactory: GameViewModelFactory

    private lateinit var viewModel: GameViewModel
    private lateinit var recyclerGrid: RecyclerView
    private lateinit var bidirectionalScroll: UnlockedHorizontalScrollView
    private lateinit var areaAdapter: AreaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_level, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let {
            viewModel = ViewModelProviders.of(it, viewModelFactory).get(GameViewModel::class.java)
            areaAdapter = AreaAdapter(it.applicationContext, viewModel)
        }
    }

    override fun onPause() {
        super.onPause()

        GlobalScope.launch {
            viewModel.saveGame()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerGrid = view.findViewById(R.id.recyclerGrid)
        recyclerGrid.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            addItemDecoration(SpaceItemDecoration(R.dimen.field_padding))
            adapter = areaAdapter
            alpha = 0.0f
        }

        bidirectionalScroll = view.findViewById(R.id.bidirectionalScroll)
        bidirectionalScroll.setTarget(recyclerGrid)

        GlobalScope.launch {
            val loadGameUid = checkLoadGameDeepLink()
            val newGameDeepLink = checkNewGameDeepLink()

            val levelSetup = when {
                loadGameUid != null -> viewModel.loadGame(loadGameUid)
                newGameDeepLink != null -> viewModel.startNewGame(newGameDeepLink)
                else -> viewModel.loadLastGame()
            }

            val width = levelSetup.width

            withContext(Dispatchers.Main) {
                recyclerGrid.layoutManager =
                    GridLayoutManager(activity, width, RecyclerView.VERTICAL, false)

                view.post {
                    recyclerGrid.scrollBy(0, recyclerGrid.height / 2)
                    bidirectionalScroll.scrollBy(recyclerGrid.width / 4, 0)
                    recyclerGrid.animate().apply {
                        alpha(1.0f)
                        duration = 1000
                    }.start()
                }
            }
        }

        viewModel.run {
            field.observe(viewLifecycleOwner, Observer {
                areaAdapter.bindField(it)
            })

            levelSetup.observe(viewLifecycleOwner, Observer {
                recyclerGrid.layoutManager =
                    GridLayoutManager(activity, it.width, RecyclerView.VERTICAL, false)
            })

            fieldRefresh.observe(viewLifecycleOwner, Observer {
                areaAdapter.notifyItemChanged(it)
            })

            eventObserver.observe(viewLifecycleOwner, Observer {
                when (it) {
                    Event.ResumeGameOver,
                    Event.GameOver,
                    Event.Victory,
                    Event.ResumeVictory -> areaAdapter.setClickEnabled(false)
                    Event.Running,
                    Event.ResumeGame,
                    Event.StartNewGame -> areaAdapter.setClickEnabled(true)
                    else -> {}
                }
            })
        }
    }

    private fun checkNewGameDeepLink(): Difficulty? = activity?.intent?.data?.let { uri ->
        if (uri.scheme == DEFAULT_SCHEME) {
            when (uri.schemeSpecificPart.removePrefix(DEEP_LINK_NEW_GAME_HOST)) {
                DEEP_LINK_BEGINNER -> Difficulty.Beginner
                DEEP_LINK_INTERMEDIATE -> Difficulty.Intermediate
                DEEP_LINK_EXPERT -> Difficulty.Expert
                DEEP_LINK_STANDARD -> Difficulty.Standard
                else -> null
            }
        } else {
            null
        }
    }

    private fun checkLoadGameDeepLink(): Int? = activity?.intent?.data?.let { uri ->
        if (uri.scheme == DEFAULT_SCHEME) {
            uri.schemeSpecificPart.removePrefix(DEEP_LINK_LOAD_GAME_HOST).toIntOrNull()
        } else {
            null
        }
    }

    companion object {
        const val DEFAULT_SCHEME = "antimine"

        const val DEEP_LINK_NEW_GAME_HOST = "//new-game/"
        const val DEEP_LINK_LOAD_GAME_HOST = "//load-game/"
        const val DEEP_LINK_BEGINNER = "beginner"
        const val DEEP_LINK_INTERMEDIATE = "intermediate"
        const val DEEP_LINK_EXPERT = "expert"
        const val DEEP_LINK_STANDARD = "standard"
    }
}
