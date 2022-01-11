package com.wuda.bbs.utils.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface WebService {
    // http://bbs.whu.edu.cn/wForum/favboard.php?bname=Test&select=0
    @GET("wForum/{page}")
    public Call<ResponseBody> request(@Path("page") String page, @QueryMap Map<String, String> map);
}
