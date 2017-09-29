package com.amsu.dategirls.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.User;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.common.Constant;
import com.amsu.dategirls.util.MyUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ModifyPersionItemActivity extends BaseActivity {

    private TextView ed_modifyuser_vlaue;
    private Intent intent;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_persion_item);
    }

    @Override
    protected void initView() {
        setLeftImage(R.drawable.back_normal);
        setRightText("完成");
        getIv_base_leftimage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getTv_base_rightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nickName = ed_modifyuser_vlaue.getText().toString();
                if (!nickName.equals(value)){
                    if (intent!=null){
                        intent.putExtra("result",nickName);
                        User user = new User();
                        String userobjectId = MyUtil.getStringValueFromSP(Constant.USEROBJECTID);
                        user.setObjectId(userobjectId);
                        user.setNickName(nickName);
                        if (!userobjectId.equals("")){
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        //MyUtil.showToask(PersionDataActivity.this,"上传成功");
                                        MyUtil.putStringValueFromSP(Constant.NICKNAME,nickName);
                                    }
                                    else {
                                        //MyUtil.showToask(PersionDataActivity.this,"上传失败"+e.toString());
                                    }
                                }
                            });
                        }
                        setResult(RESULT_OK, intent);
                    }
                }
                finish();
            }
        });
        ed_modifyuser_vlaue = (TextView) findViewById(R.id.ed_modifyuser_vlaue);

    }

    @Override
    protected void initData() {
        intent = getIntent();
        if (intent!=null){
            int type = intent.getIntExtra("type", -1);
            if (type==1){
                setCenterText("修改昵称");
                value = intent.getStringExtra("value");
                ed_modifyuser_vlaue.setText(value);
            }
        }

    }
}
