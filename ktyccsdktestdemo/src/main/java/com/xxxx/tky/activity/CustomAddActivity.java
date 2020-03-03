package com.xxxx.tky.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.google.gson.Gson;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.dialog.BusinessDialog;
import com.xxxx.cc.dialog.DialogClickListener;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CustomDefinedBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.parse.CustomItemParse;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;


public class CustomAddActivity extends BaseHttpRequestActivity {
    private UserBean cacheUserBean;
    private List<CustomDefinedBean> customDefinedBeans = new ArrayList<>();
    @BindView(R.id.layout_defined_message)
    LinearLayout definedLayout;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.phone_num)
    EditText phoneNum;
    @BindView(R.id.save_btn)
    Button saveBtn;


    public final static String TYPE_YMDHDS = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";
    public final static String TYPE_YMD = "^[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$";
    public final static String TYPE_HDS = "^(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d$";

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_add_custom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        getDefinedMessage();

    }

    private void getDefinedMessage() {
        baseGetPresenter.presenterBusinessByHeader(HttpRequest.Contant.selfdefinedContacts, false, "token", cacheUserBean.getToken());

    }

    private void addCustom() {
        basePostPresenter.presenterBusinessByHeader(HttpRequest.Contant.addCustom, false, "token", cacheUserBean.getToken());
    }

    @OnClick(R.id.save_btn)
    public void saveClick(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        addCustom();
    }

    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }

    @Override
    public com.alibaba.fastjson.JSONObject getHttpRequestParams(String moduleName) {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        try {

            if ((HttpRequest.Contant.addCustom).equals(moduleName)) {
                jsonObject.put("ownerName", cacheUserBean.getUsername());
                jsonObject.put("name", userName.getText().toString());
                jsonObject.put("mobile", phoneNum.getText().toString());
                jsonObject.put("shares", "no");
                if (null != customDefinedBeans && customDefinedBeans.size() > 0) {
                    for (int i = 0; i < customDefinedBeans.size(); i++) {
                        CustomDefinedBean bean = customDefinedBeans.get(i);
                        if ("number".equals(bean.getType())) {
                            if (TextUtils.isEmpty(bean.getValue())) {
                                jsonObject.put(bean.getField(), 0);
                            } else {
                                jsonObject.put(bean.getField(), Long.parseLong(bean.getValue()));
                            }

                        } else if ("time".equals(bean.getType())) {
                            if (TextUtils.isEmpty(bean.getValue())) {
                                jsonObject.put(bean.getField(), 0);
                            } else {
                                jsonObject.put(bean.getField(), TimeUtils.getSecondFromTime(bean.getValue()));
                            }

                        } else {
                            jsonObject.put(bean.getField(), bean.getValue());
                        }

                    }
                }
            }
            LogUtils.i("zwmn", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        super.dealHttpRequestResult(moduleName, result, response);
//        LogUtils.i("zwmn", moduleName + " >> " + result + " >> " + response);
        if (HttpRequest.Contant.selfdefinedContacts.equals(moduleName)) {
            if (result.isOk() && !TextUtils.isEmpty(response)) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray dataList = json.optJSONArray("data");
                    if (null != dataList && dataList.length() > 0) {
                        customDefinedBeans.clear();
                        for (int i = 0; i < dataList.length(); i++) {
                            JSONObject item = dataList.getJSONObject(i);
                            if (item.optBoolean("showInPage")) {
                                CustomDefinedBean bean = (new Gson()).fromJson(item.toString(), CustomDefinedBean.class);
                                customDefinedBeans.add(bean);
                            }
                        }
                        setCustomDefinedView();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else if (HttpRequest.Contant.addCustom.equals(moduleName)) {
            if (result.isOk()) {
                ToastUtil.showToast(this, "添加客户成功");
                setResult(RESULT_OK);
                finish();

            } else {
                ToastUtil.showToast(this, result.getMessage());
            }


        }
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        LogUtils.i("zwmn", moduleName + " >> " + result + " >> ");

    }

    private void setCustomDefinedView() {
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
        LogUtils.i("zwmn", bean.toString());
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
        dialog.setYearLimt(5);
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
                LogUtils.i("zwmn", "所选日期时间：" + text);
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
                LogUtils.i("zwmn", "所选日期时间：" + text);
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
                LogUtils.i("zwmn", "所选日期时间：" + text);
                textView.setText(text);
                bean.setValue(text);

            }
        });
        dialog.show();

    }
}
