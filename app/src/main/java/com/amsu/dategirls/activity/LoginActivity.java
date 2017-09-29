package com.amsu.dategirls.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.User;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.util.CodeTimerTaskUtil;
import com.amsu.dategirls.util.MyUtil;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    private EditText et_login_phone;
    private EditText et_login_code;
    private Button bt_login_getcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        setCenterText("登陆");
        setLeftImage(R.drawable.back_normal);
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et_login_phone = (EditText) findViewById(R.id.et_login_phone);
        et_login_code = (EditText) findViewById(R.id.et_login_code);
        bt_login_getcode = (Button) findViewById(R.id.bt_login_getcode);
        final Button bt_login_nextstep = (Button) findViewById(R.id.bt_login_nextstep);
    }

    private void startUpdateCodeTime() {
        CodeTimerTaskUtil codeTimerTaskUtil = new CodeTimerTaskUtil(this);
        codeTimerTaskUtil.setOnTimeChangeListerner(60, 1000, new CodeTimerTaskUtil.OnTimeChangeListerner() {
            @Override
            public void onFormatStringTimeChange(int currentScend) {
                Log.i(TAG,"currentScend:"+currentScend);
                bt_login_getcode.setText(currentScend+"");

            }

            @Override
            public void onTimeOut() {
                bt_login_getcode.setText("获取验证码");
                bt_login_getcode.setClickable(true);
                bt_login_getcode.setTextSize(10);
                bt_login_getcode.setBackgroundResource(R.drawable.bg_button_verifycode);
            }
        });
        codeTimerTaskUtil.startTime();
    }


    @Override
    protected void initData(){

        EventHandler eh = new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                Log.i(TAG,"afterEvent:"+result+","+event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        startLogin();
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                        sendSucces();

                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表

                    }
                }
                else{
                    //event:2,result:0,data:java.lang.Throwable: {"status":603,"detail":"请填写正确的手机号码"}
                    String resultData = data.toString();
                    String josnData = resultData.split(":")[1]+":"+resultData.split(":")[2]+":"+resultData.split(":")[3];
                    Log.i(TAG,"josnData:"+josnData);
                    String detail ="";
                    int status = 0;
                    try {
                        JSONObject jsonObject = new JSONObject(josnData);
                        status = jsonObject.getInt("status");
                        detail = jsonObject.getString("detail");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.i(TAG,"status:"+status+",detail:"+detail);

                    final String finalDetail = detail;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyUtil.showToask(LoginActivity.this, finalDetail);
                        }
                    });

                    MyUtil.hideDialog();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }

    private void startLogin() {
        MyUtil.showDialog("查询数据",this);
        final String phone = et_login_phone.getText().toString();
        final User user = new User();
        user.setPhone(phone);
        //user.setUserID("user"+System.currentTimeMillis());

        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("phone",phone);
        bmobQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null){
                    Log.i(TAG,"list:"+list.size());
                    if (list.size()==0){
                        MyUtil.showDialog("正在注册",LoginActivity.this);
                        final String userID = "user" + System.currentTimeMillis();
                        user.setUserID(userID);
                        user.setObjectId(System.currentTimeMillis()+"");
                        user.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e==null){
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                MyUtil.showDialog("环信数据校验",LoginActivity.this);
                                                EMClient.getInstance().createAccount(user.getPhone(), user.getPhone());//同步方法
                                                MyUtil.saveUserToSP(user);
                                                MyUtil.hideDialog();
                                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                            }
                                            catch (final HyphenateException e1) {
                                                e1.printStackTrace();
                                                Log.i(TAG,"注册失败:"+e1.toString());
                                                runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        MyUtil.hideDialog();
                                                        int errorCode= e1.getErrorCode();
                                                        if(errorCode== EMError.NETWORK_ERROR){
                                                            Toast.makeText(LoginActivity.this, "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
                                                        }else if(errorCode == EMError.USER_ALREADY_EXIST){
                                                            Toast.makeText(LoginActivity.this, "用户已存在！", Toast.LENGTH_SHORT).show();
                                                        }else if(errorCode == EMError.USER_AUTHENTICATION_FAILED){
                                                            Toast.makeText(LoginActivity.this, "注册失败，无权限！", Toast.LENGTH_SHORT).show();
                                                        }else if(errorCode == EMError.USER_ILLEGAL_ARGUMENT){
                                                            Toast.makeText(LoginActivity.this, "用户名不合法", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(LoginActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    }.start();

                                }
                                else {
                                    Log.i(TAG,e.toString()+""+e.getErrorCode());
                                    MyUtil.hideDialog();
                                    MyUtil.showToask(LoginActivity.this,e.toString());
                                }

                            }
                        });
                    }
                    else {
                        User user1 = list.get(0);
                        MyUtil.hideDialog();
                        MyUtil.saveUserToSP(user1);
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }
                }
                else {
                    e.printStackTrace();
                    MyUtil.showToask(LoginActivity.this,"登陆失败，请检查网络");
                    MyUtil.hideDialog();
                }
            }
        });

    }

    private void sendSucces() {
        MyUtil.hideDialog();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bt_login_getcode.setClickable(false);
                bt_login_getcode.setBackgroundResource(R.drawable.bg_button_code_disable);
                bt_login_getcode.setTextSize(15);
                bt_login_getcode.setText(60+"");
                MyUtil.showToask(LoginActivity.this,"验证码发送成功");
                startUpdateCodeTime();
            }
        });

    }

    public void getVerifyCode(View view) {
        String phone = et_login_phone.getText().toString();
        if(!TextUtils.isEmpty(phone)){
            SMSSDK.getVerificationCode("86", phone);//86为国家代码
            MyUtil.showDialog("正在获取",this);
        }else{
            MyUtil.showToask(this,"输入手机号");
            bt_login_getcode.setClickable(true);
        }
    }

    public void login(View view) {

        startLogin();

        /*String phone = et_login_phone.getText().toString();
        String code = et_login_code.getText().toString();
        if(TextUtils.isEmpty(phone)){
            MyUtil.showToask(this,"输入手机号");
        }
        else if(TextUtils.isEmpty(code)){
            MyUtil.showToask(this,"输入验证码");
        }
        else{
            SMSSDK.submitVerificationCode("86", phone, code);
            MyUtil.showDialog("正在校验",this);
        }*/
    }
}
