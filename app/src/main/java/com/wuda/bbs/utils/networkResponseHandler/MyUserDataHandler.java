package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public abstract class MyUserDataHandler implements ContentResponseHandler<Map<String, String>> {
    @Override
    public ContentResponse<Map<String, String>> handleNetworkResponse(@NonNull byte[] data) {

        ContentResponse<Map<String, String>> response = new ContentResponse<>();

        try {
            String html = new String(data, "GBK");
            Document doc = Jsoup.parse(html);

            Map<String, String> formData = new HashMap<>();
            String name, value;

            Elements form = doc.getElementsByTag("form");
            if (!form.isEmpty()) {
                Elements inputs = form.get(0).getElementsByTag("input");
                for (Element input: inputs) {
                    if (input.hasAttr("type")) {
                        if (input.attr("type").equals("radio")) {
                            if (input.hasAttr("checked")) {
                                name = input.attr("name");
                                value = input.attr("value");
                                formData.put(name, value);
                                continue;
                            }
                        } else if (input.attr("type").equals("reset")) {
                            continue;
                        }
                    }

                    name = input.attr("name");
                    value = input.attr("value");
                    formData.put(name, value);
                }

                Elements selectors = form.get(0).getElementsByTag("select");
                for (Element select: selectors) {
                    name = select.attr("name");
                    for (Element option: select.getElementsByTag("option")) {
                        if (option.hasAttr("selected")) {
                            value = option.attr("value");
                            formData.put(name, value);
                            break;
                        }
                    }
                }

                Elements textarea = form.get(0).getElementsByTag("textarea");
                for (Element area: textarea) {
                    name = area.attr("name");
                    value = area.text();
                    formData.put(name, value);
                }

            }

            response.setContent(formData);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
            response.setMassage(e.getMessage());
        }

        return response;
    }
}
