package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;

public interface ContentResponseHandler<T> {
    ContentResponse<T> handleNetworkResponse(@NonNull byte[] data);
    void onResponseHandled(ContentResponse<T> response);
}
