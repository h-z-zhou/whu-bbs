
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

public class NoAuthBBSCallback<T> implements Callback<ResponseBody> {

    ContentResponseHandler<T> mResponseHandler;
    ContentResponse<T> mContentResponse;

    public NoAuthBBSCallback(ContentResponseHandler<T> mResponseHandler) {
        this.mResponseHandler = mResponseHandler;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
        ResponseBody body = response.body();
        if (body == null ) {
            mContentResponse = new ContentResponse<>();
            mContentResponse.setResultCode(ResultCode.ERROR);
        } else {
            try {
                mContentResponse = mResponseHandler.handleNetworkResponse(body.bytes());
            } catch (IOException e) {
                e.printStackTrace();
                mContentResponse = new ContentResponse<>(ResultCode.ERROR, e.getMessage());
            }
        }
        mResponseHandler.onResponseHandled(mContentResponse);
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        mContentResponse = new ContentResponse<>(ResultCode.CONNECT_ERR, t.getMessage());
    }
}