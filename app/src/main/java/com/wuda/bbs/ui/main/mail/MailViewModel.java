package com.wuda.bbs.ui.main.mail;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.response.MailResponse;

public class MailViewModel extends ViewModel {
    MutableLiveData<MailResponse> mailResponse;
    final String[] boxNames = new String[]{"inbox", "sendbox", "deleted"};
    final String[] boxTitles = new String[]{"收信箱", "发信箱", "废信箱"};
    MutableLiveData<Pair<String, String>> box;

    public MailViewModel() {
        box = new MutableLiveData<>(new Pair<>("inbox", "收信箱"));
        mailResponse = new MutableLiveData<>();
        mailResponse.setValue(new MailResponse());
    }

    // TODO: Implement the ViewModel
}