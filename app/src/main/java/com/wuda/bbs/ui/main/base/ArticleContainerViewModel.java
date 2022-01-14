package com.wuda.bbs.ui.main.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.Article;
import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.BaseBoard;

import java.util.ArrayList;
import java.util.List;

public class ArticleContainerViewModel extends ViewModel {
    public MutableLiveData<BaseBoard> board;

    public MutableLiveData<ArticleResponse> articleResponse;
    public MutableLiveData<List<Article>> articleList;

    public ArticleContainerViewModel() {
        articleResponse = new MutableLiveData<>();
        articleResponse.setValue(new ArticleResponse());
        articleList = new MutableLiveData<>();
        articleList.setValue(new ArrayList<>());
        board = new MutableLiveData<>();
    }

    public void appendArticles(List<Article> articleList) {
        List<Article> preArticleList = this.articleList.getValue();
        preArticleList.addAll(articleList);
        this.articleList.postValue(preArticleList);
    }
}