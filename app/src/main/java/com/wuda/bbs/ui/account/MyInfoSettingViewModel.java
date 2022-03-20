package com.wuda.bbs.ui.account;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.MyUserDataHandler;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.UpLoadAvatarHandler;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MyInfoSettingViewModel extends BaseResponseViewModel {
    Map<String, String> form;
    private MutableLiveData<Boolean> setInfoResultMutableLiveData;

    public MutableLiveData<Boolean> getSetInfoResultMutableLiveData() {
        if (setInfoResultMutableLiveData == null) {
            setInfoResultMutableLiveData = new MutableLiveData<>();
        }
        return setInfoResultMutableLiveData;
    }

    public void requestMyUserInfo() {
        NetworkEntry.requestMyUserData(new MyUserDataHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Map<String, String>> response) {
                if (response.isSuccessful()) {
                    form = response.getContent();
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void setMyUserInfo(Map<String, String> form) {
        NetworkEntry.setMyUserInfo(form, new SimpleResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Object> response) {
                if (response.isSuccessful()) {
                    setInfoResultMutableLiveData.postValue(Boolean.TRUE);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void uploadAvatar(String path) {

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("upfile", file.getName(), requestBody);
        NetworkEntry.uploadAvatar(part, new UpLoadAvatarHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (response.isSuccessful()) {
                    String newAvatar = response.getContent().getResult();
                    form.put("myface", newAvatar);
                    form.put("width", "80");
                    form.put("height", "80");
                    setMyUserInfo(form);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

}