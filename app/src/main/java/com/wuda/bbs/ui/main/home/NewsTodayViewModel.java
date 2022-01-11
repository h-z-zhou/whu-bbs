package com.wuda.bbs.ui.main.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.ArticleResponse;

public class NewsTodayViewModel extends ViewModel {
    public MutableLiveData<ArticleResponse> newsTodayArticleResponse;
    // TODO: Implement the ViewModel
    public NewsTodayViewModel() {
        newsTodayArticleResponse = new MutableLiveData<>();
        newsTodayArticleResponse.setValue(new ArticleResponse());
    }
}