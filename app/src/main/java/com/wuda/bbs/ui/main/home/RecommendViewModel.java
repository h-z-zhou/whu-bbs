package com.wuda.bbs.ui.main.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.ArticleResponse;

public class RecommendViewModel extends ViewModel {
    MutableLiveData<ArticleResponse> recommendArticleResponse;

    public RecommendViewModel() {
        this.recommendArticleResponse = new MutableLiveData<>();
        this.recommendArticleResponse.setValue(new ArticleResponse());
    }
}