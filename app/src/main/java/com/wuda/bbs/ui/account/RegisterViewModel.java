package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.RegisterHandler;

import java.util.Map;

public class RegisterViewModel extends BaseResponseViewModel {
    private MutableLiveData<ContentResponse<String>> responseLiveData;

    public MutableLiveData<ContentResponse<String>> getResponseLiveData() {
        if (responseLiveData == null) {
            responseLiveData = new MutableLiveData<>();
        }
        return responseLiveData;
    }

    public void register(Map<String, String> form) {

        NetworkEntry.register(form, new RegisterHandler() {
            @Override
            public void onResponseHandled(ContentResponse<String> response) {
                if (response.isSuccessful()) {
                    responseLiveData.postValue(response);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}