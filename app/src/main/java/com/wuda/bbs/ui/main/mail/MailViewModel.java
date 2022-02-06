package com.wuda.bbs.ui.main.mail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.response.MailResponse;

public class MailViewModel extends ViewModel {
    MutableLiveData<MailResponse> mailResponse;

    public MailViewModel() {
        mailResponse = new MutableLiveData<>();
        mailResponse.setValue(new MailResponse());
    }

    // TODO: Implement the ViewModel
}