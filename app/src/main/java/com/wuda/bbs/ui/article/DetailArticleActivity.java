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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.bbs.DetailArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.DetailArticleAdapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.ui.widget.TopicDecoration;
import com.wuda.bbs.utils.network.NetConst;

import java.util.List;

public class DetailArticleActivity extends CustomizedThemeActivity {

    DetailArticleViewModel mViewModel;

    Toolbar toolbar;
    RecyclerView article_rv;
    DetailArticleAdapter articleAdapter;

    TextView reply_tv;
    MotionEvent mTouchEvent;

    ActivityResultLauncher<Intent> resultLauncher;

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

        toolbar = findViewById(R.id.back_toolbar);

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        article_rv = findViewById(R.id.shared_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DetailArticleActivity.this);
        article_rv.setLayoutManager(linearLayoutManager);
        articleAdapter = new DetailArticleAdapter(
                DetailArticleActivity.this,
                mViewModel.getBriefArticle().getGID(),
                mViewModel.getBriefArticle().getBoardID()
        );
        articleAdapter.setTitle(title);

        article_rv.setAdapter(articleAdapter);
        article_rv.addItemDecoration(new TopicDecoration(DetailArticleActivity.this));

        article_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pos = linearLayoutManager.findFirstVisibleItemPosition();
                ActionBar actionBar = getSupportActionBar();
                assert actionBar != null;
                actionBar.setDisplayShowTitleEnabled(pos != 0);
            }
        });



        reply_tv = findViewById(R.id.detailArticle_reply_textView);

        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    mViewModel.requestContentFromServer();
                }
            }
        });

        eventBinding();

        mViewModel.requestContentFromServer();
//        mViewModel.requestArticleTreeFromServer();

        assert briefArticle != null;
        if (briefArticle.getReID() == null) {
            // 从回文转寄跳转时缺重要数据（标题，作者等）
            mViewModel.add2history();
        }
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
        } else if (item.getItemId() == R.id.menu_share) {
            BriefArticle briefArticle = mViewModel.getBriefArticle();
            String webUrl = NetConst.BASE + "wForum/disparticle.php?" +
                    "boardName=" + briefArticle.getBoardID() +
                    "&ID=" + briefArticle.getGID();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, "珞珈山水:\"" + briefArticle.getTitle() + "\" " +  webUrl);
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventBinding() {

        mViewModel.getDetailArticlesMutableLiveData().observe(DetailArticleActivity.this, new Observer<List<DetailArticle>>() {
            @Override
            public void onChanged(List<DetailArticle> detailArticles) {
                articleAdapter.updateDataSet(detailArticles);
                reply_tv.setVisibility(View.VISIBLE);
                if (mViewModel.getBriefArticle().getReID() != null) {
                    int pos = articleAdapter.reID2Pos(mViewModel.getBriefArticle().getReID());
                    article_rv.scrollToPosition(pos);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            View itemView = article_rv.getLayoutManager().findViewByPosition(0);
//                            ValueAnimator animator = ObjectAnimator.ofInt(article_rv, "backgroundColor", 0x00ff0000, 0x6600ff00);//对背景色颜色进行改变，操作的属性为"backgroundColor",此处必须这样写，不能全小写,后面的颜色为在对应颜色间进行渐变
//                            animator.setDuration(3000);
//                            animator.setEvaluator(new ArgbEvaluator());//如果要颜色渐变必须要ArgbEvaluator，来实现颜色之间的平滑变化，否则会出现颜色不规则跳动
//                            animator.start();
//                        }
//                    }, 200);
                }
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
//                startActivity(intent);
                resultLauncher.launch(intent);
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