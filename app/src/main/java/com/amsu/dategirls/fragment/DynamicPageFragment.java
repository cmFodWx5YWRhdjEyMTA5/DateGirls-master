package com.amsu.dategirls.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.amsu.dategirls.R;
import com.amsu.dategirls.activity.AddDynamicsActivity;
import com.amsu.dategirls.activity.DynamicsDetialActivity;
import com.amsu.dategirls.adapter.DynamicsAdapter;
import com.amsu.dategirls.bean.Dynamics;
import com.amsu.dategirls.common.Constant;
import com.amsu.dategirls.util.MyUtil;
import com.amsu.dategirls.view.LastMsgListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Administrator on 2016/7/7.
 */
public class DynamicPageFragment extends Fragment {

    private static final String TAG = "DynamicPageFragment";
    private View inflate;
    private LastMsgListView lv_dynamic_list;
    private List<Dynamics> dynamicsList;
    private DynamicsAdapter dynamicsAdapter;
    private boolean isNeedRefreshDynasic;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.frgament_page_dynamic, new LinearLayout(getActivity()),false);
        initView();
        initData();
        return inflate;
    }

    private void initView() {
        TextView tv_base_centerText = (TextView) inflate.findViewById(R.id.tv_base_centerText);
        tv_base_centerText.setText("动态");

        lv_dynamic_list = (LastMsgListView) inflate.findViewById(R.id.lv_dynamic_list);
        RelativeLayout rl_dynamic_add = (RelativeLayout) inflate.findViewById(R.id.rl_dynamic_add);
        rl_dynamic_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddDynamicsActivity.class);
                getActivity().startActivityForResult(intent,100);
            }
        });

        dynamicsList = new ArrayList<>();

        lv_dynamic_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dynamics dynamics = dynamicsList.get(position-1);
                Log.i(TAG,"onItemClick:"+position);
                Intent intent = new Intent(getActivity(), DynamicsDetialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("dynamics",dynamics);
                intent.putExtra("bundle",bundle);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        lv_dynamic_list.setRefreshDataListener(new LastMsgListView.RefreshDataListener() {
            @Override
            public void refresh() {
                initData();

            }

            @Override
            public void loadMore() {
                loadMoreData();
            }
        });
    }

    private void loadMoreData() {
        if (dynamicsList.size()>0){
            Dynamics dynamics = dynamicsList.get(dynamicsList.size()-1);
            String createdAt = dynamics.getCreatedAt();
            Log.i(TAG,"createdAt:"+createdAt);
            BmobQuery<Dynamics> dynamicsBmobQuery = new BmobQuery<>();
            dynamicsBmobQuery.setLimit(10);
            dynamicsBmobQuery.order("-createdAt");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = null;
            try {
                parse = sdf.parse(createdAt);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (parse==null){
                return;
            }
            dynamicsBmobQuery.addWhereLessThan("createdAt",new BmobDate(parse));
            dynamicsBmobQuery.findObjects(new FindListener<Dynamics>() {
                @Override
                public void done(List<Dynamics> list, BmobException e) {
                    lv_dynamic_list.loadMoreSuccessd();
                    if (e==null){
                        //成功
                        Log.i(TAG,"list:"+list.size());
                        MyUtil.showToask(getActivity(),"加载成功");
                        if (list.size() > 0){
                            dynamicsList.addAll(list);
                            dynamicsAdapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        Log.i(TAG,"e:"+e);
                        MyUtil.showToask(getActivity(),"加载失败");
                    }
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constant.dynamics!=null){
            if (dynamicsAdapter!=null){
                dynamicsList.add(0,Constant.dynamics);
                dynamicsAdapter.notifyDataSetChanged();
            }
            Constant.dynamics = null;
        }
    }

    private void initData() {
        BmobQuery<Dynamics> dynamicsBmobQuery = new BmobQuery<>();
        dynamicsBmobQuery.setLimit(10);
        dynamicsBmobQuery.order("-createdAt");
        dynamicsBmobQuery.findObjects(new FindListener<Dynamics>() {
            @Override
            public void done(List<Dynamics> list, BmobException e) {
                lv_dynamic_list.refreshSuccessed();

                if (e==null){
                    //成功
                    Log.i(TAG,"list:"+list.size());
                    MyUtil.showToask(getActivity(),"加载成功");
                    if (list.size() > 0){
                        dynamicsList.clear();
                        for (int i=0;i<list.size();i++){
                            dynamicsList.add(list.get(i));
                        }
                        dynamicsList = list;
                        if (dynamicsAdapter==null){
                            dynamicsAdapter = new DynamicsAdapter(dynamicsList,getActivity());
                            lv_dynamic_list.setAdapter(dynamicsAdapter);
                        }
                        else {
                            dynamicsAdapter.notifyDataSetChanged();
                        }

                    }
                }
                else {
                    Log.i(TAG,"e:"+e);
                    MyUtil.showToask(getActivity(),"加载失败");
                }

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"requestCode:"+requestCode+",resultCode:"+resultCode);
        if (requestCode==100 && resultCode==RESULT_OK){
            if (data!=null){
                Bundle bundle = data.getBundleExtra("bundle");
                Dynamics dynamics = bundle.getParcelable("dynamics");
                Constant.dynamics = dynamics;
                Log.i(TAG,"dynamics:"+dynamics.toString());
            }
        }
    }
}
