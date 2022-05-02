package com.wuda.bbs.ui.drawer;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.History;
import com.wuda.bbs.ui.adapter.HistoryAdapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends CustomizedThemeActivity {

    RecyclerView history_rv;
    HistoryAdapter adapter;

    HistoryViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("阅读历史");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        history_rv = findViewById(R.id.recyclerView);
        history_rv.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        history_rv.addItemDecoration(new DividerItemDecoration(HistoryActivity.this, DividerItemDecoration.VERTICAL));

        adapter = new HistoryAdapter(HistoryActivity.this, new ArrayList<>());
        adapter.setMore(false);
        history_rv.setAdapter(adapter);

        mViewModel = new ViewModelProvider(HistoryActivity.this).get(HistoryViewModel.class);
        mViewModel.getHistoryListMutableLiveData().observe(HistoryActivity.this, new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                adapter.setContents(histories);
            }
        });
        mViewModel.loadHistory();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_history_clear) {
            mViewModel.clearHistory();
            adapter.setContents(new ArrayList<>());
        }
        return super.onOptionsItemSelected(item);
    }

}