package com.wuda.bbs.bean;

import java.util.ArrayList;
import java.util.List;

public class BriefArticleResponse {
    boolean success;
    Integer currentPage;
    Integer totalPage;
    List<BriefArticle> briefArticleList;

    public BriefArticleResponse() {
        currentPage = 0;
        totalPage = 1;
        briefArticleList = new ArrayList<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<BriefArticle> getBriefArticleList() {
        return briefArticleList;
    }

    public void setBriefArticleList(List<BriefArticle> briefArticleList) {
        this.briefArticleList = briefArticleList;
    }

    public void addArticle(BriefArticle briefArticle) {
        briefArticleList.add(briefArticle);
    }
}
