package com.wuda.bbs.ui.login;

import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.User;

import java.util.ArrayList;
import java.util.List;

public class LoginViewModel extends ViewModel {
    // 登录的用户信息
    // 历史帐号记录
    List<User> historyLoginUserList = new ArrayList<>();
    // 当前用户信息（输入框保持）
    User currentUser = new User("guest", "", 'h');
}