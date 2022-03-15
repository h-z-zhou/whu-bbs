package com.wuda.bbs.utils.network;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RootService {

    @GET("{page}")
    Call<ResponseBody> get(@Path("page") String page);

    @GET("{page}")
    Call<ResponseBody> get(@Path("page") String page, @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("{page}")
    Call<ResponseBody> post(@Path("page") String page, @FieldMap Map<String, String> formData);

    @FormUrlEncoded
    @POST("{page}")
    Call<ResponseBody> post(@Path("page") String page, @QueryMap Map<String, String> queryMap, @FieldMap Map<String, String> formData);

    @Multipart
    @POST("{page}")
    Call<ResponseBody> uploadFile(@Path("page") String page, @Part MultipartBody.Part file);

    @Multipart
    @POST("{page}")
    Call<ResponseBody> uploadFiles(@Path("page") String page, @QueryMap Map<String, String> queryMap, @Part("counter") RequestBody counter, @Part List<MultipartBody.Part> files);

    // without encoder
    // for GBK Wrapper
    @GET("{page}")
    Call<ResponseBody> _get(@Path("page") String page, @QueryMap(encoded = true) Map<String, String> form);

    @FormUrlEncoded
    @POST("{page}")
    Call<ResponseBody> _post(@Path("page") String page, @FieldMap(encoded = true) Map<String, String> form);

    @FormUrlEncoded
    @POST("{page}")
    Call<ResponseBody> _post(@Path("page") String page, @QueryMap(encoded = true) Map<String, String> queryForm, @FieldMap(encoded = true) Map<String, String> form);
}
