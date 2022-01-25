package com.wuda.bbs.ui.login;

import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.User;

import java.util.ArrayList;
import java.util.List;

public class LoginViewModel extends ViewModel {
    // 登录的用户信息
    // 历史帐号记录
    List<User> historyUserList = new ArrayList<>();
    // 当前用户信息（输入框保持）
    User currentUser = new User("", "", User.FLAG_CURRENT);

    public void updateCurrentUser(User user) {
        if (!currentUser.equals(user)) {
            currentUser = user;
            for (int i=0; i<historyUserList.size(); ++i) {
                // 修正历史记录
                historyUserList.get(i).flag = User.FLAG_HISTORY;
                if (historyUserList.get(i).id.equals(user.id)){
                    historyUserList.remove(i);
                }
            }
            historyUserList.add(0, currentUser);
        }
    }
}