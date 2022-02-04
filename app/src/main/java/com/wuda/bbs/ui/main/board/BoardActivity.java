package com.wuda.bbs.ui.main.board;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.BaseBoard;

public class BoardActivity extends AppCompatActivity {

    BaseBoard board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        board = (BaseBoard) getIntent().getSerializableExtra("board");

        if (board == null) {
            finish();
        }

        Toolbar toolbar = findViewById(R.id.board_toolbar);
        toolbar.setTitle(board.getName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BoardArticleFragment boardArticleFragment = new BoardArticleFragment();
        boardArticleFragment.setBoard(board);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.board_article_fragment_container, boardArticleFragment)
                .commit();
    }
}