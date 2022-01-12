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
    public boolean favourite;

    public Board(@NonNull String id, String name, String section, boolean favourite) {
        this.id = id;
        this.name = name;
        this.section = section;
        this.favourite = favourite;
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

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
