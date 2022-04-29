package com.wuda.bbs.logic.bean.bbs;

import java.util.List;

public class MailContent {
    String content;
    String delUrl;
    List<Attachment> attachmentList;

    public MailContent(String content, String delUrl) {
        this.content = content;
        this.delUrl = delUrl;
    }

    public String getContent() {
        return content;
    }

    public String getDelUrl() {
        return delUrl;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
