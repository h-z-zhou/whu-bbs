package com.wuda.bbs.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;

import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;

import java.util.ArrayList;
import java.util.List;

public class ThemeManager {

    private static int currentColorThemeID = R.style.Theme_red;

    private static final List<ColorThemeItem> colorThemeList = new ArrayList<>();
    static {
        colorThemeList.add(new ColorThemeItem(R.style.Theme_red, "red"));
        colorThemeList.add(new ColorThemeItem(R.style.Theme_purple, "purple"));
        colorThemeList.add(new ColorThemeItem(R.style.Theme_green, "green"));
        colorThemeList.add(new ColorThemeItem(R.style.Theme_grey, "grey"));
        colorThemeList.add(new ColorThemeItem(R.style.Theme_orange, "orange"));
        colorThemeList.add(new ColorThemeItem(R.style.Theme_pink, "pink"));
        colorThemeList.add(new ColorThemeItem(R.style.Theme_BBS, "default"));
    }

    public static int getCurrentColorThemeID() {
        return currentColorThemeID;
    }

    public static List<ColorThemeItem> getColorThemeList() {
        return colorThemeList;
    }

    public static void saveColorTheme(String colorTheme) {

        currentColorThemeID = getColorThemeID(colorTheme);

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = BBSApplication.getAppContext().getSharedPreferences("theme", Context.MODE_PRIVATE).edit();
        editor.putString("color", colorTheme);
        editor.apply();
    }

    public static void loadColorTheme() {
        SharedPreferences sharedPreferences = BBSApplication.getAppContext().getSharedPreferences("theme", Context.MODE_PRIVATE);
        String colorTheme = sharedPreferences.getString("color", "default");
        currentColorThemeID = getColorThemeID(colorTheme);
    }

    public static void setColorTheme(Context mContext) {
        mContext.setTheme(currentColorThemeID);
    }

    public static int getColorThemeID(String colorTheme) {

        for (ColorThemeItem item: colorThemeList) {
            if (colorTheme.equals(item.name)) {
                return item.resId;
            }
        }

        return R.style.Theme_BBS;
    }

    public static int getThemePrimaryColor(Context mContext, int resId) {
        int[] attrs = {R.attr.colorPrimary};
        @SuppressLint("Recycle") TypedArray array = mContext.obtainStyledAttributes(resId, attrs);
        return array.getColor(0, Color.BLACK);
    }

    public static class ColorThemeItem {

        Integer resId;
        String name;

        public ColorThemeItem(Integer resId, String name) {
            this.resId = resId;
            this.name = name;
        }

        public Integer getResId() {
            return resId;
        }

        public String getName() {
            return name;
        }
    }
}
