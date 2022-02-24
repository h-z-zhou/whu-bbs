package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;

public interface BaseResponseHandler {
    BaseResponse handleNetworkResponse(@NonNull byte[] data);
    void onResponseHandled(BaseResponse baseResponse);
}
