package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WebMailContentHandler implements ContentResponseHandler<String> {
    @Override
    public ContentResponse<String> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<String> response;
        try {
            String html = new String(data, "GBK");
            Elements articles = Jsoup.parse(html).getElementsByClass("article");
            if (!articles.isEmpty()) {
                String content = articles.get(0).html();
                response = new ContentResponse<>();
                response.setContent(beautifyContent(content));
            } else  {
                response = new ContentResponse<>(ResultCode.DATA_ERR, "未知错误");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.DATA_ERR, e.getMessage());
        }
        return response;
    }

    private String beautifyContent(String content){
        Pattern pattern = Pattern.compile("(?<=\\\\n\\\\n)(.*?)(?=\\\\n'\\);)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            content = matcher.group();
        }
        content = content.replaceAll("\\\\n", "");
        content = content.replaceAll("\\\\r.*?m", "\n");

        return content.trim();
    }
}
