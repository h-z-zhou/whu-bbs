package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.UnsupportedEncodingException;

public abstract class RegisterResponseHandler implements BaseResponseHandler {
    @Override
    public BaseResponse handleNetworkResponse(@NonNull byte[] data) {
        BaseResponse baseResponse;
        try {
            String text = new String(data, "GBK");
            baseResponse = HtmlParser.parseRegisterResponse(text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            baseResponse = new BaseResponse(ResultCode.ERROR, e.getMessage());
        }
        return baseResponse;
    }
}
