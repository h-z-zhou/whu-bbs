package com.wuda.bbs.ui.drawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.ColorThemeAdapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;
import com.wuda.bbs.utils.ThemeManager;

public class ColorThemeActivity extends CustomizedThemeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_theme);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("主题");;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ColorThemeActivity.this));
        ColorThemeAdapter adapter = new ColorThemeAdapter(this);

        adapter.setItemListener(new AdapterItemListener<ThemeManager.ColorThemeItem>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(ThemeManager.ColorThemeItem data, int position) {
                ThemeManager.saveColorTheme(data.getName());
                // 无效？？？
                setResult(RESULT_OK);

                adapter.notifyDataSetChanged();
                recreate();
            }

            @Override
            public void onItemLongClick(ThemeManager.ColorThemeItem data, int position) {

            }
        });

        recyclerView.setAdapter(adapter);
    }
}