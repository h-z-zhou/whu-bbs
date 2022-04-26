package com.wuda.bbs.ui.mail;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.MailListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MailViewModel extends BaseResponseViewModel {

    private MutableLiveData<List<Mail>> mailListMutableLiveData;

//    final String[] boxNames = new String[]{"inbox", "sendbox", "deleted"};
//    final String[] boxTitles = new String[]{"收信箱", "发信箱", "废信箱"};
    MutableLiveData<Pair<String, String>> box;
    int selectedPosition = -1;

    int currentPage = 0;
    int totalPage = 1;

    public MutableLiveData<List<Mail>> getMailListMutableLiveData() {
        if (mailListMutableLiveData == null) {
            mailListMutableLiveData = new MutableLiveData<>();
        }
        return mailListMutableLiveData;
    }

    public MailViewModel() {
        box = new MutableLiveData<>(new Pair<>("inbox", "收信箱"));
    }

    public void requestMailsFromServer() {
        Map<String, String> form = new HashMap<>();
        form.put("list", "1");
        form.put("boxname", box.getValue().first);

        NetworkEntry.requestMailList(form, new MailListHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<Mail>> response) {
                if (response.isSuccessful()) {
                    mailListMutableLiveData.postValue(response.getContent());
                    currentPage = response.getCurrentPage();
                    totalPage = response.getTotalPage();
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}