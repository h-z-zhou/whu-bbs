package com.wuda.bbs.ui.main.board;

import androidx.annotation.NonNull;

import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.ui.main.base.ArticleContainerFragment;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardArticleFragment extends ArticleContainerFragment {

    BaseBoard baseBoard;

    @Override
    protected void requestArticleFromServer() {
        if (baseBoard == null) {
            article_srl.setRefreshing(false);
            return;
        }
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("page", Integer.toString(requestPage));
        form.put("board", baseBoard.getId());
        mobileService.request("topics", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                article_srl.setRefreshing(false);
                try {
                    String text = response.body().string();
                    ArticleResponse articleResponse = XMLParser.parseTopic(text);
                    mViewModel.articleResponse.postValue(articleResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                article_srl.setRefreshing(false);

            }
        });
    }

    public void setBoard(BaseBoard baseBoard) {
        // 放到 mViewModel ???
        this.baseBoard = baseBoard;
    }
}
