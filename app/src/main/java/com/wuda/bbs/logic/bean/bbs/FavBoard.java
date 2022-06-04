package com.wuda.bbs.logic.bean.bbs;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class FavBoard extends BaseBoard{
    @NonNull
    @ColumnInfo(name = "favor_by_username")
    String favorByUsername;

    public FavBoard(@NonNull String id, String name, String num, @NonNull String favorByUsername) {
        super(id, name, num);
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
