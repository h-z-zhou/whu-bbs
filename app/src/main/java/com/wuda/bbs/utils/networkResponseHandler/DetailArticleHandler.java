package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.DetailArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.util.List;

public abstract class DetailArticleHandler implements ContentResponseHandler<List<DetailArticle>> {
    @Override
    public ContentResponse<List<DetailArticle>> handleNetworkResponse(@NonNull byte[] data) {
        return  XMLParser.parseDetailArticle(new String(data));
    }
}
