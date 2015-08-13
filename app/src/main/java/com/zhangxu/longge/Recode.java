package com.zhangxu.longge;

import android.app.Activity;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;


public class Recode extends Activity {

    private MediaRecorder mRecorder;

    public long time_ = System.currentTimeMillis();

    public File file = new File("/sdcard/longge/" + "recode" + time_ + ".3gp");

    public String fileName = file.getPath();

    //调用系统录音录制声音
    public void recodeVoice() {

        mRecorder = new MediaRecorder();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(fileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {

            Log.e("通知","播放过程中出现了错误");

        }



    }

    //停止录制声音
    public void stopRecode() {

        Log.e("通知","点击了Stop");
        mRecorder.stop();
        mRecorder.release();
    }
}
