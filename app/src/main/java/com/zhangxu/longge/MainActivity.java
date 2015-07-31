package com.zhangxu.longge;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static final int TAKE_PHOTO = 1 ;
    private RelativeLayout user1;
    private RelativeLayout user2;

    private LinearLayout mPicture;
    private LinearLayout mVedio;
    private LinearLayout mTake_pic;
    private LinearLayout mTape;

    private TextView user1_tv;
    private TextView user2_tv;

    private ImageView user1_iv;
    private ImageView user2_iv;
    private ImageView add_button;

    private EditText edit_input;

    private ViewPager view_paper;

    private Fragment user1Fragment;
    private Fragment user2Fragment;

    private ArrayList<Fragment> fragArr;



    private DataBaseHelper helper;
    private SQLiteDatabase db;

    private Button sendButton;

    private int index;

    private RelativeLayout mdrawerlayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


    }



    private void initView() {
        user1 = (RelativeLayout) findViewById(R.id.user_1);
        user2 = (RelativeLayout) findViewById(R.id.user_2);

        mPicture = (LinearLayout) findViewById(R.id.picture);
        mVedio = (LinearLayout) findViewById(R.id.vedio);
        mTake_pic = (LinearLayout) findViewById(R.id.take_pic);
        mTape = (LinearLayout) findViewById(R.id.tape);

        mPicture.setOnClickListener(this);
        mVedio.setOnClickListener(this);
        mTake_pic.setOnClickListener(this);
        mTape.setOnClickListener(this);


        user1_tv = (TextView) findViewById(R.id.user1_tv);
        user2_tv = (TextView) findViewById(R.id.user2_tv);

        user1_iv = (ImageView) findViewById(R.id.user1_iv);
        user2_iv = (ImageView) findViewById(R.id.user2_iv);
        add_button = (ImageView) findViewById(R.id.add_button);

        edit_input = (EditText) findViewById(R.id.edit_input);

        edit_input.setOnClickListener(this);

        view_paper = (ViewPager) findViewById(R.id.view_paper);

        mdrawerlayout = (RelativeLayout) findViewById(R.id.drawer_layout);

        mdrawerlayout.setVisibility(View.GONE);


        add_button.setOnClickListener(this);


        user1.setOnClickListener(MyOnClickListener0);
        user2.setOnClickListener(MyOnClickListener1);


        user1Fragment = new User1Fragment();
        user2Fragment = new User2Fragment();

        fragArr = new ArrayList<Fragment>();

        sendButton = (Button) findViewById(R.id.send);

        sendButton.setOnClickListener(this);

        fragArr.add(user1Fragment);
        fragArr.add(user2Fragment);

        view_paper.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragArr));

        view_paper.setOnPageChangeListener(new MyOnPaperChangeListener());

        view_paper.setCurrentItem(0);

        user1_tv.setTextColor(0xff41b5e8);


        helper = new DataBaseHelper(this);

        db = helper.getWritableDatabase();



    }
//各种按钮的点击事件
    @Override
    public void onClick(View v) {

      switch (v.getId()) {

          case R.id.send:

          String text = edit_input.getText().toString();

          db.execSQL("insert into dialog(id,text,image) values(?,?,?)", new Object[]{index, text,null});

          Log.e("db", "" + index);

          refresh();

              edit_input.setText("");

              break;
          case R.id.add_button:

            mdrawerlayout.setVisibility(View.VISIBLE);

              break;

          case R.id.edit_input:
            mdrawerlayout.setVisibility(View.GONE);
              break;

          case R.id.take_pic:
              takePhoto();
      }

    }
//刷新界面
    private void refresh() {
        ((User1Fragment) user1Fragment).refresh();
        ((User2Fragment) user2Fragment).refresh();
    }

    //调用系统相机进行拍照
    private void takePhoto() {

        Uri imageUri = null;


        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile", "SD card is not avaiable/writeable right now.");
            return;
        }

        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");

        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        long time_ = System.currentTimeMillis();
        File out = new File("/sdcard/longge/" + "yuantu" + time_ +".jpg");
        imageUri = Uri.fromFile(out);

        // 获取拍照后未压缩的原图像，并保存在uri路径中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        String imageName = imageUri.getPath();

        db.execSQL("insert into dialog(id,text,image) values(?,?,?)", new Object[]{index, null,imageName});



        startActivityForResult(intent, TAKE_PHOTO);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode == Activity.RESULT_OK){
            refresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//用户1切换的点击事件
    private View.OnClickListener MyOnClickListener0 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            index = 0;
            view_paper.setCurrentItem(index);
        }
    };

//用户2切换的点击事件
    private View.OnClickListener MyOnClickListener1 = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            index = 1;
            view_paper.setCurrentItem(index);
        }
    };




    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

       ArrayList<Fragment> list;

        public MyFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> list) {
            super(fm);

            this.list = list;
        }

        @Override
        public Fragment getItem(int i) {
            return fragArr.get(i);
        }

        @Override
        public int getCount() {
            return fragArr.size();
        }
    }





//滑动切换的监听器
    private class MyOnPaperChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {

            Log.e("0000","ooooo");

            if(i==0){
                index = i;
                user1_tv.setTextColor(0xff41b5e8);
                Log.e("1111111","1111111");
            }else {
                user1_tv.setTextColor(0xff666666);
            }

            if(i==1){
                index = i;
                user2_tv.setTextColor(0xff41b5e8);
            }else{
                user2_tv.setTextColor(0xff666666);
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }


}
