package com.zhangxu.longge;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class PlayVideo extends Activity implements View.OnClickListener {

    private Button mPlay;
    private Button mPause;
    private Button mStop;


    private String fileName;



    MediaPlayer mediaPlayer ; // 播放器的内部实现是通过MediaPlayer
    SurfaceView surfaceView ;// 装在视频的容器
    SurfaceHolder surfaceHolder;// 控制surfaceView的属性（尺寸、格式等）对象
    boolean isPause ; // 是否已经暂停了

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        surfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mPlay = (Button) findViewById(R.id.play);
        mPause = (Button) findViewById(R.id.pause);
        mStop = (Button) findViewById(R.id.stop);

        mPlay.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mStop.setOnClickListener(this);

        Intent intent = getIntent();
        fileName = intent.getStringExtra("video path");


        //获取与当前surfaceView相关联的那个的surefaceHolder
        surfaceHolder = surfaceView.getHolder();

        //注册当surfaceView创建、改变和销毁时应该执行的方法
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i("通知", "surfaceHolder被销毁了");
                if(mediaPlayer!=null)
                    mediaPlayer.release();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("通知", "surfaceHolder被create了");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                       int height) {
                Log.i("通知", "surfaceHolder被改变了");
            }
        });


        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                play();
                break;
            case R.id.pause:
                pause();
                break;
            case R.id.stop:
                stop();
                break;
            default:
                break;
        }

    }

    //播放
    private void play(){

        mediaPlayer = new MediaPlayer();
        // 设置多媒体流类型
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // 设置用于展示mediaPlayer的容器
        mediaPlayer.setDisplay(surfaceHolder);
        try {

            File file = new File(fileName);
            if(file.exists()){
                Log.e("file","文件存在");
            }
            String a = file.getPath();
            Log.e("aaa",a);
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPause = false;
        } catch (Exception e) {
            Log.i("通知", "播放过程中出现了错误哦");
        }
    }

    //暂停
    private void pause(){
        Log.i("通知", "点击了暂停按钮");
        if(isPause==false){
            mediaPlayer.pause();
            isPause=true;
        }else{
            mediaPlayer.start();
            isPause=false;
        }


    }

    //停止
    private void stop(){
        Log.i("通知", "点击了stop按钮");
        mediaPlayer.stop();
        mediaPlayer.release();

    }

}
