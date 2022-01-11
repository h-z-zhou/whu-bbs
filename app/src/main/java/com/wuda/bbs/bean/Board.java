package com.wuda.bbs.bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Board {
    @NonNull
    @PrimaryKey
    public String id;
    public String name;
    public String section;
    public boolean isDisplayed;

    public Board(@NonNull String id, String name, String section, boolean isDisplayed) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.isDisplayed = isDisplayed;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isDisplayed() {
        return isDisplayed;
    }

    public void setDisplayed(boolean displayed) {
        isDisplayed = displayed;
    }
}
