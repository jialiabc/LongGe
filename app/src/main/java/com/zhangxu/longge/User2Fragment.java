package com.zhangxu.longge;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User2Fragment extends Fragment {



    private View user2View;

    private ListView mlistView;

    private SQLiteDatabase db;
    private DataBaseHelper helper;

    private Context mContext;

    private List<Map<String,Object>> list;




    private DialogAdapter adapter;

    private int index;

    private  Cursor c;





    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        user2View = inflater.inflate(R.layout.fragment_user2,null);


        initView();

        refresh();

        return user2View;



    }

    public void refresh() {
        list.clear();
        initData();
        adapter.notifyDataSetChanged();
        mlistView.setSelection(list.size()-1);
    }

    private void initData() {


        db = helper.getReadableDatabase();

        c = db.query("dialog",null,null,null,null,null,null);




        for(c.moveToFirst();!c.isAfterLast();c.moveToNext()) {

            Map<String,Object> map = new HashMap<String, Object>();
            map.put("Text", c.getString(c.getColumnIndex("text")));
            map.put("Id", c.getInt(c.getColumnIndex("id")));
            map.put("Image",c.getString(c.getColumnIndex("image")));
            list.add(map);

            Log.e("LIST2", list.toString());

        }

    }


    private void initView() {

        list = new ArrayList<Map<String,Object>>();

        mlistView = (ListView) user2View.findViewById(R.id.list_view2);
        helper = new DataBaseHelper(mContext);

        adapter = new DialogAdapter();
        mlistView.setAdapter(adapter);




    }


    public void onPause(){
        super.onPause();


    }
    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
    }


    private class DialogAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            View view = null;
            Map map = list.get(position);
            index = Integer.parseInt(map.get("Id").toString());

            Log.e("index111111111",index+"");

            if(index==1){
                View layout_me = View.inflate(mContext,R.layout.layout_item_me,null);
                ImageView me_iv = (ImageView) layout_me.findViewById(R.id.head_me);
                TextView me_tv = (TextView) layout_me.findViewById(R.id.text_me);
                ImageView me_photo = (ImageView) layout_me.findViewById(R.id.image_me);

                me_iv.setImageResource(R.drawable.xiaohong);

                if(map.get("Text")!=null) {
                    me_tv.setText(map.get("Text").toString());
                }
                if(map.get("Image")!=null) {
                    String image = map.get("Image").toString();
                    Log.e("image2",image);
                    File file = new File(image);
                    if(file.exists()){
                        Log.e("cccccc",image);
                    }

                    Bitmap bm = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image), 100, 200);
                    me_photo.setImageBitmap(bm);


                }

                view = layout_me;
            }
            if(index==0){
                View layout_other = View.inflate(mContext,R.layout.layout_item_other,null);
                ImageView other_iv = (ImageView) layout_other.findViewById(R.id.head_other);
                TextView other_tv = (TextView) layout_other.findViewById(R.id.text_other);

                ImageView other_photo = (ImageView) layout_other.findViewById(R.id.image_other);

                other_iv.setImageResource(R.drawable.xiaoming);
                //添加文字对话
                if(map.get("Text")!=null) {
                    other_tv.setText(map.get("Text").toString());
                }
                //添加图片对话
                if(map.get("Image")!=null) {
                    String image = map.get("Image").toString();
                    File file = new File(image);
                    //判断文件是否存在
                    if(file.exists()){
                        Log.e("ddddddd" +
                                "",image);
                    }

                    Bitmap bm = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image),100,200);
                    other_photo.setImageBitmap(bm);

                }
                view = layout_other;
            }

            return view;
        }
    }
}
