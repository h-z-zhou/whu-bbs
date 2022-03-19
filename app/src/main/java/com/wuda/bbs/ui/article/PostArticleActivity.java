package com.wuda.bbs.ui.article;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.ui.base.NavigationHost;

public class PostArticleActivity extends AppCompatActivity implements NavigationHost {

    Toolbar toolbar;
    PostBoardViewModel mPostBoardViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_article);

        mPostBoardViewModel = new ViewModelProvider(PostArticleActivity.this).get(PostBoardViewModel.class);

        BaseBoard board = (BaseBoard) getIntent().getSerializableExtra("board");
        if (board != null) {
            mPostBoardViewModel.boardMutableLiveData.postValue(board);
        }

        toolbar = findViewById(R.id.back_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigationTo(new PostArticleFragment(), false);

    }

    @Override
    public void navigationTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.post_article_container, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}