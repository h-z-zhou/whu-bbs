package com.wuda.bbs.ui.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.effective.android.panel.PanelSwitchHelper;
import com.wuda.bbs.R;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelFrameLayout;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

public class NewArticleActivity extends AppCompatActivity {
    private PanelSwitchHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_article);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mHelper == null) {
            mHelper = new PanelSwitchHelper.Builder(this).build();
        }
    }


    @Override
    public void onBackPressed() {
        //用户按下返回键的时候，如果显示面板，则需要隐藏
//        if (mHelper != null && mHelper.hookSystemBackForHindPanel()) {
//            return;
//        }
        if (mHelper != null && mHelper.hookSystemBackByPanelSwitcher()) {
            return;
        }
        super.onBackPressed();
    }
}