package com.amsu.dategirls.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.adapter.CommentAdapter;
import com.amsu.dategirls.bean.Dynamics;
import com.amsu.dategirls.bean.FriendRelation;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class DynamicsDetialActivity extends BaseActivity {

    private static final String TAG = "DynamicsDetialActivity";
    private ImageView iv_item_image1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamics_detial);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);  //影藏输入键盘

    }

    @Override
    protected void initView() {
        initHeadView();
        setCenterText("动态详情");
        setLeftImage(R.drawable.back_normal);
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ListView lv_comment_list = (ListView) findViewById(R.id.lv_comment_list);
        View inflate = View.inflate(this, R.layout.view_item_dynamics, null);
        View text = View.inflate(this, R.layout.view_item_text, null);
        lv_comment_list.addHeaderView(inflate);
        lv_comment_list.addHeaderView(text);
        lv_comment_list.setAdapter(new CommentAdapter(this));

        CircleImageView cv_item_usericon = (CircleImageView) inflate.findViewById(R.id.cv_item_usericon);
        TextView tv_item_username = (TextView) inflate.findViewById(R.id.tv_item_username);
        TextView tv_item_time = (TextView) inflate.findViewById(R.id.tv_item_time);
        TextView tv_item_text = (TextView) inflate.findViewById(R.id.tv_item_text);
        
        final ImageView iv_item_surname = (ImageView) inflate.findViewById(R.id.iv_item_surname);
        ImageView iv_item_comment = (ImageView) inflate.findViewById(R.id.iv_item_comment);
        ImageView iv_item_share = (ImageView) inflate.findViewById(R.id.iv_item_share);
        final TextView iv_item_surnamecount = (TextView) inflate.findViewById(R.id.iv_item_surnamecount);
        TextView iv_item_commentcount = (TextView) inflate.findViewById(R.id.iv_item_commentcount);
        LinearLayout ll_dynamic_images = (LinearLayout) inflate.findViewById(R.id.ll_dynamic_images);
        RelativeLayout rl_item_surname = (RelativeLayout) inflate.findViewById(R.id.rl_item_surname);

        EditText et_detial_commentinput = (EditText) findViewById(R.id.et_detial_commentinput);



        Intent intent = getIntent();
        if (intent!=null){
            BitmapUtils bitmapUtils = new BitmapUtils(this);
            Bundle bundle = intent.getBundleExtra("bundle");
            final Dynamics dynamics = bundle.getParcelable("dynamics");
            List<String> imageList = dynamics.getImageList();

            rl_item_surname.setOnClickListener(new View.OnClickListener() {
                private boolean isSurnamed;
                @Override
                public void onClick(View v) {
                    if (!isSurnamed){
                        isSurnamed = true;
                        iv_item_surname.setImageResource(R.drawable.jlb_dzyz);
                        final int newCount =dynamics.getSurnameCount()+1;
                        iv_item_surnamecount.setText(newCount+"");  //点赞加1
                        dynamics.setSurnameCount(newCount);
                        //Log.i(TAG,"dynamics:"+dynamics.toString());
                        dynamics.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e==null){
                                    Log.i(TAG,"点赞成功");
                                }
                            }
                        });
                    }
                }
            });

            cv_item_usericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DynamicsDetialActivity.this, UserHomepageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("dynamics",dynamics);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }
            });

            tv_item_text.setText(dynamics.getContent());
            tv_item_time.setText(dynamics.getTime());
            tv_item_username.setText(dynamics.getUserNickName());
            iv_item_surnamecount.setText(dynamics.getSurnameCount()+"");
            iv_item_commentcount.setText(dynamics.getCommentCount()+"");
            bitmapUtils.display(cv_item_usericon,dynamics.getUserIconUrl());
            if (dynamics.getContent().equals("")){
                tv_item_text.setVisibility(View.GONE);
            }
            else {
                tv_item_text.setVisibility(View.VISIBLE);
            }

            int margin = (int) getResources().getDimension(R.dimen.x12);
            int imageHeight = (int) getResources().getDimension(R.dimen.x328);

            if (0<imageList.size() && imageList.size()<=9){
                ll_dynamic_images.removeAllViews(); //清楚所有子布局，防止布局累加
                ll_dynamic_images.setVisibility(View.VISIBLE);
                int lineCount = (imageList.size()-1) / 3 +1;  // 1  2 3
                Log.i(TAG,"lineCount:"+lineCount);

                for (int i=0;i<lineCount;i++){  //行
                    LinearLayout linearLayout = new LinearLayout(this);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    int jIndex = 3;
                    if (i==lineCount-1){
                        jIndex = imageList.size()%3;
                        if (jIndex==0){
                            jIndex = 3;
                        }
                    }
                    for (int j=0;j<jIndex;j++){  //每行三个图片
                        ImageView imageView = new ImageView(this);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(imageHeight,imageHeight);
                        if (j>0){
                            imageViewParams.setMargins(margin,0,0,0); //图片之间水平间距
                        }
                        bitmapUtils.display(imageView,imageList.get(j+3*i));
                        linearLayout.addView(imageView,imageViewParams);
                    }

                    LinearLayout.LayoutParams layoutParams =   new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,imageHeight);
                    if (i>0){
                        layoutParams.setMargins(0,margin,0,0); //图片之间垂直间距
                    }
                    ll_dynamic_images.addView(linearLayout,layoutParams);
                }

                ll_dynamic_images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(DynamicsDetialActivity.this, LookupPictureActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("dynamics",dynamics);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);
                    }
                });

            }
            else {
                ll_dynamic_images.setVisibility(View.GONE);
            }
            
            /*

            if (type==0){
                //显示评论页
                getWindow().setSoftInputMode(   WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);  //显示输入键盘

            }
            else if (type==1){

            }*/

            /*//查看图片，测试
            iv_item_image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(DynamicsDetialActivity.this,LookupPictureActivity.class);
                    intent1.putExtra("imageUrl",dynamics.getImageList()[0]);
                    startActivity(intent1);
                }
            });*/
        }

    }

    @Override
    protected void initData() {

    }


    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){

            }
        }
    }
}