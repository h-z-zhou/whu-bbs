package com.wuda.bbs.ui.main.board;

import android.widget.Toast;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.utils.networkResponseHandler.TopicArticleHandler;

import java.util.HashMap;
import java.util.Map;

public class BoardArticleFragment extends ArticleContainerFragment {

    BaseBoard board;

    @Override
    protected void requestArticleFromServer() {
        if (board == null) {
            article_srl.setRefreshing(false);
            return;
        }
//        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("page", Integer.toString(requestPage));
        form.put("board", board.getId());

        TopicArticleHandler handler = new TopicArticleHandler() {
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
        NetworkEntry.requestTopicArticle(form, handler);
    }

    public void setBoard(BaseBoard baseBoard) {
        // 放到 mViewModel ???
        this.board = baseBoard;
    }
}
