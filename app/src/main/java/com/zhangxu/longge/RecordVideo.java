package com.zhangxu.longge;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RecordVideo extends Activity implements SurfaceHolder.Callback {

    private Button mButton_record;

    private SurfaceView mView;

    private MediaRecorder mRecord;

    private SurfaceHolder holder;

    private File mfile;

    private boolean isRecording = false;

    private Camera camera;

    private Timer timer;
    private TimerTask task;

    private long downTime;
    private long upTime;

    private SQLiteOpenHelper helper;
    private SQLiteDatabase db;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);

        mButton_record = (Button) findViewById(R.id.record);

        mView = (SurfaceView) findViewById(R.id.mView);

        //设置Surface不需要自己维护缓冲区
        mView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        holder = mView.getHolder();// 取得holder
        holder.addCallback(this); // holder加入回调接口

        //设置分辨率
        mView.getHolder().setFixedSize(1280,720);

        //设置该组件让屏幕不会自动关闭
        mView.getHolder().setKeepScreenOn(true);

        mButton_record.setOnTouchListener(new MyOnTouchListener());

        helper = new DataBaseHelper(this);
        db = helper.getWritableDatabase();

        Intent intent = getIntent();

        index = intent.getIntExtra("index",1);

        Log.e("index_record",index+"");


    }



    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        this.holder = holder;
        //开启相机
        if(camera == null)
        {
            camera = Camera.open();

            try {
                camera.setPreviewDisplay(holder);
                camera.setDisplayOrientation(90);
                //得到手机摄像头适配的分辨率
                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> list = parameters.getSupportedPictureSizes();
                for(Camera.Size size:list){
                    Log.e("1234",size.height + " " + size.width);
                }




            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        camera.startPreview();



    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {



    }

    private class MyOnTouchListener implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == KeyEvent.ACTION_DOWN){

                if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

                    return false;
                }

                downTime = System.currentTimeMillis();
                //创建MediaRecorder对象
                mRecord = new MediaRecorder();

                mRecord.setOrientationHint(90);


                //改变字体颜色和图片
                mButton_record.setBackgroundResource(R.drawable.record2);
                mButton_record.setTextColor(0xff22f402);

                //关闭预览并释放资源
                if(camera != null) {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                }




                timer = new Timer();
                task = new TimerTask() {
                    @Override
                    public void run() {

                        //创建保存录制视频的视频文件
                        long time_ = System.currentTimeMillis();
                        mfile = new File("/sdcard/longge/" + "video" + time_ + ".mp4");


                        mRecord.reset();


                        //设置从麦克风采集声音
                        mRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
                        //设置从摄像头采集图像
                        mRecord.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                        //设置视频文件的输出格式（必须在设置声音编码格式，图像编码格式之前设置）
                        mRecord.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                        //设置声音编码的格式
                        mRecord.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        //设置图像编码格式
                        mRecord.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                        mRecord.setOutputFile(mfile.getAbsolutePath());
                        //指定使用SurfaceView来预览视频
                        mRecord.setPreviewDisplay(mView.getHolder().getSurface());

                        try{

                            mRecord.prepare();


                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        mRecord.start();

                        isRecording = true;
                    }
                };
                timer.schedule(task, 1000);
            }
            if(event.getAction() == KeyEvent.ACTION_UP){
                //如果正在录制
                upTime = System.currentTimeMillis();
                if((upTime - downTime) < 1000){
                    timer.cancel();
                    task.cancel();

                }

                mButton_record.setBackgroundResource(R.drawable.record1);
                mButton_record.setTextColor(0xffffffff);
                if(isRecording){
                    //停止录制
                    mRecord.stop();
                    //释放资源
                    mRecord.release();
                    mRecord = null;
                    isRecording = false;

                    String fileName = mfile.getPath();
                    db.execSQL("insert into Dialog(id,text,image,voice,video) values(?,?,?,?,?)",new Object[]{index, null, null,null,fileName});
                    finish();
                }

            }
            return false;
        }
    }



}
