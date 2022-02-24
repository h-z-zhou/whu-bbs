package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;

/*
*   do nothing，just return BaseResponse with SUCCESS
*
*   => login / logout
* */
public abstract class SimpleResponseHandler implements BaseResponseHandler {
    @Override
    public BaseResponse handleNetworkResponse(@NonNull byte[] data) {
        return new BaseResponse();
    }
}
