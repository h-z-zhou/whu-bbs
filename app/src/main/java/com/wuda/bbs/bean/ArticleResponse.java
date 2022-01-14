package com.wuda.bbs.bean;

import java.util.ArrayList;
import java.util.List;

public class ArticleResponse {
    boolean success;
    Integer currentPage;
    Integer totalPage;
    List<Article> articleList;

    public ArticleResponse() {
        currentPage = 0;
        totalPage = 1;
        articleList = new ArrayList<>();
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

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public void addArticle(Article article) {
        articleList.add(article);
    }
}
