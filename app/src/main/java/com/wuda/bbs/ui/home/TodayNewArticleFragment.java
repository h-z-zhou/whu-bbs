package com.wuda.bbs.ui.home;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.board.ArticleContainerFragment;
import com.wuda.bbs.utils.networkResponseHandler.TodayNewArticleHandler;

import java.util.List;

public class TodayNewArticleFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {

        NetworkEntry.requestTodayNewArticle(new TodayNewArticleHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<BriefArticle>> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        article_srl.setRefreshing(false);
                    }
                });
                if (response.isSuccessful()) {
                    mViewModel.articleResponse.postValue(response);
                } else {
//                    Toast.makeText(getContext(), response.getMassage(), Toast.LENGTH_SHORT).show();
//                    new CustomDialog(getContext()).show();
                }
            }
        });
    }

}