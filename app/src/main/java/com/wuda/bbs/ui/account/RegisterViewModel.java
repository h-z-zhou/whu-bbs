package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.networkResponseHandler.RegisterResponseHandler;

import java.util.Map;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<BaseResponse> baseResponseLiveData;

    public MutableLiveData<BaseResponse> getBaseResponseLiveData() {
        if (baseResponseLiveData == null) {
            baseResponseLiveData = new MutableLiveData<>();
        }
        return baseResponseLiveData;
    }

    public void register(Map<String, String> form) {
        NetworkEntry.register(form, new RegisterResponseHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                baseResponseLiveData.postValue(baseResponse);
            }
        });
    }
}