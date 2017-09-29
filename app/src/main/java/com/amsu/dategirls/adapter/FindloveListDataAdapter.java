package com.amsu.dategirls.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.User;
import com.amsu.dategirls.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by hai on 2017/9/29.
 */

public class FindloveListDataAdapter extends BaseAdapter {
    private List<User> userList;
    private Context context;
    private BitmapUtils bitmapUtils;

    public FindloveListDataAdapter(List<User> userList, Context context) {
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
        User user = userList.get(position);
        View inflate = View.inflate(context, R.layout.view_item_findlove, null);
        CircleImageView iv_findlove_iamge = (CircleImageView) inflate.findViewById(R.id.iv_findlove_iamge);
        TextView iv_findlove_name = (TextView) inflate.findViewById(R.id.iv_findlove_name);
        final Button iv_findlove_follow = (Button) inflate.findViewById(R.id.iv_findlove_follow);

        String iconUrl = user.getIconUrl();
        String name = user.getNickName();
        iv_findlove_name.setText(name);
        bitmapUtils.display(iv_findlove_iamge, iconUrl);

        iv_findlove_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_findlove_follow.setBackgroundResource(R.drawable.bg_button_code_disable);
                iv_findlove_follow.setText("已关注");
            }
        });

        return inflate;
    }
}