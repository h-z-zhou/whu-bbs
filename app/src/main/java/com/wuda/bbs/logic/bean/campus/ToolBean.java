package com.wuda.bbs.logic.bean.campus;

import java.io.Serializable;


public class ToolBean implements Serializable {
    int iconId;
    String iconColor;
    String name;
    String url;
    // ToolActivity 加载的 Fragment
    Class<?> targetFragmentClz;

    public ToolBean() {

    }

    public ToolBean(int iconId, String iconColor, String name, String url, Class<?> targetFragmentClz) {
        this.iconId = iconId;
        this.iconColor = iconColor;
        this.name = name;
        this.url = url;
        this.targetFragmentClz = targetFragmentClz;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<?> getTargetFragmentClz() {
        return targetFragmentClz;
    }

    public void setTargetFragmentClz(Class<?> targetFragmentClz) {
        this.targetFragmentClz = targetFragmentClz;
    }
}
