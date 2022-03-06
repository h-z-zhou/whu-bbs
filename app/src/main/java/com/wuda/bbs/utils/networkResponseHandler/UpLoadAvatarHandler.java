package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UpLoadAvatarHandler implements ContentResponseHandler<WebResult> {
    @Override
    public ContentResponse<WebResult> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<WebResult> response;

        try {
            String html = new String(data, "GBK");
            Pattern pattern = Pattern.compile("(?<=newsrc = ').*?(?=';\n)");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                response = new ContentResponse<>();
                response.setContent(new WebResult(matcher.group()));
            } else {
                response = new ContentResponse<>();
                response.setResultCode(ResultCode.ERROR);
                Document doc = Jsoup.parse(html);
                Elements tables = doc.getElementsByClass("TableBody1");
                if (!tables.isEmpty()) {
                    Element tb = tables.get(tables.size()-1);
                    response.setContent(new WebResult(tb.text()));
                } else {
                    tables = doc.getElementsByClass("TableBody2");
                    if (!tables.isEmpty()) {
                        Element tb = tables.get(tables.size()-1);
                        response.setContent(new WebResult(tb.text()));
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.DATA_ERR, e.getMessage());
        }
        return response;
    }
}
