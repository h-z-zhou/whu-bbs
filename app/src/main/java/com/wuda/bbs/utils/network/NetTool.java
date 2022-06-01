package com.wuda.bbs.utils.network;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NetTool {
    /**
     * 对传输的数据进行 GBK 编码
     * @param form 编码数据
     * @return  GBK编码的数据
     */
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

    /**
     * 从连接中提取参数
     * @param url
     * @return
     */
    public static Map<String, String> extractUrlParam(String url) {
        Map<String, String> parameters = new HashMap<>();

        String params = url.split("\\?")[1];
        String[] param_arr = params.split("&");
        for (String param: param_arr) {
            String[] pair = param.split("=");
            if (pair.length == 2) {
                parameters.put(pair[0], pair[1]);
            }
        }

        return parameters;
    }
}
