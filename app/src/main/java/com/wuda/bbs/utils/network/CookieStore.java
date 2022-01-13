package com.wuda.bbs.utils.network;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieStore implements CookieJar {

    static Map<String, List<Cookie>> cookieStore = new HashMap<>();

//    public CookieStore() {
//        if (BBSApplication.getAppContext() != null) {
//            SharedPreferences sp = BBSApplication.getAppContext().getSharedPreferences("cookies", Context.MODE_PRIVATE);
//        }
//    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        if (url.toString().equals(NetConst.LOGIN)) {
            Map<String, Cookie> filter = new HashMap<>();
            for (Cookie cookie: cookies) {
                filter.put(cookie.name(), cookie);
            }
            cookieStore.put(url.host(), new ArrayList<>(filter.values()));
        }
    }

    @NonNull
    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        if (cookies != null) {
            return cookies;
        } else {
            return new ArrayList<>();
        }
    }
}
