package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.util.List;

public abstract class MailListHandler implements ContentResponseHandler<List<Mail>> {
    @Override
    public ContentResponse<List<Mail>> handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseMailList(new String(data));
    }
}
