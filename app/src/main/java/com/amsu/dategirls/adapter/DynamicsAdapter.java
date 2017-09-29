package com.amsu.dategirls.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.activity.DynamicsDetialActivity;
import com.amsu.dategirls.activity.LookupPictureActivity;
import com.amsu.dategirls.activity.MyFollowActivity;
import com.amsu.dategirls.activity.UserHomepageActivity;
import com.amsu.dategirls.bean.Dynamics;
import com.amsu.dategirls.view.CircleImageView;
import com.amsu.dategirls.view.MyRelativeLayout;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by HP on 2017/1/16.
 */

public class DynamicsAdapter extends BaseAdapter {
    private static final String TAG = "DynamicsAdapter";
    private List<Dynamics> dynamicsList;
    private Context context;
    private BitmapUtils bitmapUtils;
    int margin = 0;
    int imageHeight = 0;
    MyOnClickListener myOnClickListener;




    public DynamicsAdapter(List<Dynamics> dynamicsList, Context context) {
        this.dynamicsList = dynamicsList;
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
        margin = (int) context.getResources().getDimension(R.dimen.x12);
        imageHeight = (int) context.getResources().getDimension(R.dimen.x328);
        myOnClickListener = new MyOnClickListener();
    }

    @Override
    public int getCount() {
        return dynamicsList.size();
        //return 5;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.i("position",position+"");
        final Dynamics dynamics = dynamicsList.get(position);
        View inflate;
        final MyHolder myHolder;
        if (convertView!=null){
            inflate = convertView;
            myHolder = (MyHolder)inflate.getTag();
        }
        else {
            inflate = View.inflate(context, R.layout.view_item_dynamics, null);
            myHolder = new MyHolder();
            myHolder.cv_item_usericon = (CircleImageView) inflate.findViewById(R.id.cv_item_usericon);
            myHolder.tv_item_username = (TextView) inflate.findViewById(R.id.tv_item_username);
            myHolder.tv_item_time = (TextView) inflate.findViewById(R.id.tv_item_time);
            myHolder.tv_item_text = (TextView) inflate.findViewById(R.id.tv_item_text);
            myHolder.iv_item_surname = (ImageView) inflate.findViewById(R.id.iv_item_surname);
            myHolder.iv_item_comment = (ImageView) inflate.findViewById(R.id.iv_item_comment);
            myHolder.iv_item_share = (ImageView) inflate.findViewById(R.id.iv_item_share);
            myHolder.iv_item_surnamecount = (TextView) inflate.findViewById(R.id.iv_item_surnamecount);
            myHolder.iv_item_commentcount = (TextView) inflate.findViewById(R.id.iv_item_commentcount);
            myHolder.rl_item_surname = (RelativeLayout) inflate.findViewById(R.id.rl_item_surname);
            myHolder.rl_item_comment = (RelativeLayout) inflate.findViewById(R.id.rl_item_comment);
            myHolder.rl_item_share = (RelativeLayout) inflate.findViewById(R.id.rl_item_share);
            myHolder.ll_dynamic_images = (LinearLayout) inflate.findViewById(R.id.ll_dynamic_images);

            myHolder.rl_item_surname.setOnClickListener(new View.OnClickListener() {
                private boolean isSurnamed;
                @Override
                public void onClick(View v) {
                    if (!isSurnamed){
                        isSurnamed = true;
                        myHolder.iv_item_surname.setImageResource(R.drawable.jlb_dzyz);
                        final int newCount =dynamics.getSurnameCount()+1;
                        myHolder.iv_item_surnamecount.setText(newCount+"");  //点赞加1
                        dynamics.setSurnameCount(newCount);
                        //Log.i(TAG,"dynamics:"+dynamics.toString());
                        dynamics.increment("surnameCount");
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
           /* myHolder.rl_item_surname.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });*/

            //myHolder.rl_item_surname.dispatchTouchEvent()

            myHolder.iv_item_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DynamicsDetialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("dynamics",dynamics);
                    intent.putExtra("bundle",bundle);
                    intent.putExtra("type",0);
                    context.startActivity(intent);
                    //跳到评论页
                    /*myHolder.iv_item_comment.setImageResource(R.drawable.jlb_plyp);
                    int commentcount = Integer.parseInt(myHolder.iv_item_commentcount.getText().toString())+1;
                    myHolder.iv_item_commentcount.setText(commentcount+"");*/
                }
            });

            myHolder.iv_item_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            myHolder.cv_item_usericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserHomepageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("dynamics",dynamics);
                    intent.putExtra("bundle",bundle);
                    context.startActivity(intent);
                }
            });
            inflate.setTag(myHolder);
        }

        bitmapUtils.display( myHolder.cv_item_usericon,dynamics.getUserIconUrl());
        myHolder.tv_item_username.setText(dynamics.getUserNickName());
        myHolder.tv_item_time.setText(dynamics.getTime());
        myHolder.tv_item_text.setText(dynamics.getContent());
        myHolder.iv_item_surnamecount.setText(dynamics.getSurnameCount()+"");
        myHolder.iv_item_commentcount.setText(dynamics.getCommentCount()+"");
        List<String> imageList = dynamics.getImageList();
        if (dynamics.getContent().equals("")){
            myHolder.tv_item_text.setVisibility(View.GONE);
        }
        else {
            myHolder.tv_item_text.setVisibility(View.VISIBLE);
        }

        if (0<imageList.size() && imageList.size()<=9){
            myHolder.ll_dynamic_images.removeAllViews(); //清楚所有子布局，防止布局累加
            myHolder.ll_dynamic_images.setVisibility(View.VISIBLE);
            int lineCount = (imageList.size()-1) / 3 +1;  // 1  2 3
            Log.i(TAG,"lineCount:"+lineCount);

            for (int i=0;i<lineCount;i++){  //行
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                int jIndex = 3;
                if (i==lineCount-1){
                    jIndex = imageList.size()%3;
                    if (jIndex==0){
                        jIndex = 3;
                    }
                }
                for (int j=0;j<jIndex;j++){  //每行三个图片
                    ImageView imageView = new ImageView(context);
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
                myHolder.ll_dynamic_images.addView(linearLayout,layoutParams);
            }

            myHolder.ll_dynamic_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LookupPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("dynamics",dynamics);
                    intent.putExtra("bundle",bundle);
                    context.startActivity(intent);
                }
            });

        }
        else {
            myHolder.ll_dynamic_images.setVisibility(View.GONE);
        }
        return inflate;
    }

    class MyHolder{
        CircleImageView cv_item_usericon;
        TextView tv_item_username;
        TextView tv_item_time;
        TextView tv_item_text;
        ImageView iv_item_surname;
        ImageView iv_item_comment;
        ImageView iv_item_share;
        TextView iv_item_surnamecount ;
        TextView iv_item_commentcount ;
        RelativeLayout rl_item_surname;
        RelativeLayout rl_item_comment;
        RelativeLayout rl_item_share;
        LinearLayout ll_dynamic_images;

    }

    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_item_comment:

                    break;
                case R.id.ll_dynamic_images:

                    break;

            }
        }
    }
}
