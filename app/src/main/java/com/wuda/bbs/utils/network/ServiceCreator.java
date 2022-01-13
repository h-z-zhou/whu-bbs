package com.wuda.bbs.utils.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class ServiceCreator {

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetConst.BASE)
            .client(new OkHttpClient.Builder()
                    .cookieJar(new CookieStore())
                    .build()
            )
            .build();

    public static <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
