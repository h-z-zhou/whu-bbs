package com.wuda.bbs.logic.bean.bbs;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity
public class DetailBoard extends BaseBoard {

    private String section;

    public DetailBoard(@NonNull String id, String name, String num, String section) {
        super(id, name, num);
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
