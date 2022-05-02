package com.wuda.bbs.ui.article;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;

public class SelectContentActivity extends CustomizedThemeActivity {

    TextView content_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_content);
        content_tv = findViewById(R.id.select_content_textView);

        String content = getIntent().getStringExtra("content");
        if (content == null) {
            finish();
        }

        content_tv.setText(content);
    }
}