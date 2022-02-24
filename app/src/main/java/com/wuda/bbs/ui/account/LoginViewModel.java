package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.response.AccountResponse;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.networkResponseHandler.AccountResponseHandler;

public class LoginViewModel extends ViewModel {
    // keep the text of EditTextView
    String id = "";
    String pwd = "";
    private MutableLiveData<AccountResponse> accountResponseLiveData;

    public MutableLiveData<AccountResponse> getAccountResponseLiveData() {
        if (accountResponseLiveData == null) {
            accountResponseLiveData = new MutableLiveData<>();
        }
        return accountResponseLiveData;
    }

    public void login(Account account) {
        NetworkEntry.login(account, new AccountResponseHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                if (baseResponse instanceof AccountResponse) {
                    accountResponseLiveData.postValue((AccountResponse) baseResponse);
                }
            }
        });
    }
}