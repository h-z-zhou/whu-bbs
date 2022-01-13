package com.wuda.bbs.bean;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    public String name;
    public String passwd;
    public int flag;  // 是否为当前用户，登录使用

    public static int FLAG_CURRENT = 0, FLAG_HISTORY = 1;

    public User(@NonNull String name, String passwd, int flag) {
        this.name = name;
        this.passwd = passwd;
        this.flag = flag;
    }

    public boolean equals(User user) {
        return user.name.equals(name) && user.passwd.equals(passwd) && user.flag == flag;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
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
