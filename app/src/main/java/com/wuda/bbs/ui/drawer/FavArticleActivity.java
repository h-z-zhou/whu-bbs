package com.wuda.bbs.ui.drawer;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.FavArticle;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.FavArticleAdapter;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.networkResponseHandler.FavArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavArticleActivity extends AppCompatActivity {

    RecyclerView favArticle_rv;
    FavArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_article);

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
//                return 0;
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
                int position = viewHolder.getAdapterPosition();
//                FavArticleAdapter adapter = (FavArticleAdapter) favArticle_rv.getAdapter();
//                if (adapter == null)
//                    return;
                FavArticle favArticle = adapter.removeItem(position);
                if (favArticle != null) {
                    removeFavArticle(favArticle);
                }
                // 防止 Footer 被移除
                adapter.updateFooter();
            }
        });
        mItemTouchHelper.attachToRecyclerView(favArticle_rv);

        requestFavArticleFromServer();

    }

    private void requestFavArticleFromServer() {

        NetworkEntry.requestFavArticle(new FavArticleHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavArticle>> response) {
                if (response.isSuccessful()) {
                    adapter.setContents(response.getContent());
                    adapter.setMore(false);
                }
            }
        });
    }

    public void removeFavArticle(FavArticle favArticle) {

        Map<String, String> form = NetTool.extractUrlParam(favArticle.getDelUrl());
        NetworkEntry.removeFavArticle(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FavArticleActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}