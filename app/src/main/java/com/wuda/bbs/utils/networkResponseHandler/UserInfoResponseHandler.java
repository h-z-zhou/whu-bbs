package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

public abstract class UserInfoResponseHandler implements ContentResponseHandler<UserInfo> {

    @Override
    public ContentResponse<UserInfo> handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseUserInfo(new String(data));
    }

//    public UserInfoResponse handleNetworkRespondse(@NonNull byte[] data) {
//        String xmlData = new String(data);
//        return XMLParser.parseUserInfo(xmlData);
//    }
}
