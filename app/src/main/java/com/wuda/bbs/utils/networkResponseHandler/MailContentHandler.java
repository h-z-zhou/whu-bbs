package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

public abstract class MailContentHandler implements ContentResponseHandler<String>{

    @Override
    public ContentResponse<String> handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseMailContent(new String(data));
    }
}


