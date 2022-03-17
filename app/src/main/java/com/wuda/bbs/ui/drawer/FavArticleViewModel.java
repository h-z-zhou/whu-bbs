package com.wuda.bbs.ui.drawer;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.FavArticle;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.networkResponseHandler.FavArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.List;
import java.util.Map;

public class FavArticleViewModel extends BaseResponseViewModel {
    private MutableLiveData<List<FavArticle>> favArticleMutableLiveData;

    public MutableLiveData<List<FavArticle>> getFavArticleMutableLiveData() {
        if (favArticleMutableLiveData == null) {
            favArticleMutableLiveData = new MutableLiveData<>();
        }
        return favArticleMutableLiveData;
    }

    public void requestFavArticleFromServer() {

        NetworkEntry.requestFavArticle(new FavArticleHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavArticle>> response) {
                if (response.isSuccessful()) {
                    favArticleMutableLiveData.postValue(response.getContent());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void removeFavArticle(FavArticle favArticle) {

        Map<String, String> form = NetTool.extractUrlParam(favArticle.getDelUrl());
        NetworkEntry.removeFavArticle(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (!response.isSuccessful()) {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}
