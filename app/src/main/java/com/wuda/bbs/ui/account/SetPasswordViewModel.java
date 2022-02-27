package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.networkResponseHandler.SetPasswordResponseHandler;

import java.util.Map;

public class SetPasswordViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<ContentResponse<String>> responseLiveData;

    public MutableLiveData<ContentResponse<String>> getResponseLiveData() {
        if (responseLiveData == null) {
            responseLiveData = new MutableLiveData<>();
        }
        return responseLiveData;
    }

    public void setPassword(Map<String, String> form) {
        NetworkEntry.setPassword(form, new SetPasswordResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<String> response) {
                if (response.isSuccessful()) {
                    responseLiveData.postValue(response);
                }
            }
        });
    }
}