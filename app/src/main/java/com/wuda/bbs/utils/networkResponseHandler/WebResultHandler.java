package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;

public abstract class WebResultHandler implements ContentResponseHandler<WebResult>{
    @Override
    public ContentResponse<WebResult> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<WebResult> response =new ContentResponse<>();
        try {
            String html = new String(data, "GBK");
            Document doc = Jsoup.parse(html);
            Elements tables = doc.getElementsByClass("TableBody1");
            if (!tables.isEmpty()) {
                Element tb = tables.get(tables.size()-1);
                response.setContent(new WebResult(tb.text()));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.DATA_ERR);
            response.setMassage(e.getMessage());
        }

        return response;
    }
}
