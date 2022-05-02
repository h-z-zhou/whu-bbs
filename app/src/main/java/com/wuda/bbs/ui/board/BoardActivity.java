package com.wuda.bbs.ui.board;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;

public class BoardActivity extends CustomizedThemeActivity {

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

        BoardArticleFabFragment boardArticleFabFragment = new BoardArticleFabFragment();
        boardArticleFabFragment.setBoard(board);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.board_article_fragment_container, boardArticleFabFragment)
                .commit();
    }
}