package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AccountDao;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.utils.networkResponseHandler.AccountResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.UserInfoResponseHandler;

import java.util.List;

public class AccountSharedViewModel extends ViewModel {
    private MutableLiveData<Account> currentAccount;
    private MutableLiveData<List<Account>> allAccounts;
    private MutableLiveData<UserInfo> userInfo;

    public MutableLiveData<Account> getCurrentAccount() {
        if (currentAccount == null) {
            currentAccount = new MutableLiveData<>();
            if (allAccounts == null) {
                allAccounts = new MutableLiveData<>();
            }
            loadAccounts();
        }
        return currentAccount;
    }

    public MutableLiveData<List<Account>> getAllAccounts() {
        if (allAccounts == null) {
            allAccounts = new MutableLiveData<>();
            if (currentAccount == null) {
                currentAccount = new MutableLiveData<>();
            }
            loadAccounts();
        }
        return allAccounts;
    }

    public MutableLiveData<UserInfo> getUserInfo() {
        if (userInfo == null) {
            userInfo = new MutableLiveData<>();
            requestUserInfo();
        }
        return userInfo;
    }

    private void loadAccounts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AccountDao dao = AppDatabase.getDatabase(BBSApplication.getAppContext()).getAccountDao();
                List<Account> accounts = dao.loadAllAccounts();
                allAccounts.postValue(dao.loadAllAccounts());
                for (Account account : accounts) {
                    if (account.getFlag() == Account.FLAG_CURRENT) {
                        currentAccount.postValue(account);
                        break;
                    }
                }
            }
        }).start();
    }

    public void updateCurrentAccount(Account account) {

        List<Account> accountList = allAccounts.getValue();
        if (accountList == null)
            return;
        // switch / new login
        boolean existed = false;
        for (int i = 0; i< accountList.size(); ++i) {
            // 修正历史记录
            accountList.get(i).flag = Account.FLAG_HISTORY;
            if (accountList.get(i).id.equals(account.id)){
                accountList.get(i).setFlag(Account.FLAG_CURRENT);
                accountList.get(i).setAvatar(account.getAvatar());
                accountList.get(i).setPasswd(account.getPasswd());
                existed = true;
            }
        }
        if (!existed) {
            accountList.add(account);
        }
        allAccounts.postValue(accountList);
        currentAccount.postValue(account);

        if (userInfo != null) {
            requestUserInfo();
        }

        AccountDao accountDao = AppDatabase.getDatabase(BBSApplication.getAppContext()).getAccountDao();
        BBSApplication.setAccount(account);

        accountDao.insertAccount(accountList);
    }

    public void login(Account account) {
        NetworkEntry.login(account, new AccountResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Account> response) {
                updateCurrentAccount(response.getContent());
            }
        });
    }

    public void logout(SimpleResponseHandler responseHandler) {
        NetworkEntry.logout(responseHandler);
    }

    public void requestUserInfo() {
        String userId = currentAccount.getValue().getId();
        NetworkEntry.requestUserInfo(userId, new UserInfoResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<UserInfo> response) {
                UserInfo userInfo = response.getContent();
                userInfo.setId(userId);
                AccountSharedViewModel.this.userInfo.postValue(userInfo);

                Account account = currentAccount.getValue();
                if (!account.getAvatar().equals(userInfo.getAvatar())) {
                    account.setAvatar(userInfo.getAvatar());
                    updateCurrentAccount(account);
                }
            }
        });
    }
}
