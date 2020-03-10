package dev.lucasnlm.antimine.common.level.data

import androidx.annotation.Keep

@Keep
data class LevelSetup(
    val width: Int,
    val height: Int,
    val mines: Int,
    val preset: DifficultyPreset = DifficultyPreset.Custom
)
