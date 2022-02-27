package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;

/*
*   do nothing，just return BaseResponse with SUCCESS
*
*   => login / logout / (add/delete)friend
* */
public abstract class SimpleResponseHandler implements ContentResponseHandler<Object> {
    @Override
    public ContentResponse<Object> handleNetworkResponse(@NonNull byte[] data) {
        return new ContentResponse<>();
    }
}
