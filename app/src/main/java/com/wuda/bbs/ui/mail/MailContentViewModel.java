package com.wuda.bbs.ui.mail;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.Mail;
import com.wuda.bbs.logic.bean.bbs.MailContent;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.ReplyInfoMailContentHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebMailContentHandler;

import java.util.HashMap;
import java.util.Map;

public class MailContentViewModel extends BaseResponseViewModel {
    // TODO: Implement the ViewModel
    Mail mail;
    String boxName;
    private MutableLiveData<MailContent> mailContentMutableLiveData;
    private MutableLiveData<String> replyArticleUrlMutableLivaData;

    public MutableLiveData<MailContent> getMailContentMutableLiveData() {
        if (mailContentMutableLiveData == null) {
            mailContentMutableLiveData = new MutableLiveData<>();
        }
        return mailContentMutableLiveData;
    }

    public MutableLiveData<String> getReplyArticleUrlMutableLivaData() {
        if (replyArticleUrlMutableLivaData == null) {
            replyArticleUrlMutableLivaData = new MutableLiveData<>();
        }
        return replyArticleUrlMutableLivaData;
    }

    //    private void requestMailContent() {
//        Map<String, String> form = new HashMap<>();
//        form.put("read", mail.getNum());
//        form.put("boxname", boxName);
//
//        NetworkEntry.requestMailContent(form, new MailContentHandler() {
//            @Override
//            public void onResponseHandled(ContentResponse<String> response) {
//                if (response.isSuccessful()) {
//                    mailContent.postValue(response.getContent());
//                }
//                mailContent.postValue(response.getContent());
//            }
//        });
//    }

    public void requestMailContentFromWeb() {
        Map<String, String> form = new HashMap<>();

        form.put("dir", box2dir(boxName));
        form.put("num", mail.getNum());

        NetworkEntry.requestMailContent(form, new WebMailContentHandler() {
            @Override
            public void onResponseHandled(ContentResponse<MailContent> response) {
                if (response.isSuccessful()) {
                    MailContent mailContent = response.getContent();

                    mailContentMutableLiveData.postValue(mailContent);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void requestReplyArticleUrl() {
        Map<String, String> form = new HashMap<>();
        form.put("boxname", boxName);
        form.put("num", mail.getNum());

        NetworkEntry.requestMailContent(form, new ReplyInfoMailContentHandler() {
            @Override
            public void onResponseHandled(ContentResponse<String> response) {
                if (response.isSuccessful()) {
                    getReplyArticleUrlMutableLivaData().postValue(response.getContent());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    private String box2dir(String box) {
        switch (box) {
            case "inbox":
                return ".DIR";
            case "deleted":
                return ".DELETED";
            case "sendbox":
                return ".SENT";
            default:
                return ".DIR";
        }
    }
}