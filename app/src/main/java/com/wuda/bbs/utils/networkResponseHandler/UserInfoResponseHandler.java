package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.UserInfoResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

public abstract class UserInfoResponseHandler implements BaseResponseHandler {
    @Override
    public UserInfoResponse handleNetworkResponse(@NonNull byte[] data) {
        String xmlData = new String(data);
        return XMLParser.parseUserInfo(xmlData);
    }
}
