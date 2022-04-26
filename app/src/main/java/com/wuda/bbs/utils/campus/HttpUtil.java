package com.wuda.bbs.utils.campus;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {

    private static final ConcurrentHashMap<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();
    private static final CookieJar cookieJar = new CookieJar() {
        @Override
        public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
            cookieStore.put(httpUrl.host(), list);  // 第一次登录会使用旧的cookie，必须退出后台才可以
        }

        @NonNull
        @Override
        public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
            List<Cookie> cookies = cookieStore.get(httpUrl.host());
//            Log.d("request host", httpUrl.toString());
//            if (cookies != null)
//                Log.d("cookies", cookies.toString());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    };

    private static final String USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36";

    // 登录时清除旧的CAS Cookies
    // cas 请求（记录一次） => 失效
    // 登录 => 新Cookie未加载
    // 重新请求（失败）
    public static void clearCookieStore() {
        cookieStore.clear();
    }

    public static void removeCookie(String host) {
        cookieStore.remove(host);
    }

    // 不需要登录
    public static void sendOkHttpRequest(String address, Callback callback) {
        Log.d("http url", address);
        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("User-Agent", USER_AGENT)
                .build();
        client.newCall(request).enqueue(callback);

    }


    // post
    public static void postOkHttpRequest(String address, Map<String, String> data, Callback callback) {
        Log.d("http url", address);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (String key: data.keySet()) {
            bodyBuilder.add(key, data.get(key));
        }

        Request request = new Request.Builder()
                .url(address)
                .addHeader("User-Agent", USER_AGENT)
                .post(bodyBuilder.build())
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void postOkHttpRequestWithHeader(String address, Map<String, String> data, Map<String, String> header, Callback callback) {
        Log.d("http url", address);

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (String key: data.keySet()) {
            bodyBuilder.add(key, data.get(key));
        }

        Request.Builder builder = new Request.Builder();
        if (!header.isEmpty()) {
            for (String key: header.keySet()) {
                builder.addHeader(key, header.get(key));
            }
        }

        Request request = builder.url(address)
                .addHeader("User-Agent", USER_AGENT)
                .post(bodyBuilder.build())
                .build();
        client.newCall(request).enqueue(callback);
    }
}
