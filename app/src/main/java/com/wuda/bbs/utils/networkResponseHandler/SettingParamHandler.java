package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class SettingParamHandler implements ContentResponseHandler<List<Boolean>>{
    @Override
    public ContentResponse<List<Boolean>> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<List<Boolean>> response;

        try {
            response = HtmlParser.parseUserParamResponse(new String(data, "GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.DATA_ERR, e.getMessage());
        }
        return response;
    }
}
