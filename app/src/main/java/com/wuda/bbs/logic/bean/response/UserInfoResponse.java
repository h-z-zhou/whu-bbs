package com.wuda.bbs.logic.bean.response;

import com.wuda.bbs.logic.bean.UserInfo;

public class UserInfoResponse extends BaseResponse{
    UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
