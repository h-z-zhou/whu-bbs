package com.wuda.bbs.utils.network;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.networkResponseHandler.BaseResponseHandler;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BBSCallback3 implements Callback<ResponseBody> {

    BaseResponseHandler mResponseHandler;
    BaseResponse mBaseResponse;

    public BBSCallback3(BaseResponseHandler mResponseHandler) {
        this.mResponseHandler = mResponseHandler;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
        ResponseBody body = response.body();
        if (body == null ) {
            mBaseResponse = new BaseResponse();
            mBaseResponse.setResultCode(ResultCode.ERROR);
        } else {
            try {
                mBaseResponse = mResponseHandler.handleNetworkResponse(body.bytes());
            } catch (IOException e) {
                e.printStackTrace();
                mBaseResponse = new BaseResponse();
                mBaseResponse.setResultCode(ResultCode.ERROR);
                mBaseResponse.setMassage(e.getMessage());
            }
        }
        mResponseHandler.onResponseHandled(mBaseResponse);
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        mBaseResponse = new BaseResponse();
        mBaseResponse.setResultCode(ResultCode.CONNECT_ERR);
        mResponseHandler.onResponseHandled(mBaseResponse);
    }
}

