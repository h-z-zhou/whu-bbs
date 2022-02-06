package com.wuda.bbs.bean.response;

import com.wuda.bbs.bean.UserInfo;

public class UserInfoResponse extends BaseResponse{
    UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
