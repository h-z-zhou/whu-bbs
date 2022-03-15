package com.wuda.bbs.utils.parser;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

    public static ContentResponse<String> parseFindPasswordResponse(String responseText) {
        ContentResponse<String> response;

        try {
            JSONObject responseJson = new JSONObject(responseText);
            responseJson.has("success");
            if (responseJson.has("success")) {
                response = new ContentResponse<>();
                response.setContent(responseJson.getString("success"));
            } else if (responseJson.has("error")) {
                response = new ContentResponse<>(ResultCode.SERVER_HANDLE_ERR);
                response.setContent(responseJson.getString("error"));
            } else {
                response = new ContentResponse<>(ResultCode.UNMATCHED_CONTENT_ERR);
            }
        } catch (JSONException e) {
            response = new ContentResponse<>(ResultCode.UNMATCHED_CONTENT_ERR, e);
            e.printStackTrace();
        }
        return response;
    }
}

