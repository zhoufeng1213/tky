package com.xxxx.cc.base.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xxxx.cc.base.widget.LoadingDialog;

import butterknife.ButterKnife;

/**
 * @author zhoufeng
 * @date 2019/8/12
 * @moduleName
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 依附的activity
     */
    protected FragmentActivity mActivity;
    /**
     * 根view
     */
    protected View mRootView;
    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getContentViewId() != 0) {
            if (mRootView != null) {
                ViewGroup parent = (ViewGroup) mRootView.getParent();
                if (parent != null) {
                    parent.removeView(mRootView);
                }
            } else {
                mRootView = inflater.inflate(getContentViewId(), container, false);
                ButterKnife.bind(this,mRootView);
            }
        } else {
            mRootView = super.onCreateView(inflater, container, savedInstanceState);
        }
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    /**
     * 设置根布局资源id
     */
    public abstract int getContentViewId();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //    @Override
//    public void setMenuVisibility(boolean menuVisible) {
//        super.setMenuVisibility(menuVisible);
//        if (this.getView() != null) {
//            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
//        }
//    }


    private LoadingDialog loadingDialog;

    public void showDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
        }
        loadingDialog.setMessage(true);
        loadingDialog.show();
    }

    public void showDialog(String title) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
        }
        loadingDialog.setMessage(title);
        loadingDialog.show();
    }

    public void showDialog(String title, boolean isCancel) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext, isCancel);
        }
        loadingDialog.setMessage(title);
        loadingDialog.show();
    }

    public void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void showToast(String msg) {
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
//        ToastUtil.showToast(mContext, msg);
    }


    public void startActivity(Class className,String ...params){
        Intent intent = new Intent(mContext,className);
        if(params!=null) {
            if (params.length>1&&params.length%2==0){
                for (int i = 0; i <params.length ; i+=2) {
                    intent.putExtra(params[i],params[i+1]);
                }
            }
        }
        startActivity(intent);
    }



}

