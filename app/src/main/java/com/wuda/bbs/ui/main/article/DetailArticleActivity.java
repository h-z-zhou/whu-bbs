package com.wuda.bbs.ui.main.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.bean.DetailArticleResponse;
import com.wuda.bbs.ui.adapter.DetailArticleRecyclerAdapter;
import com.wuda.bbs.ui.widget.TopicDecoration;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailArticleActivity extends AppCompatActivity {

    BriefArticle mBriefArticle;
    DetailArticleResponse detailArticleResponse;

    RecyclerView article_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        article_rv = findViewById(R.id.recyclerView);
        article_rv.setLayoutManager(new LinearLayoutManager(DetailArticleActivity.this));
        article_rv.setAdapter(new DetailArticleRecyclerAdapter(getBaseContext()));
        article_rv.addItemDecoration(new TopicDecoration(getBaseContext()));

        mBriefArticle = (BriefArticle) getIntent().getSerializableExtra("article");

        requestContentFromServer();

    }

    private void requestContentFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
//        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("GID", mBriefArticle.getGID());
        form.put("board", mBriefArticle.getBoardID());
        form.put("page", "1");
        Log.d("article", form.toString());
        mobileService.request("read", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
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

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}