package com.wuda.bbs.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.wuda.bbs.logic.bean.bbs.Account;

public class BBSApplication extends Application {
    private static Context appContext;
    private static Account account;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
        SharedPreferences sp = appContext.getSharedPreferences("user", MODE_PRIVATE);
        String id = sp.getString("id", "guest");
        String passwd = sp.getString("passwd", "");
        String avatar = sp.getString("avatar", "");
        account = new Account(id, passwd, avatar, Account.FLAG_CURRENT);
    }

    public static void setAccount(Account account) {
        if (!account.getId().equals(BBSApplication.account.getId())) {
            BBSApplication.account = account;
            SharedPreferences.Editor editor = appContext.getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("id", account.getId());
            editor.putString("passwd", account.getPasswd());
            editor.putString("avatar", account.getAvatar());
            editor.apply();
        }
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static String getAccountId() {
        return account.getId();
    }

    public static String getAccountAvatar() {
        return account.getAvatar();
    }

    public static boolean isLogin() {
        return !account.getId().equals("guest");
    }
}
