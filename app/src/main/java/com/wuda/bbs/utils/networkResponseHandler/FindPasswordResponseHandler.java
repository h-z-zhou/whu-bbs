package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.parser.JsonParser;

public abstract class FindPasswordResponseHandler implements BaseResponseHandler {
    @Override
    public BaseResponse handleNetworkResponse(@NonNull byte[] data) {

        String text = new String(data);
        return JsonParser.parseFindPasswordResponse(text);
    }
}
