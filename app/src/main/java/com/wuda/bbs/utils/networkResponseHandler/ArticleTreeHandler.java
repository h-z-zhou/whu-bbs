package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;

public abstract class ArticleTreeHandler implements ContentResponseHandler{
    @Override
    public ContentResponse handleNetworkResponse(@NonNull byte[] data) {
        return null;
    }
}
