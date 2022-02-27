package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.parser.JsonParser;

public abstract class FindPasswordResponseHandler implements ContentResponseHandler<String> {
    @Override
    public ContentResponse<String> handleNetworkResponse(@NonNull byte[] data) {
        return JsonParser.parseFindPasswordResponse(new String(data));
    }
}
