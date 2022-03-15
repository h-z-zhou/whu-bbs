package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.MailContent;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WebMailContentHandler implements ContentResponseHandler<MailContent> {
    @Override
    public ContentResponse<MailContent> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<MailContent> response;
        try {
            String html = new String(data, "GBK");
            Document doc = Jsoup.parse(html);
            Elements articles = doc.getElementsByClass("article");
            Elements operations = doc.getElementsByClass("oper");
            if (!articles.isEmpty() && !operations.isEmpty() && !operations.get(0).getElementsByTag("a").isEmpty()) {
                String content = beautifyContent(articles.get(0).html());
                Element link = operations.get(0).getElementsByTag("a").get(0);
                String delUrl = link.attr("href");
                response = new ContentResponse<>();
                response.setContent(new MailContent(content, delUrl));
            } else  {
                response = new ContentResponse<>(ResultCode.UNMATCHED_CONTENT_ERR);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e);
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
