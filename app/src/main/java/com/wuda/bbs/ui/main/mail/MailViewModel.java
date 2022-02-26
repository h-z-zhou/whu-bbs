package com.wuda.bbs.ui.main.mail;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;

import java.util.ArrayList;
import java.util.List;

public class MailViewModel extends ViewModel {
    MutableLiveData<ContentResponse<List<Mail>>> mailResponse;
    final String[] boxNames = new String[]{"inbox", "sendbox", "deleted"};
    final String[] boxTitles = new String[]{"收信箱", "发信箱", "废信箱"};
    MutableLiveData<Pair<String, String>> box;

    public MailViewModel() {
        box = new MutableLiveData<>(new Pair<>("inbox", "收信箱"));
        mailResponse = new MutableLiveData<>();
        ContentResponse<List<Mail>> contentResponse = new ContentResponse<>();
        contentResponse.setContent(new ArrayList<>());
        mailResponse.setValue(contentResponse);
    }

    // TODO: Implement the ViewModel
}