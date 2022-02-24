package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.networkResponseHandler.SetPasswordResponseHandler;

import java.util.Map;

public class SetPasswordViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<BaseResponse> baseResponseLiveData;

    public MutableLiveData<BaseResponse> getBaseResponseLiveData() {
        if (baseResponseLiveData == null) {
            baseResponseLiveData = new MutableLiveData<>();
        }
        return baseResponseLiveData;
    }

    public void setPassword(Map<String, String> form) {
        NetworkEntry.setPassword(form, new SetPasswordResponseHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                baseResponseLiveData.postValue(baseResponse);
            }
        });
    }
}