package com.wuda.bbs.logic.bean.response;

import com.wuda.bbs.logic.bean.Account;

public class AccountResponse extends BaseResponse {
    Account account;

    public AccountResponse() {
    }

    public AccountResponse(Account account) {
        this.account = account;
    }

    public AccountResponse(ResultCode resultCode, String massage) {
        super(resultCode, massage);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
