package com.wuda.bbs.ui.mail;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.networkResponseHandler.MailListHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void requestMailsFromServer() {
        Map<String, String> form = new HashMap<>();
        form.put("list", "1");
        form.put("boxname", box.getValue().first);

        NetworkEntry.requestMailList(form, new MailListHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<Mail>> response) {
                if (response.isSuccessful()) {
                    mailResponse.postValue(response);
                }
            }
        });

    }
}