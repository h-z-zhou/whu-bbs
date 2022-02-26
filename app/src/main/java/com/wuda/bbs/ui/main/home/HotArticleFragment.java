package com.wuda.bbs.ui.main.home;

import android.widget.Toast;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.ui.main.board.ArticleContainerFragment;
import com.wuda.bbs.utils.networkResponseHandler.HotArticleHandler;

public class HotArticleFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {

        HotArticleHandler handler = new HotArticleHandler() {
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

        NetworkEntry.requestHotArticle(handler);
    }
}