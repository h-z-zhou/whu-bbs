package com.wuda.bbs.bean;

import java.util.ArrayList;
import java.util.List;

public class DetailArticleResponse {
    String GID;
    Integer currentPage;
    Integer totalPage;
    Integer replyNum;
    String Flag;

    List<DetailArticle> detailArticleList;

    public DetailArticleResponse() {
        detailArticleList = new ArrayList<>();
        totalPage = 1;
        currentPage = 0;
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public List<DetailArticle> getDetailArticleList() {
        return detailArticleList;
    }

    public void setDetailArticleList(List<DetailArticle> detailArticleList) {
        this.detailArticleList = detailArticleList;
    }
}
