package com.xxxx.tky.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.fly.sweet.dialog.SweetAlertDialog;
import com.google.gson.Gson;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.dialog.BusinessDialog;
import com.xxxx.cc.dialog.DialogClickListener;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CustomDefinedBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.parse.CustomItemParse;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.util.TextUtil;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/2/7
 * @moduleName
 */
public class CustomPersonDetailToDetailActivity extends BaseHttpRequestActivity {


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.delete_btn)
    ImageView deleteBtn;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.name_detail_image)
    ImageView nameDetailImage;
    @BindView(R.id.phone_num)
    EditText phoneNum;
    @BindView(R.id.phone_detail_image)
    ImageView phoneDetailImage;
    //    @BindView(R.id.user_beizhu)
//    EditText userBeizhu;
    @BindView(R.id.layout_defined_message)
    LinearLayout definedLayout;
    private QueryCustomPersonBean queryCustomPersonBean;
    private UserBean cacheUserBean;
    private ArrayList<CustomDefinedBean> customDefinedBeans;
    private Activity mActivity;
    public final static String TYPE_YMDHDS = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
    public final static String TYPE_YMD = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
    public final static String TYPE_HDS = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";


    @Override
    public int getLayoutViewId() {
        return R.layout.activity_custom_person_detail_to_detail_new;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle.setText("客户详情");
        mActivity = this;
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }

            String data = getIntent().getStringExtra("data");
            if (!TextUtils.isEmpty(data)) {

                queryCustomPersonBean = JSON.parseObject(data, QueryCustomPersonBean.class);
                if (queryCustomPersonBean != null) {
                    if (!TextUtils.isEmpty(queryCustomPersonBean.getName())) {
                        userName.setText(queryCustomPersonBean.getName());
                        userName.setSelection(queryCustomPersonBean.getName().length());
                    }
                    phoneNum.setText(queryCustomPersonBean.getRealMobileNumber());
                }
            }

            Object definedDatas = getIntent().getSerializableExtra("definedData");
            if (null != definedDatas) {
                customDefinedBeans = (ArrayList<CustomDefinedBean>) definedDatas;
                setDefinedView();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        getCustomDetail();


    }

    private void getCustomDetail() {
        basePostPresenter.presenterBusinessByHeader(
                HttpRequest.Contant.getOneCustom + queryCustomPersonBean.getId(),
                "token", cacheUserBean.getToken());

    }


    private void setDefinedView() {
        if (customDefinedBeans != null && customDefinedBeans.size() > 0) {
            definedLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < customDefinedBeans.size(); i++) {
                CustomDefinedBean bean = customDefinedBeans.get(i);
                View itemView = LayoutInflater.from(this).inflate(R.layout.view_edit_message_layout, null, false);
                TextView tvName = itemView.findViewById(R.id.tv_defined_name);
                EditText etValue = itemView.findViewById(R.id.et_defined_value);
                TextView tvValue = itemView.findViewById(R.id.tv_defined_value);
                ImageView ivArrow = itemView.findViewById(R.id.iv_right_arrow);
                boolean isEdit = bean.isSupportEdit();
                if (isEdit) {
                    ivArrow.setVisibility(View.VISIBLE);
                } else {
                    ivArrow.setVisibility(View.GONE);
                }
                if ("cdate".equals(bean.getType()) || "time".equals(bean.getType()) || "datetime".equals(bean.getType()) || "select".equals(bean.getType()) || "radio".equals(bean.getType())) {
                    tvValue.setVisibility(View.VISIBLE);
                    etValue.setVisibility(View.GONE);
                    ivArrow.setVisibility(View.VISIBLE);
                } else {
                    tvValue.setVisibility(View.GONE);
                    etValue.setVisibility(View.VISIBLE);
                    ivArrow.setVisibility(View.GONE);
                    if (isEdit) {
                        etValue.setEnabled(true);
                        if ("number".equals(bean.getType())) {
                            etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else if ("phonenumber".equals(bean.getType())) {
                            etValue.setInputType(InputType.TYPE_CLASS_PHONE);
                        }

                        etValue.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                if (null != editable && editable.length() > 0) {
                                    bean.setValue(editable.toString());
                                } else {
                                    bean.setValue("");
                                }

                            }
                        });
                    } else {
                        etValue.setEnabled(false);
                    }

                }


                tvName.setText(bean.getName());
                String value = bean.getValue();
                //cdate "2018-11-21 00:00:00" 需要特殊处理:值展示日期
                if ("cdate".equals(bean.getType())) {
                    if (!TextUtils.isEmpty(value)) {
                        value = value.split(" ")[0];
                    }
                }
                tvValue.setText(value);
                etValue.setText(value);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isEdit) {
                            setItemClick(bean, tvValue);
                        }

                    }
                });
                definedLayout.addView(itemView);
            }

        } else {
            definedLayout.setVisibility(View.GONE);
        }

    }

    private void setItemClick(CustomDefinedBean bean, TextView textView) {
//        ToastUtil.showToast(this, bean.getType());
//        LogUtils.i("zwmn", bean.toString());
        if ("select".equals(bean.getType()) || "radio".equals(bean.getType())) {
            JSONArray items = CustomItemParse.parseItem(getApplicationContext(), bean.getId());
            if (null != items && items.length() > 0) {
                BusinessDialog.showItemDialog(this, bean.getType(), bean.getValue(), items, new DialogClickListener() {
                    @Override
                    public void onCommitClick(Object object) {

                    }

                    @Override
                    public void onCancelClick(Object object) {

                    }

                    @Override
                    public void onSingleItemSelected(Object object) {
                        try {
                            org.json.JSONObject jsonObject = (org.json.JSONObject) object;
                            String value = jsonObject.optString("name");
                            bean.setValue(value);
                            textView.setText(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

        } else if ("datetime".equals(bean.getType())) {
            showDateTimePick(bean, textView);

        } else if ("cdate".equals(bean.getType())) {
            showDatePick(bean, textView);
        } else if ("time".equals(bean.getType())) {
            showTimePick(bean, textView);
        }


    }

    private void showDateTimePick(CustomDefinedBean bean, TextView textView) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(10);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMDHMS);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd HH:mm:ss");
        String value = bean.getValue();
        if (!TextUtils.isEmpty(value) && value.matches(TYPE_YMDHDS)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = format.parse(value);
                dialog.setStartDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String text = format.format(date);
//                LogUtils.i("zwmn", "所选日期时间：" + text);
                textView.setText(text);
                bean.setValue(text);

            }
        });
        dialog.show();
    }

    private void showTimePick(CustomDefinedBean bean, TextView textView) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(10);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_HMS);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("HH:mm:ss");
        String value = bean.getValue();
        if (!TextUtils.isEmpty(value) && value.matches(TYPE_HDS)) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            try {
                Date date = format.parse(value);
                dialog.setStartDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String text = format.format(date);
//                LogUtils.i("zwmn", "所选日期时间：" + text);
                textView.setText(text);
                bean.setValue(text);

            }
        });
        dialog.show();

    }

    private void showDatePick(CustomDefinedBean bean, TextView textView) {
        DatePickDialog dialog = new DatePickDialog(this);
        //设置上下年分限制
        dialog.setYearLimt(10);
        //设置标题
        dialog.setTitle("选择时间");
        //设置类型
        dialog.setType(DateType.TYPE_YMD);
        //设置消息体的显示格式，日期格式
        dialog.setMessageFormat("yyyy-MM-dd");
        String value = bean.getValue();
        if (!TextUtils.isEmpty(value) && value.matches(TYPE_YMDHDS)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(value);
                dialog.setStartDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //设置选择回调
        dialog.setOnChangeLisener(null);
        //设置点击确定按钮回调
        dialog.setOnSureLisener(new OnSureLisener() {
            @Override
            public void onSure(Date date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String text = format.format(date);
//                LogUtils.i("zwmn", "所选日期时间：" + text);
                textView.setText(text);
                bean.setValue(text);

            }
        });
        dialog.show();

    }


    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }

    @OnClick(R.id.name_layout)
    public void fourceName(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        userName.setFocusable(true);
        userName.setFocusableInTouchMode(true);
        userName.postDelayed(new Runnable() {
            @Override
            public void run() {
                userName.requestFocus();
                InputMethodManager manager = ((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null) {
                    manager.showSoftInput(userName, 0);
                }
            }
        }, 100);
    }


    @OnClick(R.id.save_btn)
    public void save(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        if (cacheUserBean != null && queryCustomPersonBean != null) {
            basePostPresenter.presenterBusinessByHeader(
                    HttpRequest.Contant.updateAll + queryCustomPersonBean.getId(),
                    "token", cacheUserBean.getToken());

        }

    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if ((HttpRequest.Contant.updateAll + queryCustomPersonBean.getId()).equals(moduleName)) {
            if (result.isOk()) {
                showToast("修改成功:");
                //把本地的数据库更新一下
                queryCustomPersonBean.setName(userName.getText().toString().trim());
                queryCustomPersonBean.setPhone(phoneNum.getText().toString().trim());
                queryCustomPersonBean.setLetters(TextUtil.getNameFirstChar(userName.getText().toString().trim()));
                queryCustomPersonBean.setDisplayNameSpelling(TextUtil.getNameToPinyin(
                        userName.getText().toString().trim()));
                //需要将修改的内容更新到queryCustomPersonBean 并存数据库
                //先将queryCustomPersonBean转成JSONObject，
                JSONObject oldJson = JSONObject.parseObject((new Gson()).toJson(queryCustomPersonBean));
                //将customDefinedBeans的内容修改到oldJson
                if (null != customDefinedBeans && customDefinedBeans.size() > 0) {
                    for (int i = 0; i < customDefinedBeans.size(); i++) {
                        CustomDefinedBean bean = customDefinedBeans.get(i);
                        if ("time".equals(bean.getType())) {
                            oldJson.put(bean.getField(), TimeUtils.getSecondFromTime(bean.getValue()));
                        } else {
                            oldJson.put(bean.getField(), bean.getValue());
                        }
                    }

                }
                //转成queryCustomPersonBean 存数据库
                queryCustomPersonBean = (new Gson()).fromJson(oldJson.toString(), QueryCustomPersonBean.class);
                DbUtil.updateQueryCustomPersonBeanById(queryCustomPersonBean);
                finish();
            } else {
                showToast("修改失败");
            }
        } else if ((HttpRequest.Contant.back + queryCustomPersonBean.getId()).equals(moduleName)) {
            if (result.isOk()) {
                showToast("放弃成功");
                //把本地的数据库更新一下
                queryCustomPersonBean.setDatastatus(false);
                DbUtil.updateQueryCustomPersonBeanById(queryCustomPersonBean);
                finish();
            } else {
                showToast("放弃失败");
            }
        } else if ((HttpRequest.Contant.getOneCustom + queryCustomPersonBean.getId()).equals(moduleName)) {
            if (result.isOk()) {
//                LogUtils.i("zwmn", "获取用户信息：" + response);

            } else {
//                LogUtils.i("zwmn", "获取用户信息请求失败");
            }

        }
    }

    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if ((HttpRequest.Contant.updateAll + queryCustomPersonBean.getId()).equals(moduleName)) {
            jsonObject.put("name", userName.getText().toString());
            jsonObject.put("mobile", phoneNum.getText().toString());
            if (null != customDefinedBeans && customDefinedBeans.size() > 0) {
                for (int i = 0; i < customDefinedBeans.size(); i++) {
                    CustomDefinedBean bean = customDefinedBeans.get(i);
                    if ("number".equals(bean.getType())) {
                        jsonObject.put(bean.getField(), Long.parseLong(bean.getValue()));
                    } else if ("time".equals(bean.getType())) {
                        jsonObject.put(bean.getField(), TimeUtils.getSecondFromTime(bean.getValue()));
                    } else {
                        jsonObject.put(bean.getField(), bean.getValue());
                    }

                }
            }
        }
//        LogUtils.i("zwmn", jsonObject.toString());
        return jsonObject;
    }


    @OnClick(R.id.delete_btn)
    public void fangQi(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        //掉http，然后修改本地数据库
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("是否放弃该客户?")
                .setConfirmText("是")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        if (cacheUserBean != null && queryCustomPersonBean != null) {
                            basePostPresenter.presenterBusinessByHeader(
                                    HttpRequest.Contant.back + queryCustomPersonBean.getId(),
                                    "token", cacheUserBean.getToken());

                        }
                    }
                })
                .setCancelText("否")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }
}
