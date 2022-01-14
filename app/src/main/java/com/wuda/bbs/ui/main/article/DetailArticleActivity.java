package com.wuda.bbs.ui.main.article;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailArticleActivity extends AppCompatActivity {

    BriefArticle mBriefArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

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
        mobileService.request("read", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.d("content", response.body().string());
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