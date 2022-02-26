package com.wuda.bbs.ui.main.home;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.ui.main.board.ArticleContainerFragment;
import com.wuda.bbs.utils.networkResponseHandler.TodayNewArticleHandler;
import com.wuda.bbs.utils.parser.HtmlParser;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.network.WebForumService;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayNewArticleFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {

        TodayNewArticleHandler handler = new TodayNewArticleHandler() {
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
        NetworkEntry.requestTodayNewArticle(handler);
    }

}