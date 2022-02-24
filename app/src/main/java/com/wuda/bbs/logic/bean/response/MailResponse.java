package com.wuda.bbs.logic.bean.response;

import com.wuda.bbs.logic.bean.Mail;

import java.util.ArrayList;
import java.util.List;

public class MailResponse extends BaseResponse {
    List<Mail> mailList;
    int currentPage;
    int totalPage;

    public MailResponse() {
        mailList = new ArrayList<>();
        currentPage = 0;
        totalPage = 1;
    }

    public List<Mail> getMailList() {
        return mailList;
    }

    public void setMailList(List<Mail> mailList) {
        this.mailList = mailList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
