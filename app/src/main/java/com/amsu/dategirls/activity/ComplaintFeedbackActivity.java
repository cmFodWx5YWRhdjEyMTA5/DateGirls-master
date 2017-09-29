package com.amsu.dategirls.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amsu.dategirls.R;
import com.amsu.dategirls.application.MyApplication;
import com.amsu.dategirls.common.BaseActivity;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class ComplaintFeedbackActivity extends BaseActivity {

	private ProgressDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complaint_feedback);


	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {

	}

	public void submitSuggest(View view) {
		EditText et_feedback_input = (EditText) findViewById(R.id.et_feedback_input);
		String msg = et_feedback_input.getText().toString();
		if (!msg.isEmpty()){
			showDialog("正在提交");
			String phone = MyApplication.sharedPreferences.getString("phone","");
			Suggestion suggestion = new Suggestion(phone,msg);

			suggestion.save(new SaveListener<String>() {
				@Override
				public void done(String s, BmobException e) {
					if (e==null){
						hideDialog();
						Toast.makeText(ComplaintFeedbackActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
						finish();
					}
					else {
						hideDialog();
						Toast.makeText(ComplaintFeedbackActivity.this,"提交失败"+s,Toast.LENGTH_SHORT).show();
					}
				}
			});
		}
		else {
			Toast.makeText(this,"请输入内容呀",Toast.LENGTH_SHORT).show();
		}


	}

	void showDialog(String message) {
		try {
			if (dialog == null) {
				dialog = new ProgressDialog(this);
				dialog.setCancelable(true);
			}
			dialog.setMessage(message);
			dialog.show();
		} catch (Exception e) {
			// 在其他线程调用dialog会报错
		}
	}

	void hideDialog() {
		if (dialog != null && dialog.isShowing())
			try {
				dialog.dismiss();
			} catch (Exception e) {
			}
	}

	class Suggestion extends BmobObject {
		String userPhone;
		String suggestion;

		public String getUserPhone() {
			return userPhone;
		}

		public void setUserPhone(String userPhone) {
			this.userPhone = userPhone;
		}

		public String getSuggestion() {
			return suggestion;
		}

		public void setSuggestion(String suggestion) {
			this.suggestion = suggestion;
		}

		public Suggestion(String userPhone, String suggestion) {
			this.userPhone = userPhone;
			this.suggestion = suggestion;
		}
	}

}

