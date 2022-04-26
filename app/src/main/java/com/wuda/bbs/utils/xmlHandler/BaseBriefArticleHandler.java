package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;

import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class BaseBriefArticleHandler extends DefaultHandler {
    ContentResponse<List<BriefArticle>> briefArticleResponse;

    public BaseBriefArticleHandler() {
        briefArticleResponse = new ContentResponse<>();
    }

    public ContentResponse<List<BriefArticle>> getBriefArticleResponse() {
        return briefArticleResponse;
    }
}
