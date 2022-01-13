package com.wuda.bbs.utils.network;

import android.app.AlertDialog;

import androidx.annotation.NonNull;

import com.wuda.bbs.application.BBSApplication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BBSCallback implements Callback<ResponseBody> {
    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) {
        String cookies = response.headers().get("Set-Cookie");
        if (cookies != null && cookies.contains("UTMPUSERID=guest")) {
            new AlertDialog.Builder(BBSApplication.getAppContext())
                    .setTitle("未登录")
                    .setMessage("请先登录")
                    .create()
                    .show();
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull Throwable t) {

    }
}
