package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.ui.account.AccountActivity;

public class ResponseErrorHandlerDialog extends BaseCustomDialog {

    private final Context mContext;
    private ResultCode resultCode;
    private final TextView massage_tv;
    private OnButtonClickListener mRetryListener;


    public ResponseErrorHandlerDialog(Context context) {
        super(context);
        this.mContext = context;
        massage_tv = new TextView(getContext());
        content_view = massage_tv;
    }

    public ResponseErrorHandlerDialog addErrorMsg(ResultCode resultCode, String massage) {
        this.resultCode = resultCode;

        String msg = massage!=null? massage: resultCode.getMsg();
        this.massage_tv.setText(msg);

        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        this.title = "出错啦";

        this.nBtnText = "退出";
        this.negative_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public ResponseErrorHandlerDialog setOnRetryButtonClickedListener(OnButtonClickListener listener) {
        this.mRetryListener = listener;
        return this;
    }

    @Override
    public void show() {
        super.show();

        if (resultCode != ResultCode.LOGIN_ERR) {
            this.pBtnText = "重试";
            this.positive_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRetryListener.onButtonClick();
                    dismiss();
                }
            });
        } else {
            this.pBtnText = "去登录";
            this.positive_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginIntent = new Intent(getContext(), AccountActivity.class);
                    loginIntent.putExtra("isLogin", true);
                    mContext.startActivity(loginIntent);
                    content_fl.removeAllViews();
                    pBtnText = "重试";
                    positive_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mRetryListener == null)
                                return;
                            mRetryListener.onButtonClick();
                            dismiss();
                        }
                    });
                    refreshView();
                }
            });
        }

        refreshView();
    }
}
