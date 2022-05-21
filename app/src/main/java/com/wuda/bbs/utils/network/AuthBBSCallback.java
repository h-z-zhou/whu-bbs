package com.wuda.bbs.utils.network;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.networkResponseHandler.ContentResponseHandler;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AuthBBSCallback<T> implements Callback<ResponseBody> {

    ContentResponseHandler<T> mResponseHandler;
    ContentResponse<T> mContentResponse;

    public AuthBBSCallback(ContentResponseHandler<T> mResponseHandler) {
        this.mResponseHandler = mResponseHandler;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
        // 未登录或浏览器登录致使掉登录
        if (!CookieStore.isLoginBBS()) {
            mContentResponse = new ContentResponse<>();
            mContentResponse.setResultCode(ResultCode.LOGIN_ERR);
        } else {
            ResponseBody body = response.body();
            if (body == null ) {
                mContentResponse = new ContentResponse<>();
                mContentResponse.setResultCode(ResultCode.EMPTY_DATA_ERR);
            } else {
                try {
                    mContentResponse = mResponseHandler.handleNetworkResponse(body.bytes());
                } catch (IOException e) {
                    e.printStackTrace();
                    mContentResponse = new ContentResponse<>(ResultCode.DATA_IO_ERR, e.getMessage());
                }
            }
        }
        mResponseHandler.onResponseHandled(mContentResponse);
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        mContentResponse = new ContentResponse<>();
        mContentResponse.setResultCode(ResultCode.CONNECT_ERR);
        mResponseHandler.onResponseHandled(mContentResponse);
    }
}