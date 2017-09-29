package com.amsu.dategirls.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.adapter.DatingEventAdapter;
import com.amsu.dategirls.bean.Article;
import com.amsu.dategirls.bean.DatingEvent;
import com.amsu.dategirls.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class DatingEventActivity extends BaseActivity {

    private static final String TAG = "DatingEventActivity";
    private List<DatingEvent> articleList;
    private ListView lv_dateevent_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_event);
    }

    @Override
    protected void initView() {
        setCenterText("活动列表");
        setLeftImage(R.drawable.back_normal);
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_dateevent_list = (ListView) findViewById(R.id.lv_dateevent_list);
        articleList = new ArrayList<>();

        lv_dateevent_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String articleurl = articleList.get(position).getArticleUrl();
                Log.i(TAG,"articleurl:"+articleurl);
                Intent intent = new Intent(DatingEventActivity.this,ShowArticleDetailActivity.class);
                intent.putExtra("articleurl",articleurl);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        BmobQuery<DatingEvent> dynamicsBmobQuery = new BmobQuery<>();
        dynamicsBmobQuery.order("-createdAt");
        dynamicsBmobQuery.findObjects(new FindListener<DatingEvent>() {
            @Override
            public void done(List<DatingEvent> list, BmobException e) {
                Log.i(TAG,"list:"+list.size());
                if (list!=null && list.size()>0){
                    articleList = list;
                    lv_dateevent_list.setAdapter(new DatingEventAdapter(articleList,DatingEventActivity.this));
                }
            }
        });
    }
}
