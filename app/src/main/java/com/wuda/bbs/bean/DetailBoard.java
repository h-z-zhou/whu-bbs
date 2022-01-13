package com.wuda.bbs.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DetailBoard extends BaseBoard {

    private String section;

    public DetailBoard(@NonNull String id, String name, String section) {
        super(id, name);
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
