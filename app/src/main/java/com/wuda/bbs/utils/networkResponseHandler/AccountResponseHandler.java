package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.Account;
import com.wuda.bbs.logic.bean.response.ContentResponse;

public abstract class AccountResponseHandler implements ContentResponseHandler<Account> {
    @Override
    public ContentResponse<Account> handleNetworkResponse(@NonNull byte[] data) {
        return new ContentResponse<>();
    }
}
