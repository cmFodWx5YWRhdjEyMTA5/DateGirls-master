package com.amsu.dategirls.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.AlbumCategory;
import com.amsu.dategirls.bean.User;
import com.amsu.dategirls.view.CircleImageView;
import com.amsu.dategirls.view.RoundRectImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by hai on 2017/9/29.
 */

public class AlbumDataAdapter extends BaseAdapter {
    private List<AlbumCategory> userList;
    private Context context;
    private BitmapUtils bitmapUtils;

    public AlbumDataAdapter(List<AlbumCategory> userList, Context context) {
        this.userList = userList;
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return userList.size();
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
        AlbumCategory albumCategory = userList.get(position);
        View inflate = View.inflate(context, R.layout.view_item_album, null);
        RoundRectImageView iv_album_iamge = (RoundRectImageView) inflate.findViewById(R.id.iv_album_iamge);
        TextView iv_album_count = (TextView) inflate.findViewById(R.id.iv_album_count);
        TextView iv_album_name = (TextView) inflate.findViewById(R.id.iv_album_name);
        TextView iv_album_lovecount = (TextView) inflate.findViewById(R.id.iv_album_lovecount);

        String iconUrl = albumCategory.getIconUrl();
        String name = albumCategory.getName();
        iv_album_name.setText(name);
        iv_album_count.setText(albumCategory.getPictureCount()+"张图片");
        iv_album_lovecount.setText(albumCategory.getLoveCount()+"");
        bitmapUtils.display(iv_album_iamge, iconUrl);



        return inflate;
    }
}