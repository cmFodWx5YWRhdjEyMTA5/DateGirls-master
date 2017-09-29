package com.amsu.dategirls.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.amsu.dategirls.R;
import com.amsu.dategirls.activity.LoginActivity;
import com.amsu.dategirls.activity.MyFollowActivity;
import com.amsu.dategirls.activity.PersionDataActivity;
import com.amsu.dategirls.activity.SettingActivity;
import com.amsu.dategirls.common.Constant;
import com.amsu.dategirls.util.MyUtil;
import com.amsu.dategirls.view.CircleImageView;
import com.lidroid.xutils.BitmapUtils;

/**
 * Created by Administrator on 2016/7/7.
 */
public class MePageFragment extends Fragment {

    private static final String TAG = "MePageFragment";
    private View inflate;
    private String currentPhone;
    private String nickName;
    private TextView tv_me_nickname;
    private CircleImageView cv_me_icon;
    private String apkUrl;
    private TextView tv_me_city;
    private TextView tv_me_age;
    private ImageView iv_me_sex;
    private BitmapUtils bitmapUtils;
    private String iconUrl;
    private int age;
    private String city;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.frgament_page_me, new LinearLayout(getActivity()),false);
        initView();



        return inflate;
    }

    private void initView() {
        Log.i(TAG,"initView");
        TextView tv_base_centerText = (TextView) inflate.findViewById(R.id.tv_base_centerText);
        tv_base_centerText.setText("我的");
        cv_me_icon = (CircleImageView) inflate.findViewById(R.id.cv_me_icon);
        tv_me_nickname = (TextView) inflate.findViewById(R.id.tv_me_nickname);
        tv_me_city = (TextView) inflate.findViewById(R.id.tv_me_city);
        tv_me_age = (TextView) inflate.findViewById(R.id.tv_me_age);
        iv_me_sex = (ImageView) inflate.findViewById(R.id.iv_me_sex);

        LinearLayout ll_me_personinf = (LinearLayout) inflate.findViewById(R.id.ll_me_personinf);
        LinearLayout ll_me_myfriend = (LinearLayout) inflate.findViewById(R.id.ll_me_myfriend);
        LinearLayout ll_me_invite = (LinearLayout) inflate.findViewById(R.id.ll_me_invite);
        LinearLayout ll_me_applyhis = (LinearLayout) inflate.findViewById(R.id.ll_me_applyhis);
        LinearLayout ll_me_setting = (LinearLayout) inflate.findViewById(R.id.ll_me_setting);


        currentPhone = MyUtil.getStringValueFromSP(Constant.PHONE);
        apkUrl = MyUtil.getStringValueFromSP("apkUrl");

        if (!currentPhone.equals("")){
            nickName =  MyUtil.getStringValueFromSP(Constant.NICKNAME);

            if (nickName.equals("")){
                tv_me_nickname.setText(currentPhone);
            }
            else {
                tv_me_nickname.setText(nickName);
            }
        }
        bitmapUtils = new BitmapUtils(getActivity());
        iconUrl = MyUtil.getStringValueFromSP(Constant.ICONURL);
        //Log.i(TAG,"iconUrl:"+stringValueFromSP);
        if (!iconUrl.equals("")){

            bitmapUtils.display(cv_me_icon, iconUrl);
        }
        String birthday = MyUtil.getStringValueFromSP(Constant.BIRTHDAY);
        if (!birthday.equals("")){
            age = MyUtil.getUserAge(birthday);
            tv_me_age.setText(age+"");
        }

        city = MyUtil.getStringValueFromSP(Constant.CITY);
        if (!city.equals("")){
            tv_me_city.setText(city);
        }

        MyOnClickListener myOnClickListener = new MyOnClickListener();

        cv_me_icon.setOnClickListener(myOnClickListener);

        ll_me_personinf.setOnClickListener(myOnClickListener);
        ll_me_invite.setOnClickListener(myOnClickListener);
        ll_me_applyhis.setOnClickListener(myOnClickListener);
        ll_me_setting.setOnClickListener(myOnClickListener);
        ll_me_myfriend.setOnClickListener(myOnClickListener);


       /* //查询apk地址
        BmobQuery<Apk> apkBmobQuery = new BmobQuery<>();
        apkBmobQuery.findObjects(getActivity(), new FindListener<Apk>() {
            @Override
            public void onSuccess(List<Apk> list) {
                if (list!=null){
                    apkUrl = list.get(0).getApkUrl();
                   // MyApplication.sharedPreferences.edit().putString()
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });*/

        //saveFileToSD();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        Log.i(TAG,"onResume isRefresh:"+Constant.isRefresh);
        /*if (isRefresh){
            Log.i(TAG,"age:"+age+",city:"+city+",iconurl:"+iconUrl+",nickname:"+nickName);
            refreshView();
        }
        */
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");
        Log.i(TAG,"onStart isRefresh:"+Constant.isRefresh);
        if (Constant.isRefresh){
            Log.i(TAG,"age:"+age+",city:"+city+",iconurl:"+iconUrl+",nickname:"+nickName);
            refreshView();
            Constant.isRefresh = false;
        }
    }

    private void refreshView() {
        if (!currentPhone.equals("")){
            nickName =  MyUtil.getStringValueFromSP(Constant.NICKNAME);

            if (nickName.equals("")){
                tv_me_nickname.setText(currentPhone);
            }
            else {
                tv_me_nickname.setText(nickName);
            }
        }
        bitmapUtils = new BitmapUtils(getActivity());
        iconUrl = MyUtil.getStringValueFromSP(Constant.ICONURL);
        //Log.i(TAG,"iconUrl:"+stringValueFromSP);
        if (!iconUrl.equals("")){
            bitmapUtils.display(cv_me_icon, iconUrl);
        }
        String birthday = MyUtil.getStringValueFromSP(Constant.BIRTHDAY);
        if (!birthday.equals("")){
            age = MyUtil.getUserAge(birthday);
            tv_me_age.setText(age+"");
        }

        city = MyUtil.getStringValueFromSP(Constant.CITY);
        if (!city.equals("")){
            tv_me_city.setText(city);
        }
    }

    /*private void saveFileToSD() {
        String path = Environment.getExternalStorageDirectory() + "/logo.png";
        File file = new File(path);
        if (file.exists()){
            return;
        }
        Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
        boolean res = logoBitmap.compress(Bitmap.CompressFormat.PNG, 100, logoStream);
        //将图像读取到logoStream中
        byte[] logoBuf = logoStream.toByteArray();
        //将图像保存到byte[]中
        Bitmap temp = BitmapFactory.decodeByteArray(logoBuf, 0, logoBuf.length);
        //将图像从byte[]中读取生成Bitmap 对象 temp
        saveMyBitmap(path,temp);
    }


    //将图像保存到SD卡中
    public void saveMyBitmap(String path,Bitmap mBitmap){

        File f = new File(path);
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_me_setting:
                    startActivity(new Intent(getActivity(),SettingActivity.class));
                    break;
                case R.id.cv_me_icon:
                    if (!currentPhone.equals("")){
                        getActivity().startActivityForResult(new Intent(getActivity(),PersionDataActivity.class),120);
                    }
                    else {
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                    }
                    break;
                case R.id.ll_me_personinf:
                    if (!currentPhone.equals("")){
                        getActivity().startActivityForResult(new Intent(getActivity(),PersionDataActivity.class),120);
                    }
                    else {
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                    }
                    break;
                case R.id.ll_me_myfriend:
                    if (!currentPhone.equals("")){
                        getActivity().startActivity(new Intent(getActivity(),MyFollowActivity.class));
                    }
                    else {
                        startActivity(new Intent(getActivity(),LoginActivity.class));
                    }
                    break;
                case R.id.ll_me_invite:

                    //share();
                    break;
                case R.id.ll_me_applyhis:
                    //startActivity(new Intent(getActivity(), LookupApplyActivity.class));

                    break;
            }
        }
    }

  /*  private void share() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("58名校贷，专业的大学生贷款软件");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(apkUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(apkUrl);

        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        String path = Environment.getExternalStorageDirectory() + "/logo.png";
        oks.setImagePath(path);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(apkUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("很好用的软件");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(apkUrl);

        //隐藏掉不需要显示的平台
        oks.addHiddenPlatform("SinaWeibo");
        oks.addHiddenPlatform("ShortMessage");

        // 启动分享GUI
        oks.show(getActivity());
    }
*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG,"requestCode:"+requestCode+",resultCode:"+resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK && requestCode==120){
            String birthday = data.getStringExtra(Constant.BIRTHDAY);
            String city = data.getStringExtra(Constant.CITY);
            String sex = data.getStringExtra(Constant.SEX);
            String iconUrl = data.getStringExtra(Constant.ICONURL);
            String nickName = data.getStringExtra(Constant.NICKNAME);
            Log.i(TAG,"birthday:"+birthday+",city:"+city+",sex:"+sex+",iconurl:"+iconUrl+",nickname:"+nickName);

            if (!MyUtil.isEmpty(iconUrl) || !MyUtil.isEmpty(birthday) || !MyUtil.isEmpty(city) || !MyUtil.isEmpty(nickName)){
                Constant.isRefresh = true;
            }

            if (!MyUtil.isEmpty(iconUrl)){
                this.iconUrl = iconUrl;
            }
            if (!MyUtil.isEmpty(birthday)){
                this.age= MyUtil.getUserAge(birthday);
            }
            if (!MyUtil.isEmpty(city)){
                this.city = city;
            }
            if (!MyUtil.isEmpty(nickName)){
                this.nickName  = nickName;
            }

            Log.i(TAG,"onActivityResult: isRefresh=="+Constant.isRefresh);

        }

    }




}
