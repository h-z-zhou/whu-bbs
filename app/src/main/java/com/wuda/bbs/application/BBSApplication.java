package com.wuda.bbs.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class BBSApplication extends Application {
    private static Context appContext;
    private static String username;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
        SharedPreferences sp = appContext.getSharedPreferences("user", MODE_PRIVATE);
        username = sp.getString("name", "guest");
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUserInfo(String username, String passwd) {
        SharedPreferences.Editor editor = appContext.getSharedPreferences("user", MODE_PRIVATE).edit();

        if (!BBSApplication.username.equals(username)) {
            BBSApplication.username = username;
            editor.putString("name", username);
        }

        editor.apply();
    }
}
