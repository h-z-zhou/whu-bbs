package com.wuda.bbs.ui.main.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.DetailBoard;

public class ArticleContainerViewModel extends ViewModel {
    public MutableLiveData<ArticleResponse> articleResponse;
    public MutableLiveData<DetailBoard> board;

    public ArticleContainerViewModel() {
        articleResponse = new MutableLiveData<>();
        articleResponse.setValue(new ArticleResponse());
        board = new MutableLiveData<>();
    }
}