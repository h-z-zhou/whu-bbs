package com.wuda.bbs.logic.bean.response;

import com.wuda.bbs.logic.bean.BriefArticle;

import java.util.ArrayList;
import java.util.List;

public class BriefArticleResponse extends BaseResponse {
    Integer currentPage;
    Integer totalPage;
    List<BriefArticle> briefArticleList;

    public BriefArticleResponse(ResultCode resultCode, String massage) {
        super(resultCode, massage);
    }

    public BriefArticleResponse() {
        currentPage = 0;
        totalPage = 1;
        briefArticleList = new ArrayList<>();
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
