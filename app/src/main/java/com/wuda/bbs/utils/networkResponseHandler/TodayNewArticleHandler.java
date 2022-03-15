package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.UnsupportedEncodingException;
import java.util.List;

public abstract class TodayNewArticleHandler implements ContentResponseHandler<List<BriefArticle>> {
    @Override
    public ContentResponse<List<BriefArticle>> handleNetworkResponse(@NonNull byte[] data) {
        try {
            return HtmlParser.parseNewsToday(new String(data, "GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e);
        }
    }
}
