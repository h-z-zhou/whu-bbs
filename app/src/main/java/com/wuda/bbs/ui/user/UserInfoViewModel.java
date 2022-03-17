package com.wuda.bbs.ui.user;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FriendDao;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.UserInfoResponseHandler;

import java.util.HashMap;
import java.util.Map;

public class UserInfoViewModel extends BaseResponseViewModel {
    String userId = "";

    private MutableLiveData<Boolean> friendStateMutableLiveData;
    private MutableLiveData<UserInfo> userInfoMutableLiveData;

    public MutableLiveData<Boolean> getFriendStateMutableLiveData() {
        if (friendStateMutableLiveData == null) {
            friendStateMutableLiveData = new MutableLiveData<>();
        }
        return friendStateMutableLiveData;
    }

    public MutableLiveData<UserInfo> getUserInfoMutableLiveData() {
        if (userInfoMutableLiveData == null) {
            userInfoMutableLiveData = new MutableLiveData<>();
        }
        return userInfoMutableLiveData;
    }

    public void requestUserInfoFromServer() {
        NetworkEntry.requestUserInfo(userId, new UserInfoResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<UserInfo> response) {
                if (response.isSuccessful()) {
                    userInfoMutableLiveData.postValue(response.getContent());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void queryFriendFromDB() {
        FriendDao friendDao = AppDatabase.getDatabase().getFriendDao();
        friendStateMutableLiveData.postValue(friendDao.loadFriend(userId) != null);
    }

    public void operateFriend() {
        Map<String, String> form = new HashMap<>();
        form.put("app", "friend");
        if (friendStateMutableLiveData.getValue() == null) return;
        if (friendStateMutableLiveData.getValue()) {
            form.put("delete", userId);
        } else {
            form.put("add", userId);
        }

        NetworkEntry.operateFriend(form, new SimpleResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Object> response) {
                friendStateMutableLiveData.postValue(!friendStateMutableLiveData.getValue());
                UserInfo userInfo = userInfoMutableLiveData.getValue();
                if (userInfo == null) return;
                Friend friend = new Friend(userId, "", userInfo.getAvatar());

                FriendDao friendDao = AppDatabase.getDatabase().getFriendDao();
                if (friendStateMutableLiveData.getValue()) {
                    friendDao.insertFriend(friend);
                } else {
                    friendDao.deleteFriend(friend);
                }
            }
        });
    }
}
