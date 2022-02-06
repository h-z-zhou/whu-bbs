package com.wuda.bbs.ui.main.article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.response.DetailArticleResponse;
import com.wuda.bbs.ui.adapter.DetailArticleRecyclerAdapter;
import com.wuda.bbs.ui.widget.TopicDecoration;
import com.wuda.bbs.utils.network.BBSCallback;
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

public class DetailArticleActivity extends AppCompatActivity {

//    BriefArticle mBriefArticle;
    String boardId;
    String groupId;
    DetailArticleResponse detailArticleResponse;

    Toolbar toolbar;
    RecyclerView article_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        article_rv = findViewById(R.id.detailArticle_recyclerView);
        article_rv.setLayoutManager(new LinearLayoutManager(DetailArticleActivity.this));
        article_rv.setAdapter(new DetailArticleRecyclerAdapter(getBaseContext()));
        article_rv.addItemDecoration(new TopicDecoration(getBaseContext()));

        toolbar = findViewById(R.id.detailArticle_toolbar);

//        mBriefArticle = (BriefArticle) getIntent().getSerializableExtra("article");
        boardId = getIntent().getStringExtra("boardId");
        groupId = getIntent().getStringExtra("groupId");

        String title = getIntent().getStringExtra("title");
        if (title != null) {
            toolbar.setTitle(title);
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        requestContentFromServer();

    }


    private void requestContentFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
//        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("GID", groupId);
        form.put("board", boardId);
        form.put("page", "1");
        Log.d("article", form.toString());

        mobileService.request("read", form).enqueue(new BBSCallback<ResponseBody>(DetailArticleActivity.this) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String text = response.body().string();
                        detailArticleResponse = XMLParser.parseDetailArticle(text);
                        ((DetailArticleRecyclerAdapter) article_rv.getAdapter()).updateDataSet(detailArticleResponse.getDetailArticleList());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}