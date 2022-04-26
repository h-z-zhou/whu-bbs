package com.wuda.bbs.ui.board;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;

import java.util.ArrayList;
import java.util.List;

public class ArticleContainerViewModel extends ViewModel {
    private final MutableLiveData<BaseBoard> board;

    public MutableLiveData<ContentResponse<List<BriefArticle>>> articleResponse;
    public MutableLiveData<List<BriefArticle>> articleList;

    public MutableLiveData<BaseBoard> getBoard() {
        return board;
    }

    public ArticleContainerViewModel() {
        articleResponse = new MutableLiveData<>();
        articleResponse.setValue(new ContentResponse<>(new ArrayList<>()));
        articleList = new MutableLiveData<>();
        articleList.setValue(new ArrayList<>());
        board = new MutableLiveData<>();
    }

    public void appendArticles(List<BriefArticle> briefArticleList) {
        List<BriefArticle> preBriefArticleList = this.articleList.getValue();
        if (preBriefArticleList == null)
            return;
        preBriefArticleList.addAll(briefArticleList);
        this.articleList.postValue(preBriefArticleList);
    }
}