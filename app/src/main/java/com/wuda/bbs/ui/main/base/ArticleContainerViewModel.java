package com.wuda.bbs.ui.main.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.ArticleResponse;

public class ArticleContainerViewModel extends ViewModel {
    public MutableLiveData<ArticleResponse> articleResponse;

    public ArticleContainerViewModel() {
        articleResponse = new MutableLiveData<>();
        articleResponse.setValue(new ArticleResponse());
    }
}