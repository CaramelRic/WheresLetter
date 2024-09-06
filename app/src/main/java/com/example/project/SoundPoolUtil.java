package com.example.project;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.example.project.R;

/**
 * Created by zhongwang on 2017/9/8.
 */

public class SoundPoolUtil {
    private static SoundPoolUtil soundPoolUtil;
    private SoundPool soundPool;

    //单例模式
    public static SoundPoolUtil getInstance(Context context) {
        if (soundPoolUtil == null)
            soundPoolUtil = new SoundPoolUtil(context);
        return soundPoolUtil;
    }

    private SoundPoolUtil(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 0);
        //加载音频文件
        soundPool.load(context, R.raw.button, 1);
        //soundPool.load(context, R.raw.music2, 1);
        //soundPool.load(context, R.raw.music3, 1);


    }

    public void play(int number) {
        Log.d("tag", "number " + number);
        //播放音频
        soundPool.play(number, 1, 1, 0, 0, 1);
    }
}