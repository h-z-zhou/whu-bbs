package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.util.List;

public abstract class HotArticleHandler implements ContentResponseHandler<List<BriefArticle>> {
    @Override
    public ContentResponse<List<BriefArticle>> handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseHot(new String(data));
    }
}
