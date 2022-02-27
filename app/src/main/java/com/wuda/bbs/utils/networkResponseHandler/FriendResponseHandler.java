package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.util.List;

public abstract class FriendResponseHandler implements ContentResponseHandler<List<Friend>> {
    @Override
    public ContentResponse<List<Friend>> handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseFriends(new String(data));
    }
}
