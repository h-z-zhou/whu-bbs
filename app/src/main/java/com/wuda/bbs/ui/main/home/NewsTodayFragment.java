package com.wuda.bbs.ui.main.home;

import androidx.annotation.NonNull;

import com.wuda.bbs.bean.BriefArticleResponse;
import com.wuda.bbs.ui.main.base.ArticleContainerFragment;
import com.wuda.bbs.utils.htmlParser.HtmlParser;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.network.WebService;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsTodayFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {

        WebService webService = ServiceCreator.create(WebService.class);
        webService.request("newtopic.php", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    BriefArticleResponse briefArticleResponse = new BriefArticleResponse();
                    try {
                        briefArticleResponse = HtmlParser.parseNewsToday(new String(responseBody.bytes(), "GBK"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mViewModel.articleResponse.postValue(briefArticleResponse);
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