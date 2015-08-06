package com.zhangxu.longge;

import android.app.Activity;
import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by my on 15/8/6.
 */
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
        } catch (IOException e) {

        }
        mRecorder.start();


    }

    //停止录制声音
    public void stopRecode() {


        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }
}
