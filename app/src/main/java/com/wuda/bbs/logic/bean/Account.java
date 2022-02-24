package com.wuda.bbs.logic.bean;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Account {
    @PrimaryKey
    @NonNull
    public String id;
    public String passwd;
    public String avatar;
    public int flag;  // 是否为当前用户，登录使用

    @Ignore
    public static int FLAG_CURRENT = 0, FLAG_HISTORY = 1;
    @Ignore
    public static Account GUEST = new Account("guest", "", "", FLAG_CURRENT);

    public Account(@NonNull String id, String passwd, String avatar, int flag) {
        this.id = id;
        this.passwd = passwd;
        this.avatar = avatar;
        this.flag = flag;
    }

    @Ignore
    public Account(@NonNull String id, String passwd) {
        this.id = id;
        this.passwd = passwd;
    }

    public boolean equals(Account account) {
        return account.id.equals(id) && account.passwd.equals(passwd) && account.flag == flag;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
