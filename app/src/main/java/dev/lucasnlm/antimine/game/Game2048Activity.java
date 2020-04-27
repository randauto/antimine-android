package dev.lucasnlm.antimine.game;

import android.annotation.SuppressLint;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.InputDeviceCompat;

import dev.lucasnlm.antimine.MyConstant;
import dev.lucasnlm.antimine.R;
import dev.lucasnlm.antimine.mng.SoundManager;

class Game_2048_Activity extends AppCompatActivity implements OnClickListener {
    private static boolean DEF_FULLSCREEN = true;
    private static final String IS_FULLSCREEN_PREF = "is_fullscreen_pref";
    private static final long mTouchThreshold = 2000;

    /* renamed from: a */
    LinearLayout f3526a;

    /* renamed from: b */
    LinearLayout f3527b;

    /* renamed from: c */
    ImageView f3528c;
    /* access modifiers changed from: private */
    public long mLastTouch;
    private WebView mWebView;
    private Toast pressBackToast;

    private void LoadUrl() {
        String str = "";
        String string = getSharedPreferences("CommonPrefs", 0).getString("Language", str);
        String str2 = "vi";
        String str3 = "in";
        String str4 = "ms";
        String str5 = "ru";
        String str6 = "fr";
        String str7 = "es";
        String str8 = "it";
        String str9 = "pt";
        String str10 = "de";
        String str11 = "en";
        if (MyConstant.NIGHTMODE_SETTING == MyConstant.NIGHTMODE_ON) {
            this.f3526a.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            this.f3527b.setBackgroundResource(R.drawable.night_back_bg);
            String str12 = "file:///android_asset/2048/index_night.html";
            if (string.equals(str)) {
                this.mWebView.loadUrl(str12);
            }
            if (string.equals(str11)) {
                this.mWebView.loadUrl(str12);
            }
            if (string.equals(str10)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_de_night.html");
            }
            if (string.equals(str9)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_pt_night.html");
            }
            if (string.equals(str8)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_it_night.html");
            }
            if (string.equals(str7)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_sp_night.html");
            }
            if (string.equals(str6)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_fr_night.html");
            }
            if (string.equals(str5)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_ru_night.html");
            }
            if (string.equals(str4)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_ms_night.html");
            }
            if (string.equals(str3)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_in_night.html");
            }
            if (string.equals(str2)) {
                this.mWebView.loadUrl("file:///android_asset/2048/index_vi_night.html");
                return;
            }
            return;
        }
        this.f3526a.setBackgroundColor(getResources().getColor(R.color.white));
        this.f3527b.setBackgroundResource(R.drawable.layout_bg_add);
        String str13 = "file:///android_asset/2048/index.html";
        if (string.equals(str)) {
            this.mWebView.loadUrl(str13);
        }
        if (string.equals(str11)) {
            this.mWebView.loadUrl(str13);
        }
        if (string.equals(str10)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_de.html");
        }
        if (string.equals(str9)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_pt.html");
        }
        if (string.equals(str8)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_it.html");
        }
        if (string.equals(str7)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_sp.html");
        }
        if (string.equals(str6)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_fr.html");
        }
        if (string.equals(str5)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_ru.html");
        }
        if (string.equals(str4)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_ms.html");
        }
        if (string.equals(str3)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_in.html");
        }
        if (string.equals(str2)) {
            this.mWebView.loadUrl("file:///android_asset/2048/index_vi.html");
        }
    }

    private void animateClicked(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pop_in_low));
    }

    /* access modifiers changed from: private */
    public void applyFullScreen(boolean z) {
        if (z) {
            getWindow().clearFlags(1024);
        } else {
            getWindow().setFlags(1024, 1024);
        }
    }

    /* access modifiers changed from: private */
    public boolean isFullScreen() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(IS_FULLSCREEN_PREF, DEF_FULLSCREEN);
    }

    /* access modifiers changed from: private */
    public void saveFullScreen(boolean z) {
        Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putBoolean(IS_FULLSCREEN_PREF, z);
        edit.apply();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            animateClicked(view);
            SoundManager.playSound(1, 1.0f);
            onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled", "NewApi", "ShowToast"})
    public void onCreate(Bundle bundle) {
        requestWindowFeature(1);
        super.onCreate(bundle);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_game_2048);
        getWindow().getDecorView().setSystemUiVisibility(InputDeviceCompat.SOURCE_TOUCHSCREEN);
        this.f3526a = (LinearLayout) findViewById(R.id.l1);
        this.f3527b = (LinearLayout) findViewById(R.id.bg_back);
        this.f3528c = (ImageView) findViewById(R.id.back);
        this.f3528c.setOnClickListener(this);
        this.mWebView = (WebView) findViewById(R.id.mainWebView);
        WebSettings settings = this.mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        this.mWebView.setInitialScale(1);
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setRenderPriority(RenderPriority.HIGH);
        StringBuilder sb = new StringBuilder();
        sb.append(getFilesDir().getParentFile().getPath());
        sb.append("/databases");
        settings.setDatabasePath(sb.toString());
        if (bundle != null) {
            this.mWebView.restoreState(bundle);
        } else {
            LoadUrl();
        }
        this.mWebView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                long currentTimeMillis = System.currentTimeMillis();
                if (motionEvent.getAction() == 1 && Math.abs(currentTimeMillis - Game_2048_Activity.this.mLastTouch) > Game_2048_Activity.mTouchThreshold) {
                    boolean z = !Game_2048_Activity.this.isFullScreen();
                    Game_2048_Activity.this.saveFullScreen(z);
                    Game_2048_Activity.this.applyFullScreen(z);
                } else if (motionEvent.getAction() == 0) {
                    Game_2048_Activity.this.mLastTouch = currentTimeMillis;
                    SoundManager.playSound(3, 1.0f);
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mWebView.saveState(bundle);
    }
}
