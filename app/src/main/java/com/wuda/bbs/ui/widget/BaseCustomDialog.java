package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.wuda.bbs.R;

public abstract class BaseCustomDialog extends AppCompatDialog {
    protected TextView title_tv;
    protected FrameLayout content_fl;
    protected Button negative_btn, positive_btn;

    protected String title;
    protected View content_view;
    protected String pBtnText, nBtnText;

    protected OnButtonClickListener pBtnLister;
    protected OnButtonClickListener nBtnLister;


    public BaseCustomDialog(Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        setCanceledOnTouchOutside(false);

        title_tv = findViewById(R.id.dialog_title_textView);
        content_fl = findViewById(R.id.dialog_content_frameLayout);
        negative_btn = findViewById(R.id.dialog_negtive_button);
        positive_btn = findViewById(R.id.dialog_positive_button);
    }

    protected void refreshView() {

        if (!TextUtils.isEmpty(title)) {
            title_tv.setText(title);
            title_tv.setVisibility(View.VISIBLE);
        }else {
            title_tv.setVisibility(View.GONE);
        }

        if (content_view != null) {
            content_fl.addView(content_view);
        }

        //如果设置按钮的文字
        if (!TextUtils.isEmpty(pBtnText)) {
            positive_btn.setText(pBtnText);
        }else {
            positive_btn.setText("确定");
        }
        if (!TextUtils.isEmpty(nBtnText)) {
            negative_btn.setText(nBtnText);
        }else {
            negative_btn.setText("取消");
        }

        if (nBtnLister != null) {
            negative_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nBtnLister.onButtonClick();
                }
            });
        } else {
            negative_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        content_view.invalidate();
    }

    public BaseCustomDialog setOnPositiveButtonClickedListener(String text, OnButtonClickListener listener) {
        this.pBtnText = text;
        if (listener != null)
            this.pBtnLister = listener;
        return this;
    }

    public BaseCustomDialog setOnPositiveButtonClickedListener(OnButtonClickListener listener) {
        if (listener != null)
            this.pBtnLister = listener;
        return this;
    }

    public BaseCustomDialog setOnNegativeButtonClickedLister(String text, OnButtonClickListener listener) {
        this.nBtnText = text;
        if (listener != null)
            this.nBtnLister = listener;
        return this;
    }

    public BaseCustomDialog setOnNegativeButtonClickedLister(OnButtonClickListener listener) {
        if (listener != null)
            this.nBtnLister = listener;
        return this;
    }

    public interface OnButtonClickListener {
        public void onButtonClick();
    }
}
