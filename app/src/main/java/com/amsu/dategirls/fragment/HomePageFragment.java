package com.amsu.dategirls.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.amsu.dategirls.R;
import com.amsu.dategirls.activity.BeautifulPictureActivity;
import com.amsu.dategirls.activity.DatingEventActivity;
import com.amsu.dategirls.activity.FindLoveActivity;
import com.amsu.dategirls.activity.ShowArticleDetailActivity;
import com.amsu.dategirls.bean.Article;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2016/7/7.
 */
public class HomePageFragment  extends Fragment implements View.OnClickListener{

    private static final String TAG = "DynamicPageFragment";
    private View inflate;
    private ViewPager vp_home_msg;
    int previousSelectedPosition = 0;
    private List<Article> articleList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.frgament_page_home, new LinearLayout(getActivity()),false);

        initView();



        return inflate;
    }

    private void initView() {
        TextView tv_base_centerText = (TextView) inflate.findViewById(R.id.tv_base_centerText);
        tv_base_centerText.setText("主页");

        RelativeLayout rl_home_findlove = (RelativeLayout) inflate.findViewById(R.id.rl_home_findlove);
        RelativeLayout rl_home_dataevent = (RelativeLayout) inflate.findViewById(R.id.rl_home_dataevent);
        RelativeLayout rl_home_beautimage = (RelativeLayout) inflate.findViewById(R.id.rl_home_beautimage);
        RelativeLayout rl_home_story = (RelativeLayout) inflate.findViewById(R.id.rl_home_story);

        rl_home_findlove.setOnClickListener(this);
        rl_home_dataevent.setOnClickListener(this);
        rl_home_beautimage.setOnClickListener(this);
        rl_home_story.setOnClickListener(this);

        articleList = new ArrayList<>();

        initViewPage();
    }

    private void initViewPage() {
        vp_home_msg = (ViewPager) inflate.findViewById(R.id.vp_home_msg);

        final LinearLayout ll_home_point = (LinearLayout) inflate.findViewById(R.id.ll_home_point);

        for (int i = 0; i < 4; i++){
            View pointView = new View(getActivity());
            pointView.setBackgroundResource(R.drawable.selector_bg_point);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            if(i != 0)
                layoutParams.leftMargin = 10;
            pointView.setEnabled(false);
            ll_home_point.addView(pointView, layoutParams);
        }

        vp_home_msg.setAdapter(new MyViewPageAdapter(getContext()));

        vp_home_msg.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int newPosition = position % 4;
                ll_home_point.getChildAt(previousSelectedPosition).setEnabled(false);
                ll_home_point.getChildAt(newPosition).setEnabled(true);
                previousSelectedPosition  = newPosition;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        loadArticleData();

        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                vp_home_msg.setCurrentItem(vp_home_msg.getCurrentItem() + 1);
                            }
                        });
                    }
                }
            }
        }.start();
    }



    private void loadArticleData() {
        BmobQuery<Article> dynamicsBmobQuery = new BmobQuery<>();
        dynamicsBmobQuery.setLimit(4);
        dynamicsBmobQuery.addWhereEqualTo("isHomeArticle",true);
        dynamicsBmobQuery.order("-createdAt");
        dynamicsBmobQuery.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                Log.i(TAG,"list:"+list.size());
                if (list!=null && list.size()==4){
                    articleList = list;
                }




            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_home_findlove:
                startActivity(new Intent(getActivity(), FindLoveActivity.class));
                break;
            case R.id.rl_home_dataevent:
                startActivity(new Intent(getActivity(), DatingEventActivity.class));
                break;
            case R.id.rl_home_beautimage:
                startActivity(new Intent(getActivity(), BeautifulPictureActivity.class));
                break;
            case R.id.rl_home_story:

                break;
        }
    }

    class MyViewPageAdapter extends PagerAdapter {
        BitmapUtils bitmapUtils;

        public MyViewPageAdapter(Context context) {
            this.bitmapUtils = new BitmapUtils(context);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition = position % 4;
            //ImageView imageView = new ImageView(getActivity());
            /*BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), iamges[newPosition], options);
            imageView.setImageBitmap(bitmap);*/
            View inflate = View.inflate(getActivity(),R.layout.view_homeviewpage,null);
            ImageView iv_home_pageitem = (ImageView) inflate.findViewById(R.id.iv_home_pageitem);
            TextView tv_home_itemtitle = (TextView) inflate.findViewById(R.id.tv_home_itemtitle);

            if (newPosition<articleList.size()){
                final Article article = articleList.get(newPosition);
                bitmapUtils.display(iv_home_pageitem,article.getArticleImageUrl());
                tv_home_itemtitle.setText(article.getArticleTitle());

                inflate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String articleurl = article.getArticleUrl();
                        Log.i(TAG,"articleurl:"+articleurl);
                        Intent intent = new Intent(getActivity(),ShowArticleDetailActivity.class);
                        intent.putExtra("articleurl",articleurl);

                /*int count = article.getCount();
                String objectId = article.getObjectId();
                intent.putExtra("count",count);
                intent.putExtra("objectId",objectId);*/

                        startActivity(intent);
                    }
                });


            }

            container.addView(inflate);
            return inflate;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}