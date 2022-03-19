package com.wuda.bbs.ui.mail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SendMailViewModel extends ViewModel {
    MutableLiveData<String> userIdMutableLiveData = new MutableLiveData<>();
    String userId;
    String title;
    String content;
}
