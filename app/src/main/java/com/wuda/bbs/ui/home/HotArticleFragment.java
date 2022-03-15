package com.wuda.bbs.ui.home;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.board.ArticleContainerFragment;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.CustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.networkResponseHandler.HotArticleHandler;

import java.util.List;

public class HotArticleFragment extends ArticleContainerFragment {

    @Override
    protected void requestArticleFromServer() {

        NetworkEntry.requestHotArticle(new HotArticleHandler() {
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
                    new ResponseErrorHandlerDialog(getContext())
                            .addErrorMsg(response.getResultCode(), null)
                            .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                                @Override
                                public void onButtonClick() {
                                    requestArticleFromServer();
                                }
                            })
                            .show();
//                    Toast.makeText(getContext(), response.getMassage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}