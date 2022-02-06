package com.wuda.bbs.bean.response;

import com.wuda.bbs.bean.Friend;

import java.util.List;

public class FriendResponse extends BaseResponse{

    List<Friend> friendList;

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }
}
