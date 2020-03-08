package com.xxxx.tky.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.PackageUtils;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.model.UploadlogBean;
import com.xxxx.tky.util.Cemera;
import com.xxxx.tky.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

public class FeedBackActivity extends BaseHttpRequestActivity implements View.OnLongClickListener {

    @BindView(R.id.iv_close)
    ImageView mIvArrow;
    @BindView(R.id.tv_title)
    TextView mTvHeaderTitle;
    @BindView(R.id.ed_afb_problem)
    EditText mEdAfbProblem;
    @BindView(R.id.tv_afb_current_text)
    TextView mTvAfbCurrentText;
    @BindView(R.id.iv_afb_cancel_1)
    ImageView mIvAfbCancel1;
    @BindView(R.id.iv_afb_1)
    ImageView mIvAfb1;
    @BindView(R.id.rl_afb_iv_1)
    RelativeLayout mRlAfbIv1;
    @BindView(R.id.iv_afb_cancel_2)
    ImageView mIvAfbCancel2;
    @BindView(R.id.iv_afb_2)
    ImageView mIvAfb2;
    @BindView(R.id.rl_afb_iv_2)
    RelativeLayout mRlAfbIv2;
    @BindView(R.id.iv_afb_cancel_3)
    ImageView mIvAfbCancel3;
    @BindView(R.id.iv_afb_3)
    ImageView mIvAfb3;
    @BindView(R.id.rl_afb_iv_3)
    RelativeLayout mRlAfbIv3;
    @BindView(R.id.iv_afb_cancel_4)
    ImageView mIvAfbCancel4;
    @BindView(R.id.iv_afb_4)
    ImageView mIvAfb4;
    @BindView(R.id.rl_afb_iv_4)
    RelativeLayout mRlAfbIv4;
    @BindView(R.id.iv_afb_cancel_5)
    ImageView mIvAfbCancel5;
    @BindView(R.id.iv_afb_5)
    ImageView mIvAfb5;
    @BindView(R.id.rl_afb_iv_5)
    RelativeLayout mRlAfbIv5;
    @BindView(R.id.et_afb_phone_email)
    EditText mEtAfbPhoneEmail;
    @BindView(R.id.ll_afb_phone_email)
    LinearLayout mLlAfbPhoneEmail;
    @BindView(R.id.bt_afb_commit)
    Button mBtAfbCommit;
    private ProgressDialog progressDialog;
    private UserBean cacheUserBean;
   private String logZipPath;
    private String logPath;
   private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm");
   boolean isRequestok=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        logCachePath=getApplicationContext().getExternalCacheDir()+"/logCache/";
        logCacheFile=logCachePath+"/logCacheFile/";
        logPath=getApplicationContext().getExternalCacheDir()+"/log/";
        initData();
        initView();
        initLongClick();
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_feed_back;
    }

    private void initLongClick() {
        mIvAfb1.setOnLongClickListener(this);
        mIvAfb2.setOnLongClickListener(this);
        mIvAfb3.setOnLongClickListener(this);
        mIvAfb4.setOnLongClickListener(this);
        mIvAfb5.setOnLongClickListener(this);
//        长按点击 后会触发OnTOuchListener 然后onClick所以要提前禁用

        mIvAfb1.setOnTouchListener(null);
        mIvAfb2.setOnTouchListener(null);
        mIvAfb3.setOnTouchListener(null);
        mIvAfb4.setOnTouchListener(null);
        mIvAfb5.setOnTouchListener(null);
    }

    private static final String TAG = "FeedBackActivity";
    private static final int REQUEST_CAMERA_PERMISSION = 1002;
    String cameraPermission = Manifest.permission.CAMERA;

    private void initData() {
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                int maxLenght = 500;
                if (length <= maxLenght) {
//                    合法数据
                    mTvAfbCurrentText.setText(length + "/" + maxLenght);

                } else {
                    mEdAfbProblem.setText(mEdAfbProblem.getText().toString().substring(0, maxLenght));
                    mEdAfbProblem.setSelection(mEdAfbProblem.length());
                    Toast.makeText(getApplicationContext(),"文字被截取,因为文字已经超出最大限制(" + (length - maxLenght) + "个)!",Toast.LENGTH_SHORT).show();
                }
                if (length > 0 && length <= maxLenght) {
                    mBtAfbCommit.setEnabled(true);
                    mBtAfbCommit.setBackground(getResources().getDrawable(R.drawable.shape_login_blue_bg));
                } else {
                    mBtAfbCommit.setEnabled(false);
                    mBtAfbCommit.setBackground(getResources().getDrawable(R.drawable.shape_login_gray_line));
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        mEdAfbProblem.addTextChangedListener(watcher);
    }

    private void initView() {
        mTvHeaderTitle.setText("问题反馈");
        mEtAfbPhoneEmail.setText(cacheUserBean.getMobile());
    }

    private static int maxPic = 5;
    private static int currentPic = 1;
    String[] mPicLocalArray = new String[5];
    private String logCachePath;
    private String logCacheFile;
    @OnClick({R.id.iv_close, R.id.iv_afb_cancel_1, R.id.iv_afb_1, R.id.rl_afb_iv_1, R.id.iv_afb_cancel_2, R.id.iv_afb_2, R.id.rl_afb_iv_2, R.id.iv_afb_cancel_3, R.id.iv_afb_3, R.id.rl_afb_iv_3, R.id.iv_afb_cancel_4, R.id.iv_afb_4, R.id.rl_afb_iv_4, R.id.iv_afb_cancel_5, R.id.iv_afb_5, R.id.rl_afb_iv_5, R.id.ll_afb_phone_email, R.id.bt_afb_commit})
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_close) {
            finish();
        } else if (i == R.id.iv_afb_cancel_1) {
            //图片1 设置为空
            clearPic(1);
        } else if (i == R.id.iv_afb_1) {
            addPic(1);
        } else if (i == R.id.iv_afb_cancel_2) {
            clearPic(2);
        } else if (i == R.id.iv_afb_2) {
            addPic(2);
        } else if (i == R.id.iv_afb_cancel_3) {
            clearPic(3);
        } else if (i == R.id.iv_afb_3) {
            addPic(3);
        } else if (i == R.id.iv_afb_cancel_4) {
            clearPic(4);
        } else if (i == R.id.iv_afb_4) {
            addPic(4);
        } else if (i == R.id.iv_afb_cancel_5) {
            clearPic(5);
        } else if (i == R.id.iv_afb_5) {
            addPic(5);

        } else if (i == R.id.bt_afb_commit) {
            ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            progressDialog.setMessage("上传中...");
            progressDialog.show();
                for (int i1 = 0; i1 < mPicLocalArray.length; i1++) {
                    if (mPicLocalArray[i1] != null)
                        FileUtils.copy(mPicLocalArray[i1], logCacheFile);
                }
                    FileUtils.copyFolder(logPath, logCacheFile+"logs");

                String fileTime = simpleDateFormat.format(new Date()).toString().replace(":", "");
                logZipPath = logCachePath+"this_is_guid_" + fileTime+".zip";
            try {
                FileUtils.zip(logCacheFile, logZipPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            File zipFile= new File(logZipPath);
            if(zipFile.exists()) {
                basePostPresenter.post_file(
                        HttpRequest.uploadlog, zipFile,
                        "token", cacheUserBean.getToken(),
                        "Content-Type", "application/json"
                );
            }
            else
            {
                progressDialog.dismiss();
                ToastUtil.showToast(getApplicationContext(),"没有对应的log文件");
            }
            }




    }

    private void addPic(int i) {

        currentPic = i;
        changePic(FLAG_CHOOSE_IMG);
    }

    // 获取从adbox_1到adbox_64的图片
    private ImageView getPicId(int i) {
        switch (i) {
            case 0:
                return mIvAfb1;
            case 1:
                return mIvAfb2;
            case 2:
                return mIvAfb3;
            case 3:
                return mIvAfb4;
            case 4:
                return mIvAfb5;
        }
        return mIvAfb1;
    }

    private void clearPic(int i) {
        currentPic = i;
        //判断有效位数
        int effective = 0;

        for (int i2 = 0; i2 < mPicLocalArray.length; i2++) {
            if (!TextUtils.isEmpty(mPicLocalArray[i2])) {
                effective += 1;
            }
        }
        //判断这个
        for (int i1 = 0; i1 < mPicLocalArray.length; i1++) {
            //整体往前偏移1  删除掉第一个
            if (i1 >= (i - 1)) {
//                前面数组不用动。后面的向前+1 最后的一个数组设置为空
                //最后一个数据会数组越界
                if (i1 != (mPicLocalArray.length - 1)) {
                    mPicLocalArray[i1] = mPicLocalArray[i1 + 1];
                }
            }

        }
        mPicLocalArray[effective - 1] = "";

        if (effective == 5) {
            Glide.with(this).load(R.mipmap.image_empty).into(mIvAfb5);
            mIvAfbCancel5.setVisibility(View.GONE);
        } else if (effective == 4) {
            Glide.with(this).load(R.mipmap.image_empty).into(mIvAfb4);
            mIvAfbCancel4.setVisibility(View.GONE);
            mRlAfbIv5.setVisibility(View.INVISIBLE);
        } else if (effective == 3) {
            Glide.with(this).load(R.mipmap.image_empty).into(mIvAfb3);
            mIvAfbCancel3.setVisibility(View.GONE);
            mRlAfbIv4.setVisibility(View.INVISIBLE);
        } else if (effective == 2) {
            Glide.with(this).load(R.mipmap.image_empty).into(mIvAfb2);
            mIvAfbCancel2.setVisibility(View.GONE);
            mRlAfbIv3.setVisibility(View.INVISIBLE);
        } else if (effective == 1) {
            Glide.with(this).load(R.mipmap.image_empty).into(mIvAfb1);
            mIvAfbCancel1.setVisibility(View.GONE);
            mRlAfbIv2.setVisibility(View.INVISIBLE);
        }
        //重新设置图片
        for (int i1 = 0; i1 < mPicLocalArray.length; i1++) {
            if (!TextUtils.isEmpty(mPicLocalArray[i1])) {
                Glide.with(this).load(mPicLocalArray[i1]).into(getPicId(i1));

            }
        }


    }

    private void changePic(int REQUEST_CAMERA_PERMISSION) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, cameraPermission) == PermissionChecker.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{cameraPermission}, REQUEST_CAMERA_PERMISSION);
                return;
            } else {
                showSelectPictureMenu(this);
            }

        } else {
            showSelectPictureMenu(this);
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10001:
                    //图片更新成功
                    Bundle data = msg.getData();
                    progressDialog.dismiss();
                    mEdAfbProblem.setText("");
                    for (int i = 0; i < mPicLocalArray.length; i++) {
                        if (!TextUtils.isEmpty(mPicLocalArray[i])) {
                            clearPic(i);
                        }
                    }

                    new AlertDialog.Builder(FeedBackActivity.this).setTitle("您的建议已提交，感谢您的反馈")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }
                            ).show();


                    break;

                case 10002:
                    //图片更新成功
                  String message = (String) msg.obj;
                    ToastUtil.showToast(getApplicationContext(),"上传失败："+message);
                    progressDialog.dismiss();
                    break;

            }
        }
    };

    /**
     * 弹出选择照片菜单
     */

    /**
     * 本地图片选取标志
     */
    private static final int FLAG_CHOOSE_IMG = 2;


    public void showSelectPictureMenu(Activity activity) {
        Cemera.startAlbum(activity, FLAG_CHOOSE_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
            String picPath="";
            if (data != null) {
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    Cursor cursor = getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null, null, null);
                    if (null == cursor) {
                        Toast.makeText(getApplicationContext(), "图片没找到", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    cursor.moveToFirst();
                    String path = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    cursor.close();
//                    intent.putExtra("fromTag", "project");
                    picPath = path;
                } else {
                    picPath = uri.getPath();
                }

                if (!TextUtils.isEmpty(picPath)) {
                    Bitmap b = BitmapFactory.decodeFile(picPath);
                    mPicLocalArray[currentPic - 1] = picPath;
                  //  UploadImageUtils.uploadHeadImage(picPath, b, "", oss, this, "详情图上传", mHandler);
                    changeStatus(new File(picPath));
                } else {
                    ToastUtil.showToast(getApplicationContext(),"图片选择失败");
                }

            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:

                if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    showSelectPictureMenu(this);
                } else {
                   // ToastUtils.showLong(getString(R.string.add_detail_premission));
                }
                break;

        }
    }

    private void changeStatus(File picPath) {

        switch (currentPic) {
            case 1:
                if (mIvAfb1.getVisibility() != View.VISIBLE) {
                    mIvAfb1.setVisibility(View.VISIBLE);
                }
                if (mIvAfbCancel1.getVisibility() != View.VISIBLE) {
                    mIvAfbCancel1.setVisibility(View.VISIBLE);
                }
                if (mRlAfbIv2.getVisibility() != View.VISIBLE) {
                    mRlAfbIv2.setVisibility(View.VISIBLE);
                }
                Glide.with(this).load(picPath).into(mIvAfb1);
                break;
            case 2:
                if (mIvAfb2.getVisibility() != View.VISIBLE) {
                    mIvAfb2.setVisibility(View.VISIBLE);
                }
                if (mIvAfbCancel2.getVisibility() != View.VISIBLE) {
                    mIvAfbCancel2.setVisibility(View.VISIBLE);
                }
                if (mRlAfbIv3.getVisibility() != View.VISIBLE) {
                    mRlAfbIv3.setVisibility(View.VISIBLE);
                }
                Glide.with(this).load(picPath).into(mIvAfb2);
                break;
            case 3:
                if (mIvAfb3.getVisibility() != View.VISIBLE) {
                    mIvAfb3.setVisibility(View.VISIBLE);
                }
                if (mIvAfbCancel3.getVisibility() != View.VISIBLE) {
                    mIvAfbCancel3.setVisibility(View.VISIBLE);
                }
                if (mRlAfbIv4.getVisibility() != View.VISIBLE) {
                    mRlAfbIv4.setVisibility(View.VISIBLE);
                }
                Glide.with(this).load(picPath).into(mIvAfb3);
                break;
            case 4:
                if (mIvAfb4.getVisibility() != View.VISIBLE) {
                    mIvAfb4.setVisibility(View.VISIBLE);
                }
                if (mIvAfbCancel4.getVisibility() != View.VISIBLE) {
                    mIvAfbCancel4.setVisibility(View.VISIBLE);
                }
                if (mRlAfbIv5.getVisibility() != View.VISIBLE) {
                    mRlAfbIv5.setVisibility(View.VISIBLE);
                }
                Glide.with(this).load(picPath).into(mIvAfb4);
                break;
            case 5:
                if (mIvAfb5.getVisibility() != View.VISIBLE) {
                    mIvAfb5.setVisibility(View.VISIBLE);
                }
                if (mIvAfbCancel5.getVisibility() != View.VISIBLE) {
                    mIvAfbCancel5.setVisibility(View.VISIBLE);
                }

                Glide.with(this).load(picPath).into(mIvAfb5);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        int i = v.getId();

        if (i == R.id.iv_afb_1) {
            clearPic(1);
            return true;
        }
        if (i == R.id.iv_afb_2) {
            clearPic(2);
            return true;
        }
        if (i == R.id.iv_afb_3) {
            clearPic(3);
            return true;
        }
        if (i == R.id.iv_afb_4) {
            clearPic(4);
            return true;
        }
        if (i == R.id.iv_afb_5) {
            clearPic(5);
            return true;
        }
        return false;

    }


    @Override
    public com.alibaba.fastjson.JSONObject getHttpRequestParams(String moduleName) {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        if (HttpRequest.uploadlog.equals(moduleName)) {
            UploadlogBean requestBean = new UploadlogBean();
            requestBean.setAppVersion( PackageUtils.getVersionName(this));
            requestBean.setMemo(mEdAfbProblem.getText().toString().trim());
            requestBean.setOs("Android");
            requestBean.setOsVersion( android.os.Build.VERSION.RELEASE);
            requestBean.setUserAccount(cacheUserBean.getUsername());
            requestBean.setUserEmail(cacheUserBean.getEmail());
            requestBean.setUserId(cacheUserBean.getUserId());
            requestBean.setUserName(cacheUserBean.getUsername());
            requestBean.setUserPhone(cacheUserBean.getMobile());
            requestBean.setUserAgent(android.os.Build.MODEL);
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        }
        return jsonObject;
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
      FileUtils.deleteDirectory(logCachePath);
        if(result == null) return;
        if (HttpRequest.uploadlog.equals(moduleName)) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            message.what = 10002;
            message.obj=result.getMessage();
            message.setData(bundle);
            if (mHandler != null) {
                mHandler.handleMessage(message);
            }
        }
    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
     LogUtils.i("lxl", "请求数据 " + response);
        if (HttpRequest.uploadlog.equals(moduleName)) {
            FileUtils.deleteDirectory(logCachePath);
            Message message = new Message();
            Bundle bundle = new Bundle();
            message.what = 10001;
            message.setData(bundle);
            if (mHandler != null) {
                mHandler.handleMessage(message);
            }

        }
    }
}

