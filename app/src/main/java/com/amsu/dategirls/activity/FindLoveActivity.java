package com.amsu.dategirls.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.adapter.DatingEventAdapter;
import com.amsu.dategirls.adapter.FindloveListDataAdapter;
import com.amsu.dategirls.bean.DatingEvent;
import com.amsu.dategirls.bean.User;
import com.amsu.dategirls.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FindLoveActivity extends BaseActivity {

    private static final String TAG = "FindLoveActivity";
    private GridView gv_findlove_list;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_love);
    }

    @Override
    protected void initView() {
        setCenterText("一见倾心");
        setLeftImage(R.drawable.back_normal);
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gv_findlove_list = (GridView) findViewById(R.id.gv_findlove_list);

        gv_findlove_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Intent intent = new Intent(AlbumActivity.this, AlbumInfoShowActivity.class);
                intent.putExtra("albumName",albumCategoryList.get(position).getName());
                startActivity(intent);*/
            }
        });
    }

    @Override
    protected void initData() {
        userList = new ArrayList<>();
        BmobQuery<User> dynamicsBmobQuery = new BmobQuery<>();
        dynamicsBmobQuery.order("-createdAt");
        dynamicsBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                Log.i(TAG,"list:"+list.size());
                if (list!=null && list.size()>0){
                    userList = list;
                    gv_findlove_list.setAdapter(new FindloveListDataAdapter(userList,FindLoveActivity.this));
                }
            }
        });



    }
}
