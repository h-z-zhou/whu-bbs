package com.wuda.bbs.ui.main.board;

import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.Board;
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

    Board board;

    @Override
    protected void requestArticleFromServer() {
        if (board == null) {
            article_srl.setRefreshing(false);
            return;
        }
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
        form.put("page", mViewModel.articleResponse.getValue().getCurrentPage().toString());
        form.put("board", board.getId());
        mobileService.request("topics", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                article_srl.setRefreshing(false);

            }
        });
    }

    public void setBoard(Board board) {
        // 放到 mViewModel ???
        this.board = board;
    }
}
