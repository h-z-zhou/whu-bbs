package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.Attachment;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class UploadAttachmentHandler implements ContentResponseHandler<List<Attachment>> {
    @Override
    public ContentResponse<List<Attachment>> handleNetworkResponse(@NonNull byte[] data) {

        List<Attachment> attachmentList = new ArrayList<>();

        ContentResponse<List<Attachment>> response;

        try {
            String html = new String(data, "GBK");
            Elements forms = Jsoup.parse(html).getElementsByTag("form");
            Element deleteForm = forms.last();
            if (deleteForm!=null && deleteForm.attr("name").equals("deleteattach")) {
                Elements lis = deleteForm.getElementsByTag("li");

                Pattern namePattern = Pattern.compile(".*?(?=\\()");
                Pattern sizePattern = Pattern.compile("(?<=\\().*?(?=\\) <a)");
                Pattern idPattern = Pattern.compile("(?<=deletesubmit\\(').*?(?='\\);)");
                for (Element li: lis) {
                    String liHtml = li.html();
                    Matcher nameMatcher = namePattern.matcher(liHtml);
                    Matcher sizeMatcher = sizePattern.matcher(liHtml);
                    Matcher idMatcher = idPattern.matcher(liHtml);

                    if (nameMatcher.find() && sizeMatcher.find() && idMatcher.find()) {
                        attachmentList.add(new Attachment(
                                nameMatcher.group(),
                                sizeMatcher.group(),
                                idMatcher.group()
                        ));
                    }
                }

            }

            response = new ContentResponse<>();
            response.setContent(attachmentList);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e);
        }
        return response;
    }
}
