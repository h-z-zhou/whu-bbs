package com.wuda.bbs.utils.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface FindPasswordService {
    @FormUrlEncoded
    @POST("r/doreset.php")
    public Call<ResponseBody> post(@FieldMap Map<String, String> formData);
}
