package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ContentResponse;

public interface BaseResponseHandler {
    BaseResponse handleNetworkResponse(@NonNull byte[] data);
    void onResponseHandled(BaseResponse baseResponse);
}
