package com.wuda.bbs.bean;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String id;
    public String passwd;
    public int flag;  // 是否为当前用户，登录使用

    public static int FLAG_CURRENT = 0, FLAG_HISTORY = 1;

    public User(@NonNull String id, String passwd, int flag) {
        this.id = id;
        this.passwd = passwd;
        this.flag = flag;
    }

    public boolean equals(User user) {
        return user.id.equals(id) && user.passwd.equals(passwd) && user.flag == flag;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
