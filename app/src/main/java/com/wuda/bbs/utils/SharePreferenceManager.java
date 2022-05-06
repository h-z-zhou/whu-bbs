package com.wuda.bbs.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.wuda.bbs.application.BBSApplication;

public class SharePreferenceManager {
    public static String NAME_THEME = "theme";
    public static String KEY_THEME_COLOR = "color";
    public static String KEY_THEME_NIGHT = "night";

    public static void save_sp(String name, String key, String value) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = BBSApplication.getAppContext()
                .getSharedPreferences(name, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String load_sp(String name, String key, String defValue) {
        SharedPreferences sharedPreferences = BBSApplication.getAppContext().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defValue);
    }
}
