package com.wuda.bbs.ui.home;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.board.ArticleContainerFragment;
import com.wuda.bbs.utils.networkResponseHandler.HotArticleHandler;

import java.util.List;

public class HotArticleFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {

        NetworkEntry.requestHotArticle(new HotArticleHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<BriefArticle>> response) {
                mViewModel.articleResponse.postValue(response);
            }
        });
    }
}