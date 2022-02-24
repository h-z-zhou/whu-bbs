package com.wuda.bbs.logic.bean;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class FavorBoard extends BaseBoard{
    @NonNull
    @ColumnInfo(name = "favor_by_username")
    String favorByUsername;

    public FavorBoard(@NonNull String id, String name, @NonNull String favorByUsername) {
        super(id, name);
        this.favorByUsername = favorByUsername;
    }


    @NonNull
    public String getFavorByUsername() {
        return favorByUsername;
    }

    public void setFavorByUsername(@NonNull String favorByUsername) {
        this.favorByUsername = favorByUsername;
    }
}
