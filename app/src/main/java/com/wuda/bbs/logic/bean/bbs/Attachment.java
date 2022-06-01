package com.wuda.bbs.logic.bean.bbs;

import java.io.Serializable;

public class Attachment implements Serializable {
    String name;
    String size;
    String url;

    public Attachment(String name, String size, String url) {
        this.name = name;
        this.size = size;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
