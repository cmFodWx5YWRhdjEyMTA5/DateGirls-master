package com.amsu.dategirls.fragment.picture;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.activity.FindLoveActivity;
import com.amsu.dategirls.adapter.AlbumDataAdapter;
import com.amsu.dategirls.adapter.FindloveListDataAdapter;
import com.amsu.dategirls.bean.AlbumCategory;
import com.amsu.dategirls.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {


    private static final String TAG = "AlbumFragment";
    private View inflate;
    private List<AlbumCategory> albumCategories;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflate = inflater.inflate(R.layout.fragment_album, container, false);
        initView();
        return inflate;
    }

    private void initView() {
        final GridView gv_album_list = (GridView) inflate.findViewById(R.id.gv_album_list);
        albumCategories = new ArrayList<>();
        BmobQuery<AlbumCategory> dynamicsBmobQuery = new BmobQuery<>();
        dynamicsBmobQuery.order("-createdAt");
        dynamicsBmobQuery.findObjects(new FindListener<AlbumCategory>() {
            @Override
            public void done(List<AlbumCategory> list, BmobException e) {
                Log.i(TAG,"list:"+list.size());
                if (list!=null && list.size()>0){
                    albumCategories = list;
                    gv_album_list.setAdapter(new AlbumDataAdapter(albumCategories,getContext()));
                }
            }
        });
    }

}
