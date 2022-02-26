package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

public abstract class TopicArticleHandler implements BaseResponseHandler {
    @Override
    public BaseResponse handleNetworkResponse(@NonNull byte[] data) {
        String xml = new String(data);
        return XMLParser.parseTopic(xml);
    }
}
