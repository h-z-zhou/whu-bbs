package com.wuda.bbs.utils.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.wuda.bbs.ui.account.AccountActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BBSCallback<T> implements Callback<T> {
    Context mContext;
    LogoutHandler logoutHandler;

    public BBSCallback(Context context) {
        mContext = context;
        // default handler
        logoutHandler = new LogoutHandler() {
            @Override
            public void onLogout() {
                new AlertDialog.Builder(mContext)
                        .setTitle("未登录")
                        .setMessage("请先登录")
                        .setPositiveButton("去登录", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(mContext, AccountActivity.class);
                                intent.putExtra("isLogin", true);
                                mContext.startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        };
    }

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        if (mContext == null)
            return;
        String cookies = response.headers().get("Set-Cookie");
        if (cookies != null && cookies.contains("UTMPUSERID=guest")) {
            logoutHandler.onLogout();
        } else {
            onResponseWithoutLogout(call, response);
        }
    }

    public abstract void onResponseWithoutLogout(@NonNull Call<T> call, @NonNull Response<T> response);

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {

    }

    public void setLogoutHandler(LogoutHandler logoutHandler) {
        this.logoutHandler = logoutHandler;
    }

    public interface LogoutHandler {
        public void onLogout();
    }
}
