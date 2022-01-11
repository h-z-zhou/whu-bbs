package com.wuda.bbs.utils.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServiceCreator {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ServerURL.BASE)
            .client(new OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.SECONDS)
                    .connectTimeout(1, TimeUnit.SECONDS)
                    .writeTimeout(1, TimeUnit.SECONDS) //设置超时
                    .build()
            )
            .build();

    public static <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
