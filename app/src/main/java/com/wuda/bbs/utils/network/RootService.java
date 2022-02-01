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

public interface RootService {
    @GET("{page}")
    public Call<ResponseBody> request(@Path("page") String page, @QueryMap Map<String, String> map);

    @FormUrlEncoded
//    @POST("r/doreset.php")
    @POST("{page}")
    public Call<ResponseBody> post(@Path("page") String page, @FieldMap Map<String, String> formData);
}
