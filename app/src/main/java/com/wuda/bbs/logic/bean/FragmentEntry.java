package com.wuda.bbs.logic.bean;

public class FragmentEntry {
    Class<?> clz;
    String name;
    int iconId;
    int iconColor;
    String msg;

    public FragmentEntry(Class<?> clz, String name, int iconId, int iconColor, String msg) {
        this.clz = clz;
        this.name = name;
        this.iconId = iconId;
        this.iconColor = iconColor;
        this.msg = msg;
    }

    public Class<?> getClz() {
        return clz;
    }

    public String getName() {
        return name;
    }

    public int getIconId() {
        return iconId;
    }

    public int getIconColor() {
        return iconColor;
    }

    public String getMsg() {
        return msg;
    }
}
