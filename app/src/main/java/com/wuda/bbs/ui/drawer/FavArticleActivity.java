package com.wuda.bbs.ui.drawer;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.FavArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.FavArticleAdapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

import java.util.ArrayList;
import java.util.List;

public class FavArticleActivity extends CustomizedThemeActivity {

    FavArticleViewModel mViewModel;

    RecyclerView favArticle_rv;
    FavArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_article);

        mViewModel = new ViewModelProvider(FavArticleActivity.this).get(FavArticleViewModel.class);

        Toolbar toolbar = findViewById(R.id.fav_article_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        favArticle_rv = findViewById(R.id.recyclerView);
        favArticle_rv.setLayoutManager(new LinearLayoutManager(FavArticleActivity.this));

        adapter = new FavArticleAdapter(FavArticleActivity.this, new ArrayList<>());
        favArticle_rv.setAdapter(adapter);

        favArticle_rv.addItemDecoration(new DividerItemDecoration(FavArticleActivity.this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                final int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAbsoluteAdapterPosition();
                FavArticle favArticle = adapter.removeItem(position);
                if (favArticle != null) {
                    mViewModel.removeFavArticle(favArticle);
                }
                // 防止 Footer 被移除
                adapter.updateFooter();
            }
        });
        mItemTouchHelper.attachToRecyclerView(favArticle_rv);

        eventBinding();

        mViewModel.requestFavArticleFromServer();

    }

    private void eventBinding() {
        mViewModel.getFavArticleMutableLiveData().observe(FavArticleActivity.this, new Observer<List<FavArticle>>() {
            @Override
            public void onChanged(List<FavArticle> favArticles) {
                adapter.setContents(favArticles);
                adapter.setMore(false);
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(FavArticleActivity.this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(FavArticleActivity.this)
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestFavArticleFromServer();
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
    }
}