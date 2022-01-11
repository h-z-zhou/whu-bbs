package com.wuda.bbs.application;

import android.app.Application;
import android.content.Context;

public class BBSApplication extends Application {
    private static Context appContext;
    private static String userCookies;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static String getUserCookies() {
        return userCookies;
    }

    public static void setUserCookies(String userCookies) {
        BBSApplication.userCookies = userCookies;
    }
}
