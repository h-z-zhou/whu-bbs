package com.wuda.bbs.utils.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MobileService {
    // app => poster recomm hot boards
    @GET("mobile.php")
    public Call<ResponseBody> request(@Query("app") String app, @QueryMap Map<String, String> formData);
}
