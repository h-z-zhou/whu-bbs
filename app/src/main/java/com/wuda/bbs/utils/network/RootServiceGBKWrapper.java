package com.wuda.bbs.utils.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class RootServiceGBKWrapper {

    RootService mRootService;

    public RootServiceGBKWrapper(RootService mRootService) {
        this.mRootService = mRootService;
    }

    public Call<ResponseBody> get(String page, Map<String, String> form) {
        return mRootService._get(page, NetTool.encodeUrlFormWithGBK(form));
    }

    public Call<ResponseBody> post(String page, Map<String, String> form) {
        return mRootService._post(page, NetTool.encodeUrlFormWithGBK(form));
    }

    public Call<ResponseBody> post(String page, Map<String, String> queryForm, Map<String, String> form) {
        return mRootService._post(page, NetTool.encodeUrlFormWithGBK(queryForm), NetTool.encodeUrlFormWithGBK(form));
    }
}
