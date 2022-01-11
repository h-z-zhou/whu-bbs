package com.wuda.bbs.utils.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @FormUrlEncoded
    @POST("mobile.php")
    public Call<ResponseBody> login(@Field("app") String app, @Field("id") String username, @Field("passwd") String passwd);
}
