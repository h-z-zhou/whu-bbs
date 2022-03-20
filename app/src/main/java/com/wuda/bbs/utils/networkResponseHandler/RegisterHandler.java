package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;

public abstract class RegisterHandler implements ContentResponseHandler<String> {
    @Override
    public ContentResponse<String> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<String> response;
        try {
            String html = new String(data, "GBK");
            Document doc = Jsoup.parse(html);
            Elements tables = doc.getElementsByTag("table");
            response = new ContentResponse<>();
            if (!tables.isEmpty()) {
                response.setContent(tables.get(0).text());
            } else {
                response.setContent(html);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e);
        }
        return response;
    }
}
