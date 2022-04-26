package com.wuda.bbs.logic.bean.bbs;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Friend {
    @PrimaryKey
    @NonNull
    String id;
    String alias;
    String avatar;

    public Friend(@NonNull String id, String alias, String avatar) {
        this.id = id;
        this.alias = alias;
        this.avatar = avatar;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public String getAvatar() {
        return avatar;
    }

}
