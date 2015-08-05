package com.zhangxu.longge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


public class Image extends Activity {

    private ImageView mImage_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("image_show");

        mImage_show = (ImageView) findViewById(R.id.image_show);

        mImage_show.setImageBitmap(BitmapFactory.decodeFile(imagePath));

        mImage_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



}
