package com.amsu.dategirls.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.adapter.DynamicsAdapter;
import com.amsu.dategirls.bean.Dynamics;
import com.amsu.dategirls.bean.FriendRelation;
import com.amsu.dategirls.bean.User;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.common.Constant;
import com.amsu.dategirls.util.MyUtil;
import com.amsu.dategirls.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserHomepageActivity extends BaseActivity {

    private static final String TAG = "UserHomepageActivity";
    private ListView lv_homepage_dynamics;
    private List<Dynamics> dynamicsList;
    private DynamicsAdapter dynamicsAdapter;
    private TextView tv_me_nickname;
    private CircleImageView cv_me_icon;
    private TextView tv_me_city;
    private TextView tv_me_age;
    private ImageView iv_me_sex;
    private String mUserID;
    private User mUser;
    private TextView tv_homepage_follow;
    private int FOLLOW_STATE_FOLLED = 0;
    private int FOLLOW_STATE_UNFOLLED = 1;
    private FriendRelation mFriendRelation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    @Override
    protected void initView() {
        setCenterText("个人主页");
        setLeftImage(R.drawable.back_normal);
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cv_me_icon = (CircleImageView) findViewById(R.id.cv_me_icon);
        tv_me_nickname = (TextView) findViewById(R.id.tv_me_nickname);
        tv_me_city = (TextView) findViewById(R.id.tv_me_city);
        tv_me_age = (TextView) findViewById(R.id.tv_me_age);
        iv_me_sex = (ImageView) findViewById(R.id.iv_me_sex);

        lv_homepage_dynamics = (ListView) findViewById(R.id.lv_homepage_dynamics);

        TextView tv_homepage_privatemsg = (TextView) findViewById(R.id.tv_homepage_privatemsg);
        tv_homepage_follow = (TextView) findViewById(R.id.tv_homepage_follow);


    }

    @Override
    protected void initData() {
        final Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mFriendRelation = bundle.getParcelable("friendRelation");

        if (mFriendRelation ==null){
            Dynamics dynamics = bundle.getParcelable("dynamics");
            mUserID = dynamics.getUserID();
        }
        else {
            mUserID = mFriendRelation.getOtherUserID();
        }

        BmobQuery<User> userBmobQuery = new BmobQuery<>();
        userBmobQuery.addWhereEqualTo("userID",mUserID);
        userBmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null){
                    if (list!=null && list.size()>0){
                        mUser = list.get(0);
                        BitmapUtils bitmapUtils = new BitmapUtils(UserHomepageActivity.this);
                        if (mUser!=null){
                            bitmapUtils.display(cv_me_icon, mUser.getIconUrl());
                        }
                        tv_me_nickname.setText(mUser.getNickName());
                        tv_me_city.setText(mUser.getCity());
                        tv_me_age.setText(MyUtil.getUserAge(mUser.getBirthday())+"");
                        if (mUser.getSex().equals("1")){//男
                            iv_me_sex.setImageResource(R.drawable.male);
                        }
                        else {
                            iv_me_sex.setImageResource(R.drawable.female);
                        }
                    }
                }
                else {
                    //MyUtil.showToask(UserHomepageActivity.this,"个人信息加载失败");
                }
            }
        });

        BmobQuery<FriendRelation> friendRelationBmobQuery = new BmobQuery<>();
        friendRelationBmobQuery.addWhereEqualTo("myUserID", MyUtil.getStringValueFromSP(Constant.USERID));
        //dynamicsBmobQuery.setLimit(10);
        //dynamicsBmobQuery.order("-createdAt");

        if (mFriendRelation !=null){
            //已关注
            changeFollowSTate(FOLLOW_STATE_FOLLED);
        }
        else {
            friendRelationBmobQuery.findObjects(new FindListener<FriendRelation>() {
                @Override
                public void done(List<FriendRelation> list, BmobException e) {
                    if (e==null){
                        //成功
                        Log.i(TAG,"list:"+list.size());
                        //MyUtil.showToask(MyFollowActivity.this,"加载成功");
                        if (list.size() > 0){
                            for (FriendRelation f:list){
                                if (f.getOtherUserID().equals(mUserID)){
                                    //已关注
                                    changeFollowSTate(FOLLOW_STATE_FOLLED);
                                    mFriendRelation = f;
                                }
                            }

                        }
                    }
                    else {
                        Log.i(TAG,"e:"+e);
                        //MyUtil.showToask(MyFollowActivity.this,"加载失败");
                    }
                }
            });

        }


        BmobQuery<Dynamics> dynamicsBmobQuery = new BmobQuery<>();
        dynamicsBmobQuery.setLimit(10);
        dynamicsBmobQuery.addWhereEqualTo("userID",mUserID);
        dynamicsBmobQuery.order("-createdAt");
        dynamicsBmobQuery.findObjects(new FindListener<Dynamics>() {
            @Override
            public void done(List<Dynamics> list, BmobException e) {
                if (e==null){
                    //成功
                    Log.i(TAG,"list:"+list.size());
                    //MyUtil.showToask(UserHomepageActivity.this,"加载成功");
                    if (list.size() > 0){
                        dynamicsList = list;
                        if (dynamicsAdapter==null){
                            dynamicsAdapter = new DynamicsAdapter(dynamicsList,UserHomepageActivity.this);
                            lv_homepage_dynamics.setAdapter(dynamicsAdapter);
                        }
                        else {
                            dynamicsAdapter.notifyDataSetChanged();
                        }

                    }
                }
                else {
                    Log.i(TAG,"e:"+e);
                    //MyUtil.showToask(UserHomepageActivity.this,"加载失败");
                }

            }
        });
    }

    private void changeFollowSTate(int state) {
        if (state==FOLLOW_STATE_FOLLED){
            tv_homepage_follow.setBackgroundResource(R.drawable.bg_text_followed);
            tv_homepage_follow.setText("取消关注");
            tv_homepage_follow.setTextColor(Color.parseColor("#BABABA"));
        }
        else if (state==FOLLOW_STATE_UNFOLLED){
            tv_homepage_follow.setBackgroundResource(R.drawable.bg_text_unfollow);
            tv_homepage_follow.setText("关注");
            tv_homepage_follow.setTextColor(Color.parseColor("#FFFFFF"));
        }

    }

    public void follow(View view) {
        if (tv_homepage_follow.getText().toString().equals("关注")){
            //关注
            changeFollowSTate(FOLLOW_STATE_FOLLED);
            final String friendRelationID = "user" + System.currentTimeMillis();
            if (mUser==null){
                MyUtil.showToask(UserHomepageActivity.this,"好友初始化错误");
                return;
            }
            FriendRelation friendRelation = new FriendRelation(friendRelationID,mUserID, MyUtil.getStringValueFromSP(Constant.USERID),mUser.getNickName(),mUser.getIconUrl(),mUser.getCity(),mUser.getSex());
            friendRelation.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e==null){

                    }
                    else {
                        Log.i(TAG,"s:"+s+"e:"+e.toString());
                    }
                }
            });

        }
        else {
            //取消关注
            changeFollowSTate(FOLLOW_STATE_UNFOLLED);
            if (mFriendRelation!=null){
                Log.i(TAG,"mFriendRelation:"+mFriendRelation.toString());
                FriendRelation friendRelation = new FriendRelation();
                friendRelation.setObjectId(mFriendRelation.getObjectId());
                //Log.i(TAG,"friendRelation:"+friendRelation.toString());
                friendRelation.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){

                        }
                        else {
                            Log.i(TAG,"e:"+e.toString());
                            MyUtil.showToask(UserHomepageActivity.this,e.toString());
                        }
                    }
                });
            }
        }
    }

    public void privateMsg(View view) {
        Intent intent = new Intent(this,ChattingActivity.class);
        intent.putExtra(Constant.USERID,mUserID);
        if (mUser!=null){
            intent.putExtra(Constant.NICKNAME,mUser.getNickName());
        }
        startActivity(intent);
    }
}
