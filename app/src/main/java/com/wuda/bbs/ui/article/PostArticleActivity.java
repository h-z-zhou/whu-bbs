package com.wuda.bbs.ui.article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.ui.base.NavigationHost;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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