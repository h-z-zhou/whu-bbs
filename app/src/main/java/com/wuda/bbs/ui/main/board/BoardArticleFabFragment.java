package com.wuda.bbs.ui.main.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.ui.article.PostArticleActivity;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.TopicArticleHandler;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardArticleFabFragment extends BoardArticleFragment {


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

}
