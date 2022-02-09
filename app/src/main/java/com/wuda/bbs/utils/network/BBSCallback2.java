package com.wuda.bbs.utils.network;

import android.content.Context;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BBSCallback2 implements Callback<ResponseBody> {
    Context mContext;
    OnLogoutHandler logoutHandler;
    CallbackWithoutLogout callbackWithoutLogout;

    private BBSCallback2() {

    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (mContext == null)
            return;
        String cookies = response.headers().get("Set-Cookie");
        if (cookies != null && cookies.contains("UTMPUSERID=guest")) {
            logoutHandler.onLogout();
        } else {
            if (callbackWithoutLogout != null) {
                callbackWithoutLogout.onResponse(call, response);
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }

    public abstract static class OnLogoutHandler {
        public void onLogout() {

        }
        public void onReLogin() {

        }
    }

    public interface CallbackWithoutLogout {
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response);
        public void onFailure(Call<ResponseBody> call, Throwable t);
    }

    public static class Builder {
        Context mContext;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }


    }


}
