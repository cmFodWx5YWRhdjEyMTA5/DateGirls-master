package com.amsu.dategirls.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.amsu.dategirls.R;
import com.amsu.dategirls.bean.Dynamics;
import com.amsu.dategirls.common.BaseActivity;
import com.amsu.dategirls.common.Constant;
import com.amsu.dategirls.selectphoto.SelectPhotoAdapter;
import com.amsu.dategirls.util.MyBitMapUtil;
import com.amsu.dategirls.util.MyUtil;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class AddDynamicsActivity extends BaseActivity {

    private static final String TAG = "AddDynamicsActivity";
    private EditText et_add_input;
    private BottomSheetDialog bottomSheetDialog;
    private Button bt_dialog_take;
    private Button bt_dialog_choose;
    private Button bt_dialog_cancel;
    private File currentImageSaveFile;
    private MyOnClickListener myOnClickListener;
    private LinearLayout ll_select_images;
    private LinearLayout rl_add_bottom;
    private ImageView iv_add_icon;
    private LinearLayout addLinearLayout;
    private String[] mImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dynamics);
    }

    @Override
    protected void initView() {
        setCenterText("添加动态");
        setLeftText("取消");
        getTv_base_leftText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectPhotoAdapter.mSelectedImage!=null){
                    SelectPhotoAdapter.mSelectedImage.clear();
                }
                finish();
            }
        });
        setRightText("发布");
        getTv_base_rightText().setTextColor(Color.parseColor("#999999"));
        getTv_base_rightText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.showDialog("加载中",AddDynamicsActivity.this);
                //发布
                String content = et_add_input.getText().toString();
                String dynamicsID = "dy"+System.currentTimeMillis();
                String userID = MyUtil.getStringValueFromSP(Constant.USERID);
                String nickName = MyUtil.getStringValueFromSP(Constant.NICKNAME);
                String iconUrl = MyUtil.getStringValueFromSP(Constant.ICONURL);
                final List<String> imageList = new ArrayList<>();
                String dynamicsFormatTime = MyUtil.getDynamicsFormatTime(new Date());

                final Dynamics dynamics = new Dynamics(dynamicsID,userID,dynamicsFormatTime,content,nickName,iconUrl,imageList,0,0);


                if (mImages!=null && mImages.length>0){
                    //先上传文件，上传成功后插入数据库
                    for (int i=0;i<mImages.length;i++){
                        Bitmap bitmap = BitmapFactory.decodeFile(mImages[i]);
                        Bitmap bitmap1 = MyBitMapUtil.compressImage(bitmap);
                        mImages[i] = MyBitMapUtil.saveBitmapFile(bitmap1,AddDynamicsActivity.this).getAbsolutePath();
                    }

                    BmobFile.uploadBatch(mImages, new UploadBatchListener() {
                        @Override
                        public void onSuccess(List<BmobFile> list, List<String> list1) {
                            Log.i(TAG,"onSuccess:"+","+list1.size());
                            if (mImages.length==list1.size()){
                                //全部上传成功
                                for (int i=0;i<list1.size();i++){
                                    Log.i(TAG,"list1.get(i):"+list1.get(i));
                                }

                                dynamics.setImageList(list1);
                                dynamics.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        MyUtil.hideDialog();
                                        if(e==null){
                                            MyUtil.showToask(AddDynamicsActivity.this,"发送成功");
                                            if (SelectPhotoAdapter.mSelectedImage!=null){
                                                SelectPhotoAdapter.mSelectedImage.clear();
                                            }
                                            Intent intent = getIntent();
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("dynamics",dynamics);
                                            intent.putExtra("bundle",bundle);
                                            setResult(RESULT_OK,intent);
                                            finish();
                                        }else{
                                            Log.i(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                                            MyUtil.showToask(AddDynamicsActivity.this,"发送失败");
                                        }
                                    }
                                });
                            }

                        }

                        @Override
                        public void onProgress(int i, int i1, int i2, int i3) {

                        }

                        @Override
                        public void onError(int i, String s) {
                            MyUtil.hideDialog();
                            MyUtil.showToask(AddDynamicsActivity.this,"发送失败");
                            Log.i(TAG,"onError:"+s+",");
                        }
                    });
                    /*for (int i=0;i<mImages.length;i++){
                        imageList.add(mImages[i]);
                    }*/
                }
                else {
                    dynamics.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            MyUtil.hideDialog();
                            if(e==null){
                                MyUtil.showToask(AddDynamicsActivity.this,"发送成功");
                                if (SelectPhotoAdapter.mSelectedImage!=null){
                                    SelectPhotoAdapter.mSelectedImage.clear();
                                }
                                Intent intent = getIntent();
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("dynamics",dynamics);
                                intent.putExtra("bundle",bundle);
                                setResult(RESULT_OK,intent);
                                finish();
                            }else{
                                Log.i(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                                MyUtil.showToask(AddDynamicsActivity.this,"发送失败");
                            }
                        }
                    });
                }
            }
        });

        et_add_input = (EditText) findViewById(R.id.et_add_input);
        myOnClickListener = new MyOnClickListener();
        rl_add_bottom = (LinearLayout) findViewById(R.id.rl_add_bottom);
        ll_select_images = (LinearLayout) findViewById(R.id.ll_select_images);

        iv_add_icon = (ImageView) findViewById(R.id.iv_add_icon);
        iv_add_icon.setOnClickListener(myOnClickListener);

        et_add_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0){
                    getTv_base_rightText().setTextColor(Color.parseColor("#000000"));
                    getTv_base_rightText().setClickable(true);
                }
                else {
                    getTv_base_rightText().setTextColor(Color.parseColor("#999999"));
                    getTv_base_rightText().setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addLinearLayout = new LinearLayout(AddDynamicsActivity.this);
    }

    @Override
    protected void initData() {

    }

    private void addPicture() {
        bottomSheetDialog = new BottomSheetDialog(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.choose_pcicture_dailog, null);

        bottomSheetDialog.setContentView(inflate);
        Window window = bottomSheetDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle);  //添加动画
        bottomSheetDialog.show();

        bt_dialog_take = (Button) inflate.findViewById(R.id.bt_dialog_take);
        bt_dialog_choose = (Button) inflate.findViewById(R.id.bt_dialog_choose);
        bt_dialog_cancel = (Button) inflate.findViewById(R.id.bt_dialog_cancel);


        bt_dialog_take.setOnClickListener(myOnClickListener);
        bt_dialog_choose.setOnClickListener(myOnClickListener);
        bt_dialog_cancel.setOnClickListener(myOnClickListener);

    }


    class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_add_icon:
                    addPicture();
                    break;
                case R.id.bt_dialog_choose:
                    bottomSheetDialog.dismiss();
                    chooosePicture();
                    break;
                case R.id.bt_dialog_take:
                    bottomSheetDialog.dismiss();
                    takePicture();
                    break;
                case R.id.bt_dialog_cancel:
                    bottomSheetDialog.dismiss();
                    break;
            }
        }
    }

    private void takePicture() {
        Intent tackIntent = new Intent();
        //匹配其过滤器
        tackIntent.setAction("android.media.action.IMAGE_CAPTURE");
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/tiyu");
        if (!file.exists()){
            boolean mkdirs = file.mkdirs();
            if (!mkdirs){
                return;
            }
        }
        ///storage/emulated/0/cts/1469067312871.png
        currentImageSaveFile = new File(file,System.currentTimeMillis() + ".png");
        tackIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageSaveFile));
        startActivityForResult(tackIntent,101);
    }

    private void chooosePicture() {
        Intent intent = new Intent(this,SelectPhotoActivity.class);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG,"requestCode:"+requestCode+",resultCode:"+resultCode);


        if (resultCode==RESULT_OK && requestCode==100){
            if (data!=null){
                if (mImages==null){
                    mImages = data.getStringArrayExtra("images");
                }
                else {
                    String[] images = data.getStringArrayExtra("images");
                    String[] temp = new String[mImages.length+images.length];
                    System.arraycopy(mImages, 0, temp, 0, mImages.length);
                    System.arraycopy(images, 0, temp, mImages.length, images.length);
                    mImages = temp;
                }
                displayImage(mImages);
            }
        }
        else if (resultCode==RESULT_OK && requestCode==101){
            //拍照
            if (mImages==null){
                mImages = new String[1];
                mImages[0] = currentImageSaveFile.getAbsolutePath();
            }
            else {
                String[] temp = new String[mImages.length+1];
                System.arraycopy(mImages, 0, temp, 0, mImages.length);
                temp[mImages.length] = currentImageSaveFile.getAbsolutePath();
                mImages = temp;
            }
            displayImage(mImages);
        }
    }

    private void displayImage(String[] mImages) {
        int imageHeight = (int) getResources().getDimension(R.dimen.x192);
        int margin = (int) getResources().getDimension(R.dimen.x12);
        BitmapUtils bitmapUtils = new BitmapUtils(AddDynamicsActivity.this);

        if (mImages.length>0){
            getTv_base_rightText().setTextColor(Color.parseColor("#000000"));
            getTv_base_rightText().setClickable(true);
            ll_select_images.removeAllViews();
            rl_add_bottom.removeAllViews();

            LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(imageHeight,imageHeight);
            if (mImages.length<=4){
                //imageView.setId
                for (int i = 0; i< mImages.length; i++){
                    Log.i(TAG,"images:"+ mImages[i]);
                    ImageView imageView = new ImageView(AddDynamicsActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (i>0){
                        imageViewParams.setMargins(0,0,margin,0); //图片之间水平间距
                    }
                    bitmapUtils.display(imageView, mImages[i]);

                    ll_select_images.addView(imageView,imageViewParams);
                }
                ll_select_images.addView(iv_add_icon);
                rl_add_bottom.addView(ll_select_images);

            }
            else {
                addLinearLayout.removeAllViews();
                for (int i=0;i<5;i++){
                    ImageView imageView = new ImageView(AddDynamicsActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (i>0){
                        imageViewParams.setMargins(0,0,margin,0); //图片之间水平间距
                    }
                    bitmapUtils.display(imageView, mImages[i]);
                    ll_select_images.addView(imageView,imageViewParams);
                }
                rl_add_bottom.addView(ll_select_images);

                addLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams layoutParams =   new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0,margin,0,0);

                int newIndex = mImages.length-5;
                for (int i=0;i<newIndex;i++){
                    ImageView imageView = new ImageView(AddDynamicsActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    if (i>0){
                        imageViewParams.setMargins(0,0,margin,0); //图片之间水平间距
                    }
                    bitmapUtils.display(imageView, mImages[i+5]);
                    addLinearLayout.addView(imageView,imageViewParams);
                }

                addLinearLayout.addView(iv_add_icon);
                rl_add_bottom.addView(addLinearLayout,layoutParams);

            }
        }
        else {
            /*getTv_base_rightText().setTextColor(Color.parseColor("#999999"));
            getTv_base_rightText().setClickable(false);*/
        }

    }
}
