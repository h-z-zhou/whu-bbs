package com.wuda.bbs.ui.mail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.MailContent;

public class MailContentViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<MailContent> mailContent;

    public MailContentViewModel() {
        mailContent = new MutableLiveData<>();
    }

    public String box2dir(String box) {
        switch (box) {
            case "inbox":
                return ".DIR";
            case "deleted":
                return ".DELETED";
            case "sendbox":
                return ".SENT";
            default:
                return ".DIR";
        }
    }
}