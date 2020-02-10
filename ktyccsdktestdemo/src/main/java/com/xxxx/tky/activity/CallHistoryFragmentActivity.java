package com.xxxx.tky.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.gyf.barlibrary.ImmersionBar;
import com.xxxx.cc.ui.fragment.HistoryFragment;
import com.xxxx.tky.R;

/**
 * @author zhoufeng
 * @date 2019/8/12
 * @moduleName
 */
public class CallHistoryFragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fragment);

        ImmersionBar.with(this).statusBarDarkFont(true).barColor(com.xxxx.cc.R.color.white).fitsSystemWindows(true)
                .init();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = manager.beginTransaction();
        FragmentTransaction transaction = beginTransaction.replace(R.id.fragment_container,
                new HistoryFragment());
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}
