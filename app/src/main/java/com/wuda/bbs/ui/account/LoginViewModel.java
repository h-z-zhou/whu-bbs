package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.AccountResponseHandler;

public class LoginViewModel extends BaseResponseViewModel {
    // keep the text of EditTextView
    String id = "";
    String pwd = "";
    private MutableLiveData<ContentResponse<Account>> accountResponseLiveData;

    public MutableLiveData<ContentResponse<Account>> getAccountResponseLiveData() {
        if (accountResponseLiveData == null) {
            accountResponseLiveData = new MutableLiveData<>();
        }
        return accountResponseLiveData;
    }

    public void login(Account account) {
        NetworkEntry.login(account, new AccountResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Account> response) {
                if (response.isSuccessful()) {
                    accountResponseLiveData.postValue(response);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}