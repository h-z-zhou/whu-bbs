package com.wuda.bbs.utils.parser;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public static ContentResponse<String> parseFindPasswordResponse(String responseText) {
        ContentResponse<String> response;

        JSONObject responseJson = null;
        try {
            responseJson = new JSONObject(responseText);
            responseJson.has("success");
            if (responseJson.has("success")) {
                response = new ContentResponse<>();
                response.setContent(responseJson.getString("success"));
//                response.setMassage(responseJson.getString("success"));
            } else if (responseJson.has("error")) {
                response = new ContentResponse<>(ResultCode.ERROR, responseJson.getString("error"));
            } else {
                response = new ContentResponse<>(ResultCode.ERROR, "未定义错误！");
            }
        } catch (JSONException e) {
            response = new ContentResponse<>(ResultCode.DATA_ERR, e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}

