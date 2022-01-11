package com.wuda.bbs.ui.main.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.Article;
import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.HotArticle;

import java.util.List;

public class HotViewModel extends ViewModel {
    public MutableLiveData<ArticleResponse> hotArticleResponse;
    // TODO: Implement the ViewModel
    public HotViewModel() {
        hotArticleResponse = new MutableLiveData<>();
        hotArticleResponse.setValue(new ArticleResponse());
    }
}