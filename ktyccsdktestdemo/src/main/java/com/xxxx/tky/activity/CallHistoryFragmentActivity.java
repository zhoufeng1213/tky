package com.xxxx.tky.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.kty.mars.baselibrary.base.BaseActivity;
import com.xxxx.tky.R;
import com.xxxx.tky.fragment.HistoryFragment;

/**
 * @author zhoufeng
 * @date 2019/8/12
 * @moduleName
 */
public class CallHistoryFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fragment);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = manager.beginTransaction();
        FragmentTransaction transaction = beginTransaction.replace(R.id.fragment_container,
                new HistoryFragment());
        transaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
