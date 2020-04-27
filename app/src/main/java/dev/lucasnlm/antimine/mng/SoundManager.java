package dev.lucasnlm.antimine.mng;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.os.Build.VERSION;

import java.util.HashMap;

import dev.lucasnlm.antimine.MyConstant;
import dev.lucasnlm.antimine.R;

public class SoundManager {
    private static SoundManager _instance;
    private static AudioManager mAudioManager;
    private static Context mContext;
    private static SoundPool mSoundPool;
    private static HashMap<Integer, Integer> mSoundPoolMap;

    private SoundManager() {
    }

    public static void addSound(int i, int i2) {
        mSoundPoolMap.put(Integer.valueOf(i), Integer.valueOf(mSoundPool.load(mContext, i2, 1)));
    }

    public static void cleanup() {
        mSoundPool.release();
        mSoundPool = null;
        mSoundPoolMap.clear();
        mAudioManager.unloadSoundEffects();
        _instance = null;
    }

    public static synchronized SoundManager getInstance() {
        SoundManager soundManager;
        synchronized (SoundManager.class) {
            if (_instance == null) {
                _instance = new SoundManager();
            }
            soundManager = _instance;
        }
        return soundManager;
    }

    public static void initSounds(Context context) {
        mContext = context;
        if (VERSION.SDK_INT >= 21) {
            mSoundPool = new Builder().setMaxStreams(4).build();
        } else {
            mSoundPool = new SoundPool(4, 3, 1);
        }
        mSoundPoolMap = new HashMap<>();
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
    }

    public static void loadSounds() {
        mSoundPoolMap.put(Integer.valueOf(1), Integer.valueOf(mSoundPool.load(mContext, R.raw.click, 1)));
        mSoundPoolMap.put(Integer.valueOf(2), Integer.valueOf(mSoundPool.load(mContext, R.raw.correct, 1)));
        mSoundPoolMap.put(Integer.valueOf(3), Integer.valueOf(mSoundPool.load(mContext, R.raw.incorrect, 1)));
        mSoundPoolMap.put(Integer.valueOf(4), Integer.valueOf(mSoundPool.load(mContext, R.raw.error, 1)));
        mSoundPoolMap.put(Integer.valueOf(5), Integer.valueOf(mSoundPool.load(mContext, R.raw.reward, 1)));
        mSoundPoolMap.put(Integer.valueOf(6), Integer.valueOf(mSoundPool.load(mContext, R.raw.button_pressed, 1)));
    }

    public static void muteSound() {
        mSoundPool.setVolume(1, 0.0f, 0.0f);
    }

    public static void playLoopedSound(int i) {
        float streamVolume = ((float) mAudioManager.getStreamVolume(3)) / ((float) mAudioManager.getStreamMaxVolume(3));
        mSoundPool.play(((Integer) mSoundPoolMap.get(Integer.valueOf(i))).intValue(), streamVolume, streamVolume, 1, -1, 1.0f);
    }

    public static void playSound(int i, float f) {
        try {
            if (MyConstant.SOUND_SETTING == MyConstant.SOUND_ON) {
                mSoundPool.play(((Integer) mSoundPoolMap.get(Integer.valueOf(i))).intValue(), 1.0f, 1.0f, 1, 0, f);
            }
        } catch (Exception e) {
            System.out.print("testing");
            e.printStackTrace();
        }
    }

    public static void stopSound(int i) {
        mSoundPool.stop(((Integer) mSoundPoolMap.get(Integer.valueOf(i))).intValue());
    }
}