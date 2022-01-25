package com.wuda.bbs.ui.main.mail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MailContentViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<String> mailContent;

    public MailContentViewModel() {
        mailContent = new MutableLiveData<>();
    }
}