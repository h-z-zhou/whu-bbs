package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

import java.util.Map;

public class FindPasswordViewModel extends ViewModel {

    protected MutableLiveData<ContentResponse<Object>> responseLiveData;

    public MutableLiveData<ContentResponse<Object>> getResponseLiveData() {
        if (responseLiveData == null) {
            responseLiveData = new MutableLiveData<>();
        }
        return responseLiveData;
    }

    public void findPassword(Map<String, String> form) {

        NetworkEntry.findPassword(form, new SimpleResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Object> response) {
                responseLiveData.postValue(response);
            }
        });
    }
}
