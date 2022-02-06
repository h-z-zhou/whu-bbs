package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.response.BriefArticleResponse;

import org.xml.sax.helpers.DefaultHandler;

public class BaseBriefArticleHandler extends DefaultHandler {
    BriefArticleResponse briefArticleResponse;

    public BaseBriefArticleHandler() {
        briefArticleResponse = new BriefArticleResponse();
    }

    public BriefArticleResponse getArticleResponse() {
        return briefArticleResponse;
    }
}
