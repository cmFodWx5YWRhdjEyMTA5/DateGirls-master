package com.amsu.dategirls.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.FriendRelation;
import com.amsu.dategirls.util.MyUtil;
import com.amsu.dategirls.view.RoundRectImageView;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by HP on 2017/3/29.
 */

public class FriendRelationAdapter extends BaseAdapter{
    List<FriendRelation> friendRelationList;
    Context context;
    BitmapUtils bitmapUtils;

    public FriendRelationAdapter(List<FriendRelation> friendRelationList,Context context) {
        this.friendRelationList = friendRelationList;
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public int getCount() {
        return friendRelationList.size();
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
        FriendRelation friendRelation = friendRelationList.get(position);
        View inflate ;
        MyHolder myHolder;
        if (convertView!=null){
            inflate = convertView;
            myHolder  = (MyHolder) inflate.getTag();
        }
        else {
            inflate = View.inflate(context, R.layout.list_friend_item, null);
            myHolder = new MyHolder();
            myHolder.iv_friend_image = (RoundRectImageView) inflate.findViewById(R.id.iv_friend_image);
            myHolder.tv_friend_name = (TextView) inflate.findViewById(R.id.tv_friend_name);
            myHolder.tv_friend_city = (TextView) inflate.findViewById(R.id.tv_friend_city);
            myHolder.iv_friend_sex = (ImageView) inflate.findViewById(R.id.iv_friend_sex);
            inflate.setTag(myHolder);
        }
        if (!MyUtil.isEmpty(friendRelation.getOtherIconUrl())){
            bitmapUtils.display(myHolder.iv_friend_image,friendRelation.getOtherIconUrl());
        }

        myHolder.tv_friend_name.setText(friendRelation.getOtherNickName());
        myHolder.tv_friend_city.setText(friendRelation.getOtherCity());
        if (friendRelation.getOtherSex().equals("1")){//男
            myHolder.iv_friend_sex.setImageResource(R.drawable.male);
        }
        else {
            myHolder.iv_friend_sex.setImageResource(R.drawable.female);
        }

        return inflate;
    }

    class MyHolder {
        RoundRectImageView iv_friend_image;
        TextView tv_friend_name ;
        TextView tv_friend_city ;
        ImageView iv_friend_sex ;

    }
}
