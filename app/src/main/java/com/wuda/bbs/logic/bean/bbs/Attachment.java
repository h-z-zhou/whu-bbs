package com.wuda.bbs.logic.bean.bbs;

import java.io.Serializable;

public class Attachment implements Serializable {
    String name;
    String size;
    String id;

    public Attachment(String name, String size, String id) {
        this.name = name;
        this.size = size;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
