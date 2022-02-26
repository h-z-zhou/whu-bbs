package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.UnsupportedEncodingException;

public abstract class TodayNewArticleHandler implements BaseResponseHandler {
    @Override
    public BaseResponse handleNetworkResponse(@NonNull byte[] data) {
        try {
            String html = new String(data, "GBK");
            return HtmlParser.parseNewsToday(html);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new BriefArticleResponse(ResultCode.DATA_ERR, e.getMessage());
        }
    }
}
