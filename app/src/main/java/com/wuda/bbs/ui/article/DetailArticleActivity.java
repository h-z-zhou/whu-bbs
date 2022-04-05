package com.wuda.bbs.ui.article;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.DetailArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.DetailArticleAdapter;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.ui.widget.TopicDecoration;

import java.util.List;

public class DetailArticleActivity extends AppCompatActivity {

    DetailArticleViewModel mViewModel;

    Toolbar toolbar;
    RecyclerView article_rv;
    DetailArticleAdapter articleAdapter;

    TextView reply_tv;
    MotionEvent mTouchEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        BriefArticle briefArticle = (BriefArticle) getIntent().getSerializableExtra("briefArticle");
        mViewModel = new ViewModelProvider(DetailArticleActivity.this).get(DetailArticleViewModel.class);
        if (briefArticle != null) {
            mViewModel.setBriefArticle(briefArticle);
        } else if(mViewModel.getBriefArticle() == null) {
            finish();
        }

        article_rv = findViewById(R.id.detailArticle_recyclerView);
        article_rv.setLayoutManager(new LinearLayoutManager(DetailArticleActivity.this));
        articleAdapter = new DetailArticleAdapter(
                DetailArticleActivity.this,
                mViewModel.getBriefArticle().getGID(),
                mViewModel.getBriefArticle().getBoardID()
        );

        article_rv.setAdapter(articleAdapter);
        article_rv.addItemDecoration(new TopicDecoration(DetailArticleActivity.this));

        toolbar = findViewById(R.id.detailArticle_toolbar);

        String title = mViewModel.getBriefArticle().getTitle();
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

        reply_tv = findViewById(R.id.detailArticle_reply_textView);

        eventBinding();

        mViewModel.requestContentFromServer();
        mViewModel.add2history();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.detail_article_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_favor) {
            mViewModel.add2Favor();
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventBinding() {

        mViewModel.getDetailArticlesMutableLiveData().observe(DetailArticleActivity.this, new Observer<List<DetailArticle>>() {
            @Override
            public void onChanged(List<DetailArticle> detailArticles) {
                articleAdapter.updateDataSet(detailArticles);
            }
        });

        mViewModel.getFavoriteMutableLiveData().observe(DetailArticleActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(DetailArticleActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(DetailArticleActivity.this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(DetailArticleActivity.this)
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestContentFromServer();
                            }
                        })
                        .setOnNegativeButtonClickedLister(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                finish();
                            }
                        })
                        .show();
            }
        });

        articleAdapter.setItemListener(new AdapterItemListener<DetailArticle>() {
            @Override
            public void onItemClick(DetailArticle data, int position) {
                Intent intent = new Intent(DetailArticleActivity.this, ReplyActivity.class);
                intent.putExtra("article", data);
                intent.putExtra("groupId", mViewModel.getBriefArticle().getGID());
                intent.putExtra("boardId", mViewModel.getBriefArticle().getBoardID());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(DetailArticle data, int position) {
                ArticleLongClickBottomSheet articleLongClickBottomSheet = new ArticleLongClickBottomSheet();
                articleLongClickBottomSheet.setArticle(data);
                articleLongClickBottomSheet.show(getSupportFragmentManager(), ArticleLongClickBottomSheet.class.getSimpleName());
            }
        });

        reply_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailArticleActivity.this, ReplyActivity.class);
                DetailArticle article = new DetailArticle();
                BriefArticle mBriefArticle = mViewModel.getBriefArticle();
                article.setId(mBriefArticle.getGID());
                article.setAuthor(mBriefArticle.getAuthor());
                article.setContent(mBriefArticle.getTitle());
//                article.setAuthor();
                intent.putExtra("article", article);
                intent.putExtra("groupId", mBriefArticle.getGID());
                intent.putExtra("boardId", mBriefArticle.getBoardID());
                startActivity(intent);
            }
        });

        // 取消收藏无法绑定
//        favor_iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isFavor = !isFavor;
//                if (isFavor) {
//                    favor_iv.setImageDrawable(getDrawable(R.drawable.ic_favor_filled));
//                    favor_iv.setColorFilter(Color.RED);
//                    addFavor();
//                } else {
//                    favor_iv.setImageDrawable(getDrawable(R.drawable.ic_favor));
//                    favor_iv.setColorFilter(null);
//                }
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mTouchEvent = ev;
        }
        return super.dispatchTouchEvent(ev);
    }
}