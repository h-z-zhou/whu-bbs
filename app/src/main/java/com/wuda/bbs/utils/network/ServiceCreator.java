package com.wuda.bbs.utils.network;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class ServiceCreator {


    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NetConst.BASE)
            .client(new OkHttpClient.Builder()
                    .cookieJar(CookieStore.newInstance())
                    .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(@NonNull String message) {
                            //打印retrofit日志
                            Log.i("RetrofitLog", message);
                        }
                        }).setLevel(HttpLoggingInterceptor.Level.BASIC)
                    )
                    .build()
            )
            .build();

    public static <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
