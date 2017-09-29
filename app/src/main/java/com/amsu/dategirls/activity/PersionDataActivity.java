package com.amsu.dategirls.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.ProvinceModel;
import com.amsu.dategirls.bean.User;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.common.Constant;
import com.amsu.dategirls.util.MyUtil;
import com.amsu.dategirls.util.ParseXmlDataUtil;
import com.amsu.dategirls.view.CircleImageView;
import com.amsu.dategirls.view.DateTimeDialogOnlyYMD;
import com.amsu.dategirls.view.PickerView;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersionDataActivity extends BaseActivity implements DateTimeDialogOnlyYMD.MyOnDateSetListener{

    private static final String TAG = "PersionDataActivity";
    private CircleImageView iv_personcenter_iocn;
    private TextView tv_personcenter_nickname;
    private TextView tv_personcenter_city;
    private TextView tv_personcenter_birthday;
    private TextView tv_personcenter_phone;
    private DateTimeDialogOnlyYMD dateTimeDialogOnlyYMD;
    private String upLoadbirthday;
    private List<ProvinceModel> provinceModels;
    private String province;
    private String city;
    private String area = "" ;
    private TextView tv_personcenter_sex;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persion_data);
    }

    @Override
    protected void initView() {

        setLeftImage(R.drawable.back_normal);
        setCenterText("个人信息");
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout rl_personcenter_persondata = (RelativeLayout) findViewById(R.id.rl_personcenter_persondata);
        RelativeLayout rl_personcenter_nickname = (RelativeLayout) findViewById(R.id.rl_personcenter_nickname);
        RelativeLayout rl_personcenter_city = (RelativeLayout) findViewById(R.id.rl_personcenter_city);
        RelativeLayout rl_personcenter_birthday = (RelativeLayout) findViewById(R.id.rl_personcenter_birthday);
        RelativeLayout rl_personcenter_sex = (RelativeLayout) findViewById(R.id.rl_personcenter_sex);

        iv_personcenter_iocn = (CircleImageView) findViewById(R.id.iv_personcenter_iocn);
        tv_personcenter_nickname = (TextView) findViewById(R.id.tv_personcenter_nickname);
        tv_personcenter_city = (TextView) findViewById(R.id.tv_personcenter_city);
        tv_personcenter_birthday = (TextView) findViewById(R.id.tv_personcenter_birthday);
        tv_personcenter_phone = (TextView) findViewById(R.id.tv_personcenter_phone);
        tv_personcenter_sex = (TextView) findViewById(R.id.tv_personcenter_sex);

        MyOnClickListenet myOnClickListenet = new MyOnClickListenet();

        rl_personcenter_persondata.setOnClickListener(myOnClickListenet);
        rl_personcenter_nickname.setOnClickListener(myOnClickListenet);
        rl_personcenter_city.setOnClickListener(myOnClickListenet);
        rl_personcenter_birthday.setOnClickListener(myOnClickListenet);
        rl_personcenter_sex.setOnClickListener(myOnClickListenet);

        dateTimeDialogOnlyYMD = new DateTimeDialogOnlyYMD(this, this, true, true, true);
        intent = getIntent();

    }

    @Override
    protected void initData() {
        String stringValueFromSP = MyUtil.getStringValueFromSP(Constant.ICONURL);
        if (!stringValueFromSP.equals("")){
            BitmapUtils bitmapUtils = new BitmapUtils(this);
            bitmapUtils.display(iv_personcenter_iocn,stringValueFromSP);
        }
        String nickName = MyUtil.getStringValueFromSP(Constant.NICKNAME);
        String city = MyUtil.getStringValueFromSP(Constant.CITY);
        String birthday = MyUtil.getStringValueFromSP(Constant.BIRTHDAY);
        String phone = MyUtil.getStringValueFromSP(Constant.PHONE);
        String sex = MyUtil.getStringValueFromSP(Constant.SEX);

        tv_personcenter_nickname.setText(nickName);
        tv_personcenter_city.setText(city);
        tv_personcenter_birthday.setText(birthday);
        tv_personcenter_phone.setText(phone);
        tv_personcenter_sex.setText(sex);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK){
            String result = data.getStringExtra("result");
            if (requestCode==110){
                tv_personcenter_nickname.setText(result);
                intent.putExtra(Constant.NICKNAME,result);
                setResult(RESULT_OK,intent);
            }
            else if (requestCode==111){
                tv_personcenter_city.setText(result);
            }
            else if (requestCode==112){
                tv_personcenter_birthday.setText(result);
            }

            else if (requestCode==113){
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null,null);
                if (cursor != null && cursor.moveToFirst()) {
                    final String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)); //storage/emulated/0/360Browser/download/20151006063040806.jpg
                    Log.i(TAG, "path:" + path);
                    iv_personcenter_iocn.setImageBitmap(BitmapFactory.decodeFile(path));

                    final BmobFile icon = new BmobFile(new File(path));
                    MyUtil.showDialog("正在上传",PersionDataActivity.this);
                    icon.upload(new UploadFileListener() {
                       @Override
                       public void done(BmobException e) {
                           if (e==null){
                               User user = new User();
                               String userobjectId = MyUtil.getStringValueFromSP(Constant.USEROBJECTID);
                               user.setObjectId(userobjectId);
                               user.setIconUrl(icon.getFileUrl());
                               if (!userobjectId.equals("")){
                                   user.update(new UpdateListener() {
                                       @Override
                                       public void done(BmobException e) {
                                           MyUtil.hideDialog();
                                            if (e==null){
                                                MyUtil.showToask(PersionDataActivity.this,"上传成功");
                                                intent.putExtra(Constant.ICONURL,icon.getFileUrl());
                                                setResult(RESULT_OK,intent);
                                                MyUtil.putStringValueFromSP(Constant.ICONURL,icon.getFileUrl());
                                            }
                                            else {
                                                MyUtil.showToask(PersionDataActivity.this,"上传失败"+e.toString());
                                            }
                                       }
                                   });
                               }
                           }
                           else {
                               MyUtil.hideDialog();
                               MyUtil.showToask(PersionDataActivity.this,"上传失败"+e.toString());
                           }

                       }
                   });
                    cursor.close();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(Date date) {
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        upLoadbirthday = year+"-"+month+"-"+day;
        Log.i(TAG,"onDateSet:"+upLoadbirthday);
        tv_personcenter_birthday.setText(upLoadbirthday);

        User user = new User();
        String userobjectId = MyUtil.getStringValueFromSP(Constant.USEROBJECTID);
        user.setObjectId(userobjectId);
        user.setBirthday(upLoadbirthday);
        if (!userobjectId.equals("")){
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e==null){
                        //MyUtil.showToask(PersionDataActivity.this,"上传成功");
                        MyUtil.putStringValueFromSP(Constant.BIRTHDAY,upLoadbirthday);
                        intent.putExtra(Constant.BIRTHDAY,upLoadbirthday);
                        setResult(RESULT_OK,intent);
                    }
                    else {
                        //MyUtil.showToask(PersionDataActivity.this,"上传失败"+e.toString());
                    }
                }
            });
        }

    }

    class MyOnClickListenet implements View.OnClickListener{
        Intent intent = new Intent(PersionDataActivity.this, ModifyPersionItemActivity.class);
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_personcenter_persondata:
                    Intent pickiIntent = new Intent();
                    //匹配其过滤器
                    pickiIntent.setAction("android.intent.action.PICK");
                    pickiIntent.setType("image/*");
                    startActivityForResult(pickiIntent,113);
                    break;
                case R.id.rl_personcenter_nickname:
                    intent.putExtra("type",1);
                    intent.putExtra("value",tv_personcenter_nickname.getText().toString());
                    startActivityForResult(intent,110);
                    break;
                case R.id.rl_personcenter_city:
                    chooseAreaDialog();
                    break;
                case R.id.rl_personcenter_birthday:
                    dateTimeDialogOnlyYMD.hideOrShow();
                    break;
                case R.id.rl_personcenter_sex:
                    showSexdialog();
                    break;
            }
        }
    }

    public void showSexdialog(){
        final String[] items = {"男","女"};
        new AlertDialog.Builder(this)
                .setTitle("选择")
                .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final User user = new User();
                        String userobjectId = MyUtil.getStringValueFromSP(Constant.USEROBJECTID);
                        user.setObjectId(userobjectId);
                        if (which==0){
                            user.setSex("1");
                            tv_personcenter_sex.setText("男");
                        }
                        else {
                            user.setSex("0");
                            tv_personcenter_sex.setText("女");
                        }
                        if (!userobjectId.equals("")){
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        //MyUtil.showToask(PersionDataActivity.this,"上传成功");
                                        MyUtil.putStringValueFromSP(Constant.SEX,user.getSex());
                                        intent.putExtra(Constant.SEX,user.getSex());
                                        setResult(RESULT_OK,intent);
                                    }
                                    else {
                                        //MyUtil.showToask(PersionDataActivity.this,"上传失败"+e.toString());
                                    }
                                }
                            });
                        }


                        dialog.cancel();
                    }
                })
                .show();
    }

    private void chooseAreaDialog() {
        initProvinceData();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //初始化自定义布局参数
        LayoutInflater layoutInflater = getLayoutInflater();
        final View customLayout = layoutInflater.inflate(R.layout.dialog_pick_area, (ViewGroup)findViewById(R.id.customDialog));
        Button bt_pick_cancel = (Button) customLayout.findViewById(R.id.bt_pick_cancel);
        Button bt_pick_ok = (Button) customLayout.findViewById(R.id.bt_pick_ok);

        //为对话框设置视图
        builder.setView(customLayout);
        PickerView picker_provice = (PickerView)customLayout.findViewById(R.id.picker_provice);
        final PickerView picker_city = (PickerView)customLayout.findViewById(R.id.picker_city);
        //定义滚动选择器的数据项
        final ArrayList<String> grade = new ArrayList<>();
        for(int i=0;i<provinceModels.size();i++){
            grade.add(provinceModels.get(i).getName());
        }


        //省份的数据
        picker_provice.setData(grade);
        province = grade.get(grade.size()/2);
        picker_provice.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(int position) {
                Log.i(TAG,"选择了"+grade.get(position));
                province = grade.get(position);
                //heightValue = text;
                int provincePosition = getTextPosition(grade.get(position));

                //省份切换时的城市改变
                final List<String> cityList = provinceModels.get(provincePosition).getCityList();
                picker_city.setData(cityList);
                city = cityList.get(cityList.size()/2);

                area = province+city;
                picker_city.setOnSelectListener(new PickerView.onSelectListener() {
                    @Override
                    public void onSelect(int position) {
                        Log.i(TAG,"选择了"+cityList.get(position));
                        city = cityList.get(position);
                        area = province+city;


                    }
                });
            }
        });

        //城市的默认数据
        List<String> cityList = provinceModels.get(provinceModels.size() / 2).getCityList();
        picker_city.setData(cityList);
        picker_city.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(int position) {
                Log.i(TAG,"选择了"+grade.get(position));
            }
        });
        city = cityList.get(cityList.size()/2);
        area = province+city;

        //显示对话框
        final AlertDialog showAlertDialog = builder.show();

        bt_pick_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog.dismiss();
            }
        });

        bt_pick_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog.dismiss();
                tv_personcenter_city.setText(area);

                User user = new User();
                String userobjectId = MyUtil.getStringValueFromSP(Constant.USEROBJECTID);
                user.setObjectId(userobjectId);
                user.setCity(area);
                if (!userobjectId.equals("")){
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e==null){
                                //MyUtil.showToask(PersionDataActivity.this,"上传成功");
                                MyUtil.putStringValueFromSP(Constant.CITY,area);
                                intent.putExtra(Constant.CITY,area);
                                setResult(RESULT_OK,intent);
                            }
                            else {
                                //MyUtil.showToask(PersionDataActivity.this,"上传失败"+e.toString());
                            }
                        }
                    });
                }
            }
        });
    }

    private void initProvinceData() {
        provinceModels = ParseXmlDataUtil.parseXmlDataFromAssets("province_data.xml",this);
    }

    //返回文字在List中的位置
    private int getTextPosition(String s) {
        int index = 0;
        for (int i=0;i<provinceModels.size();i++){
            if (s.equals(provinceModels.get(i).getName())){
                index =  i;
            }
        }
        return index;
    }
}
