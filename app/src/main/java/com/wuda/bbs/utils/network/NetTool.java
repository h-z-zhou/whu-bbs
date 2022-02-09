package com.wuda.bbs.utils.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NetTool {
    public static Map<String, String> encodeUrlFormWithGBK(Map<String, String> form) {
        Map<String, String> encodedForm = new HashMap<>();
        for (String key: form.keySet()) {
            try {
                String encodedKey = URLEncoder.encode(key, "GBK");
                String encodedValue = URLEncoder.encode(form.get(key), "GBK");
                encodedForm.put(encodedKey, encodedValue);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return encodedForm;
    }
}
