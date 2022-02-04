package com.wuda.bbs.ui.main.post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.BaseBoard;

public class WriteArticleActivity extends AppCompatActivity {

    BaseBoard board;
    Toolbar toolbar;
    AutoCompleteTextView boardName_atv;
    TextInputEditText title_et;
    TextInputEditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_article);

        toolbar = findViewById(R.id.writeArticle_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        boardName_atv = findViewById(R.id.writeArticle_boardName_autoCompleteTextView);
        title_et = findViewById(R.id.writeArticle_title_editText);
        content_et = findViewById(R.id.writeArticle_content_editText);

        board = (BaseBoard) getIntent().getSerializableExtra("board");

        if (board != null) {
            boardName_atv.setText(board.getName());
        }
    }
}