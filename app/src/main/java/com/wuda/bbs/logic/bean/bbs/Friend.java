package com.wuda.bbs.logic.bean.bbs;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Friend {
    @PrimaryKey
    @NonNull
    String id;
    String experience;
    String avatar;

    public Friend(@NonNull String id, String experience, String avatar) {
        this.id = id;
        this.experience = experience;
        this.avatar = avatar;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getExperience() {
        return experience;
    }

    public String getAvatar() {
        return avatar;
    }

}
