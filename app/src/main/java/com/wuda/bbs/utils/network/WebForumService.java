package com.wuda.bbs.utils.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface WebForumService {
    // http://bbs.whu.edu.cn/wForum/favboard.php?bname=Test&select=0
    @GET("wForum/{page}")
    public Call<ResponseBody> get(@Path("page") String page, @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("wForum/{page}")
    public Call<ResponseBody> post(@Path("page") String page, @FieldMap Map<String, String> formData);

    @FormUrlEncoded
    @POST("wForum/{page}")
    public Call<ResponseBody> postWithEncoded(@Path("page") String page, @FieldMap(encoded = true) Map<String, String> formData);
}
