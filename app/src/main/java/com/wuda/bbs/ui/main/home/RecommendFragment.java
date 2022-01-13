package com.wuda.bbs.ui.main.home;

import androidx.annotation.NonNull;

import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.ui.main.base.ArticleContainerFragment;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        mobileService.request("recomm", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = response.body().string();
                    ArticleResponse articleResponse = XMLParser.parseRecommend(text);
                    mViewModel.articleResponse.postValue(articleResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                article_srl.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                article_srl.setRefreshing(false);
            }
        });

    }
}