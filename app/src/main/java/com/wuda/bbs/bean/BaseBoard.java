package com.wuda.bbs.bean;

import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

public class BaseBoard {
    @NonNull
    @PrimaryKey
    String id;
    String name;

    public BaseBoard(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
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
}
