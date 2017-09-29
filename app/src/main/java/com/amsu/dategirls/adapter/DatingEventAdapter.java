package com.amsu.dategirls.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.DatingEvent;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by hai on 2017/9/29.
 */

public class DatingEventAdapter extends BaseAdapter {
    private List<DatingEvent> articleList;
    private Context context;
    private BitmapUtils bitmapUtils;

    public DatingEventAdapter(List<DatingEvent> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return articleList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View inflate = View.inflate(context, R.layout.view_item_dataevent, null);
        ImageView iv_dataeventitem_iamge = (ImageView) inflate.findViewById(R.id.iv_dataeventitem_iamge);
        TextView tv_dataeventitem_title = (TextView) inflate.findViewById(R.id.tv_dataeventitem_title);
        TextView tv_dataeventitem_time = (TextView) inflate.findViewById(R.id.tv_dataeventitem_time);
        TextView tv_dataeventitem_state = (TextView) inflate.findViewById(R.id.tv_dataeventitem_state);
        TextView tv_dataeventitem_partNumber = (TextView) inflate.findViewById(R.id.tv_dataeventitem_partNumber);

        DatingEvent datingEvent = articleList.get(position);
        bitmapUtils.display(iv_dataeventitem_iamge,datingEvent.getImageUrl());

        tv_dataeventitem_title.setText(datingEvent.getTitle());
        tv_dataeventitem_time.setText("活动时间："+datingEvent.getTime());
        if (datingEvent.getState()==1){
            tv_dataeventitem_state.setText("进行中");
            tv_dataeventitem_state.setTextColor(context.getResources().getColor(R.color.app_main_backcolor));
        }
        else {
            tv_dataeventitem_state.setText("已结束");
            tv_dataeventitem_state.setTextColor(context.getResources().getColor(R.color.app_dataevent_over));
        }
        tv_dataeventitem_partNumber.setText("参与人数："+datingEvent.getPartNumber());
        return inflate;
    }
}
