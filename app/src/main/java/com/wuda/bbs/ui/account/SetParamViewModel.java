package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.SettingParamHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.List;
import java.util.Map;

public class SetParamViewModel extends BaseResponseViewModel {
    private MutableLiveData<List<Boolean>> userParamMutableLiveData;

    public MutableLiveData<List<Boolean>> getUserParamMutableLiveData() {
        if (userParamMutableLiveData == null) {
            userParamMutableLiveData = new MutableLiveData<>();
        }
        return userParamMutableLiveData;
    }

    public void requestChoicesFromServer() {
        NetworkEntry.requestSettingParam(new SettingParamHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<Boolean>> response) {
                if (response.isSuccessful()) {
                    userParamMutableLiveData.postValue(response.getContent());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void setParam(Map<String, String> form) {
        NetworkEntry.setSettingParam(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (!response.isSuccessful()) {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}