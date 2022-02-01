package com.wuda.bbs.utils.parser;

import com.wuda.bbs.bean.BaseResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public static BaseResponse parseFindPasswordResponse(String responseText) {
        BaseResponse response = new BaseResponse();

        JSONObject responseJson = null;
        try {
            responseJson = new JSONObject(responseText);
            responseJson.has("success");
            if (responseJson.has("success")) {
                response.setMassage(responseJson.getString("success"));
            } else if (responseJson.has("error")) {
                response.setSuccessful(false);
                response.setMassage(responseJson.getString("error"));
            } else {
                response.setSuccessful(false);
                response.setMassage("未定义错误！");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            response.setSuccessful(false);
            response.setMassage(e.getMessage());
        }

        return response;
    }
}

