package com.wuda.bbs.logic.bean.bbs;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import java.io.Serializable;

public class BaseBoard implements Serializable {
    @NonNull
    @PrimaryKey
    String id;
    String name;
    String num;

    public BaseBoard(@NonNull String id, String name, String num) {
        this.id = id;
        this.name = name;
        this.num = num;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
