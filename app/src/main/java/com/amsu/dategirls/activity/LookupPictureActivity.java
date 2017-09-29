package com.amsu.dategirls.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.Dynamics;
import com.amsu.dategirls.common.BaseActivity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

public class LookupPictureActivity extends BaseActivity {
    private List<String> imageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_picture);

    }

    protected void initView() {
        ViewPager vp_picture_image = (ViewPager) findViewById(R.id.vp_picture_image);

        Intent intent = getIntent();
        if (intent!=null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            Dynamics dynamics = bundle.getParcelable("dynamics");
            if (dynamics!=null){
                imageUrls = dynamics.getImageList();
                MyViewPageAdapter myViewPageAdapter = new MyViewPageAdapter();
                vp_picture_image.setAdapter(myViewPageAdapter);
            }

        }


    }

    @Override
    protected void initData() {

    }

    class MyViewPageAdapter extends PagerAdapter{
        BitmapUtils bitmapUtils ;

        public MyViewPageAdapter() {
            bitmapUtils = new BitmapUtils(LookupPictureActivity.this);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(LookupPictureActivity.this);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            bitmapUtils.display(imageView,imageUrls.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            container.addView(imageView,imageViewParams);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
