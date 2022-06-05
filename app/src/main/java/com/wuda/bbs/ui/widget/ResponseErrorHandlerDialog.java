package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuda.bbs.logic.bean.response.ContentResponse;
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

    public ResponseErrorHandlerDialog addErrorResponse(ContentResponse<?> response) {

        this.resultCode = response.getResultCode();

        String msg;
        if (response.getMassage() != null) {
            msg = response.getMassage();
        } else if (response.getException() != null) {
            msg = response.getResultCode().getMsg() + response.getException().getMessage();
        } else {
            msg = response.getResultCode().getMsg();
        }
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

        switch (resultCode) {
            case PERMISSION_DENIED:
                this.positive_btn.setVisibility(View.GONE);
                break;
            case NO_LOGIN_ERR:
                this.pBtnText = "去登录";
                this.positive_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent loginIntent = new Intent(getContext(), AccountActivity.class);
                        mContext.startActivity(loginIntent);
                        pBtnText = "重试";
                        positive_btn.setText(pBtnText);
                        positive_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mRetryListener == null)
                                    return;
                                mRetryListener.onButtonClick();
                                dismiss();
                            }
                        });
                    }
                });
                break;
            default:
                this.pBtnText = "重试";
                this.positive_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRetryListener.onButtonClick();
                        dismiss();
                    }
                });
        }

        refreshView();
    }
}
