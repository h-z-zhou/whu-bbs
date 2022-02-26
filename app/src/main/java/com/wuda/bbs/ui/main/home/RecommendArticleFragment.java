package com.wuda.bbs.ui.main.home;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.ui.main.board.ArticleContainerFragment;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.RecommendArticleHandler;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendArticleFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {

        RecommendArticleHandler handler = new RecommendArticleHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                article_srl.setRefreshing(false);
                if (baseResponse.isSuccessful()) {
                    if (baseResponse instanceof BriefArticleResponse) {
                        mViewModel.articleResponse.postValue((BriefArticleResponse) baseResponse);
                    }
                } else {
                    Toast.makeText(getContext(), baseResponse.getMassage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        NetworkEntry.requestRecommendArticle(handler);


    }
}