package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.SetPasswordResponseHandler;

import java.util.HashMap;
import java.util.Map;

public class SetPasswordViewModel extends BaseResponseViewModel {
    private MutableLiveData<Boolean> resultMutableLiveData;
    String oldPassword;
    String new1Password;
    String new2Password;

    public MutableLiveData<Boolean> getResultMutableLiveData() {

        if (resultMutableLiveData == null) {
            resultMutableLiveData = new MutableLiveData<>();
        }
        return resultMutableLiveData;
    }

    public void setPassword() {

        Map<String, String> form = new HashMap<>();
        // pw1=&pw2=&pw3=
        form.put("pw1", oldPassword);
        form.put("pw2", new1Password);
        form.put("pw3", new2Password);

        NetworkEntry.setPassword(form, new SetPasswordResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<String> response) {
                if (response.isSuccessful()) {
                    resultMutableLiveData.postValue(Boolean.TRUE);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}