package com.amsu.dategirls.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.adapter.FriendRelationAdapter;
import com.amsu.dategirls.bean.FriendRelation;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.common.Constant;
import com.amsu.dategirls.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by HP on 2017/3/29.
 */

public class MyFollowActivity extends BaseActivity {

    private static final String TAG = "MyFollowActivity";
    private ListView lv_myfollow_friend;
    private List<FriendRelation>  friendList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_follow);
    }

    @Override
    protected void initView() {
        setCenterText("我的关注");
        setLeftImage(R.drawable.back_normal);
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lv_myfollow_friend = (ListView) findViewById(R.id.lv_myfollow_friend);


        lv_myfollow_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendRelation friendRelation = friendList.get(position);
                Log.i(TAG,"friendRelation:"+friendRelation.toString());
                Intent intent = new Intent(MyFollowActivity.this, UserHomepageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("friendRelation",friendRelation);
                intent.putExtra("bundle",bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        friendList = new ArrayList<>();
        final

        BmobQuery<FriendRelation> dynamicsBmobQuery = new BmobQuery<>();
        dynamicsBmobQuery.addWhereEqualTo("myUserID", MyUtil.getStringValueFromSP(Constant.USERID));
        //dynamicsBmobQuery.setLimit(10);
        //dynamicsBmobQuery.order("-createdAt");

        dynamicsBmobQuery.findObjects(new FindListener<FriendRelation>() {
            @Override
            public void done(List<FriendRelation> list, BmobException e) {
                if (e==null){
                    //成功
                    Log.i(TAG,"list:"+list.size());
                    //MyUtil.showToask(MyFollowActivity.this,"加载成功");
                    if (list.size() > 0){
                        friendList = list;
                        FriendRelationAdapter friendRelationAdapter = new FriendRelationAdapter(friendList,MyFollowActivity.this);
                        lv_myfollow_friend.setAdapter(friendRelationAdapter);
                        friendRelationAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    Log.i(TAG,"e:"+e);
                    //MyUtil.showToask(MyFollowActivity.this,"加载失败");
                }
            }
        });


    }
}
