package com.wuda.bbs.ui.drawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.util.Currency;
import android.os.Bundle;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.adapter.AboutAdapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;
import com.wuda.bbs.utils.ThemeManager;

public class AboutActivity extends CustomizedThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("关于");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.about_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AboutActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(AboutActivity.this, DividerItemDecoration.VERTICAL));

        recyclerView.setAdapter(new AboutAdapter(AboutActivity.this));
    }
}
