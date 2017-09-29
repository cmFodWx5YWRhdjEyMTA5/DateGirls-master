package com.amsu.dategirls.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.adapter.FragmentListAdapter;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.fragment.picture.AlbumFragment;
import com.amsu.dategirls.fragment.picture.LastPictureFragment;
import com.amsu.dategirls.fragment.picture.MostPopularPictureFragment;
import com.amsu.dategirls.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

public class BeautifulPictureActivity extends BaseActivity {

    private float mOneTableWidth;
    private ViewPager vp_picture_fragment;
    private List<Fragment> fragmentList;
    private TextView tv_picture_last;
    private TextView tv_picture_album;
    private TextView tv_picture_popular;
    private View v_analysis_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beautiful_picture);
    }

    @Override
    protected void initView() {
        setCenterText("美图");
        setLeftImage(R.drawable.back_normal);
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vp_picture_fragment = (ViewPager) findViewById(R.id.vp_picture_fragment);

        tv_picture_last = (TextView) findViewById(R.id.tv_picture_last);
        tv_picture_popular = (TextView) findViewById(R.id.tv_picture_popular);
        tv_picture_album = (TextView) findViewById(R.id.tv_picture_album);

        v_analysis_select = findViewById(R.id.v_analysis_select);


        MyClickListener myClickListener = new MyClickListener();
        tv_picture_last.setOnClickListener(myClickListener);
        tv_picture_popular.setOnClickListener(myClickListener);
        tv_picture_album.setOnClickListener(myClickListener);

        fragmentList = new ArrayList<>();
        fragmentList.add(new LastPictureFragment());
        fragmentList.add(new MostPopularPictureFragment());
        fragmentList.add(new AlbumFragment());

        FragmentListAdapter mAnalysisRateAdapter = new FragmentListAdapter(getSupportFragmentManager(), fragmentList);
        vp_picture_fragment.setAdapter(mAnalysisRateAdapter);

        //每一个小格的宽度
        mOneTableWidth = MyUtil.getScreeenWidth(this)/3;

        vp_picture_fragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Log.i(TAG,"onPageScrolled===position:"+position+",positionOffset:"+positionOffset+",positionOffsetPixels:"+positionOffsetPixels);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v_analysis_select.getLayoutParams();
                int floatWidth=  (int) (mOneTableWidth *(positionOffset+position));  //view向左的偏移量
                layoutParams.setMargins(floatWidth,0,0,0); //4个参数按顺序分别是左上右下
                v_analysis_select.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                //Log.i(TAG,"onPageSelected===position:"+position);
                setViewPageTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Log.i(TAG,"onPageScrollStateChanged===state:"+state);
            }
        });
    }

    @Override
    protected void initData() {

    }

    //点击时设置选中条目
    public void setViewPageItem(int viewPageItem,int currentItem) {
        if (currentItem==viewPageItem){
            return;
        }
        vp_picture_fragment.setCurrentItem(viewPageItem);
        RelativeLayout.LayoutParams layoutParams =   (RelativeLayout.LayoutParams) v_analysis_select.getLayoutParams();
        int floatWidth= (int) (mOneTableWidth*viewPageItem);  //view向左的偏移量
        layoutParams.setMargins(floatWidth,0,0,0); //4个参数按顺序分别是左上右下
        v_analysis_select.setLayoutParams(layoutParams);

        setViewPageTextColor(viewPageItem);

    }

    //设置文本颜色
    private void setViewPageTextColor(int viewPageItem) {
        switch (viewPageItem){
            case 0:
                tv_picture_last.setTextColor(Color.parseColor("#0c64b5"));
                tv_picture_popular.setTextColor(Color.parseColor("#999999"));
                tv_picture_album.setTextColor(Color.parseColor("#999999"));
                break;
            case 1:
                tv_picture_last.setTextColor(Color.parseColor("#999999"));
                tv_picture_popular.setTextColor(Color.parseColor("#0c64b5"));
                tv_picture_album.setTextColor(Color.parseColor("#999999"));
                break;
            case 2:
                tv_picture_last.setTextColor(Color.parseColor("#999999"));
                tv_picture_popular.setTextColor(Color.parseColor("#999999"));
                tv_picture_album.setTextColor(Color.parseColor("#0c64b5"));
                break;


        }
    }

    private class MyClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_picture_last:
                    setViewPageItem(0,vp_picture_fragment.getCurrentItem());
                    break;
                case R.id.tv_picture_popular:
                    setViewPageItem(1,vp_picture_fragment.getCurrentItem());
                    break;
                case R.id.tv_picture_album:
                    setViewPageItem(2,vp_picture_fragment.getCurrentItem());
                    break;
            }
        }
    }
}
