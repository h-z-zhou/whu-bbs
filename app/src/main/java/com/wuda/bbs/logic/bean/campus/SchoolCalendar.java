package com.wuda.bbs.logic.bean.campus;


import com.wuda.bbs.utils.campus.ServerURL;

public class SchoolCalendar {
    String name;
    String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return ServerURL.CALENDAR_UC + "/xl/" + url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
