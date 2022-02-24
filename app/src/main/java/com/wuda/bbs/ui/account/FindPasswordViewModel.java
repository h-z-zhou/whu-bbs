package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

import java.util.Map;

public class FindPasswordViewModel extends ViewModel {

    protected MutableLiveData<BaseResponse> baseResponseLiveData;

    public MutableLiveData<BaseResponse> getBaseResponseLiveData() {
        if (baseResponseLiveData == null) {
            baseResponseLiveData = new MutableLiveData<>();
        }
        return baseResponseLiveData;
    }

    public void findPassword(Map<String, String> form) {
        NetworkEntry.findPassword(form, new SimpleResponseHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                baseResponseLiveData.postValue(baseResponse);
            }
        });
    }
}
