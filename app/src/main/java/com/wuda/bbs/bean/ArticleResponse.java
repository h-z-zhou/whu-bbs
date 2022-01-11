package com.wuda.bbs.bean;

import java.util.ArrayList;
import java.util.List;

public class ArticleResponse {
    boolean success;
    int currentPage;
    int totalPage;
    List<Article> articleList;

    public ArticleResponse() {
        articleList = new ArrayList<>();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
