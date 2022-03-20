package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.FindPasswordResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

import java.util.Map;

public class FindPasswordViewModel extends BaseResponseViewModel {

    protected MutableLiveData<ContentResponse<String>> responseLiveData;

    public MutableLiveData<ContentResponse<String>> getResponseLiveData() {
        if (responseLiveData == null) {
            responseLiveData = new MutableLiveData<>();
        }
        return responseLiveData;
    }

    public void findPassword(Map<String, String> form) {

        NetworkEntry.findPassword(form, new FindPasswordResponseHandler() {
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
