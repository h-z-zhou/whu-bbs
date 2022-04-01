package com.wuda.bbs.ui.article;

import android.os.Trace;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.luck.picture.lib.entity.LocalMedia;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Attachment;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.AttachmentDetectHandler;
import com.wuda.bbs.utils.networkResponseHandler.UploadAttachmentHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PostArticleViewModel extends BaseResponseViewModel {

    String board;
    String title;
    String content;

    int attachmentState = 0;  // 0 unknown, -1 false, 1 true

    List<LocalMedia> localMediaList;

    private MutableLiveData<String> postedResultMutableLiveData;

    public MutableLiveData<String> getPostedResultMutableLiveData() {
        if (postedResultMutableLiveData == null) {
            postedResultMutableLiveData = new MutableLiveData<>();
        }
        return postedResultMutableLiveData;
    }

    public void post() {
        if (localMediaList != null) {
            uploadPhotos();
        } else {
            postArticle();
        }
    }

    protected void postArticle() {
        // board=&relID=0&font=&subject=&Content=&signature=
        Map<String, String> form = new HashMap<>();
        form.put("board", board);
        form.put("relID", "0");
        form.put("font", "");
        form.put("subject", title);
        form.put("Content", content);
        form.put("signature", "");

        NetworkEntry.postArticle(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                String text = response.getContent().getResult();
                if (response.isSuccessful()) {
                    postedResultMutableLiveData.postValue(response.getContent().getResult());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void uploadPhotos() {
        List<MultipartBody.Part> photos = new ArrayList<>();

        for (int i=0; i<localMediaList.size(); i++) {
            LocalMedia localMedia = localMediaList.get(i);
            File file = new File(localMedia.getPath());
            String fileName = localMedia.getFileName();
            String formName = "attachfile" + i;

            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData(formName, fileName, photoRequestBody);
            photos.add(part);
        }

        NetworkEntry.uploadAttachments(photos, new UploadAttachmentHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<Attachment>> response) {
                if (response.isSuccessful()) {
                    postArticle();
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void detectAttachable() {
        Map<String, String> form = new HashMap<>();
        form.put("board", board);
        NetworkEntry.detectAttachment(form, new AttachmentDetectHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean isAttachable = response.getContent();
                    attachmentState = isAttachable? 1: -1;
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}