package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.Article;
import com.wuda.bbs.bean.ArticleResponse;

import org.xml.sax.helpers.DefaultHandler;

public class BaseArticleHandler extends DefaultHandler {
    ArticleResponse articleResponse;

    public BaseArticleHandler() {
        articleResponse = new ArticleResponse();
    }

    public ArticleResponse getArticleResponse() {
        return articleResponse;
    }
}
