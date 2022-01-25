package com.wuda.bbs.ui.user;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.UserInfo;

public class UserInfoViewModel extends ViewModel {
    String userId;
    MutableLiveData<UserInfo> userInfo;

    public UserInfoViewModel() {
        userInfo = new MutableLiveData<>();
    }
}
