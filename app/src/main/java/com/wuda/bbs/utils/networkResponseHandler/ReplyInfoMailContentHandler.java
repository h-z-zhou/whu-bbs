package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ReplyInfoMailContentHandler implements ContentResponseHandler<String>{
    @Override
    public ContentResponse<String> handleNetworkResponse(@NonNull byte[] data) {

        ContentResponse<String> response = new ContentResponse<>();

        try {
            String html = new String(data, "GBK");
            Pattern pattern = Pattern.compile("<a href=\"postarticle.php?(.*?)\" >");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                response.setContent(matcher.group());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.DATA_IO_ERR);
            response.setException(e);
        }

        return response;
    }
}
