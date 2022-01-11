package com.wuda.bbs.utils.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BBSAppService {
    @GET("mobile/{app}")
    Call<ResponseBody> getApp(@Query("app") String app);
}
