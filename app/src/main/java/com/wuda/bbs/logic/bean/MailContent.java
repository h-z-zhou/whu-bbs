package com.wuda.bbs.logic.bean;

public class MailContent {
    String content;
    String delUrl;

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
}
