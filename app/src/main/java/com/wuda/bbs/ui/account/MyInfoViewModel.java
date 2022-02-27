package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.networkResponseHandler.UserInfoResponseHandler;

public class MyInfoViewModel extends ViewModel {

    MutableLiveData<UserInfo> userInfoLiveData;

    public MutableLiveData<UserInfo> getUserInfoLiveData() {
        if (userInfoLiveData == null) {
            userInfoLiveData = new MutableLiveData<>();
        }
        return userInfoLiveData;
    }

    public void requestUserInfo(String userId) {

        NetworkEntry.requestUserInfo(userId, new UserInfoResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<UserInfo> response) {
                UserInfo userInfo = response.getContent();
                userInfo.setId(userId);
                userInfoLiveData.postValue(userInfo);
            }
        });
    }
}