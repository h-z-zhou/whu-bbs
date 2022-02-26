package com.wuda.bbs.ui.main.board;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.logic.bean.BaseBoard;

import java.util.ArrayList;
import java.util.List;

public class ArticleContainerViewModel extends ViewModel {
    public MutableLiveData<BaseBoard> board;

    public MutableLiveData<BriefArticleResponse> articleResponse;
    public MutableLiveData<List<BriefArticle>> articleList;

    public ArticleContainerViewModel() {
        articleResponse = new MutableLiveData<>();
        articleResponse.setValue(new BriefArticleResponse());
        articleList = new MutableLiveData<>();
        articleList.setValue(new ArrayList<>());
        board = new MutableLiveData<>();
    }

    public void appendArticles(List<BriefArticle> briefArticleList) {
        List<BriefArticle> preBriefArticleList = this.articleList.getValue();
        preBriefArticleList.addAll(briefArticleList);
        this.articleList.postValue(preBriefArticleList);
    }
}