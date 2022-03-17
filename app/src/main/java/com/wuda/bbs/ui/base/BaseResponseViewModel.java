package com.wuda.bbs.ui.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.response.ContentResponse;

public class BaseResponseViewModel extends ViewModel {
    protected MutableLiveData<ContentResponse<?>> errorResponseMutableLiveData;

    public MutableLiveData<ContentResponse<?>> getErrorResponseMutableLiveData() {
        if (errorResponseMutableLiveData == null) {
            errorResponseMutableLiveData = new MutableLiveData<>();
        }
        return errorResponseMutableLiveData;
    }
}
