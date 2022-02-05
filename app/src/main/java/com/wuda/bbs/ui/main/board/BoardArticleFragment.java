package com.wuda.bbs.ui.main.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.BriefArticleResponse;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.ui.main.base.ArticleContainerFragment;
import com.wuda.bbs.ui.main.article.PostArticleActivity;
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

    BaseBoard board;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        FloatingActionButton writeArticle_fab = new FloatingActionButton(view.getContext());
        writeArticle_fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_create));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.bottomMargin = 32;
        params.rightMargin = 32;
        writeArticle_fab.setLayoutParams(params);

        writeArticle_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PostArticleActivity.class);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });

        article_root_fl.addView(writeArticle_fab);

        return view;
    }

    @Override
    protected void requestArticleFromServer() {
        if (board == null) {
            article_srl.setRefreshing(false);
            return;
        }
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("page", Integer.toString(requestPage));
        form.put("board", board.getId());
        mobileService.request("topics", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                article_srl.setRefreshing(false);
                try {
                    String text = response.body().string();
                    BriefArticleResponse briefArticleResponse = XMLParser.parseTopic(text);
                    mViewModel.articleResponse.postValue(briefArticleResponse);
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
        this.board = baseBoard;
    }
}
