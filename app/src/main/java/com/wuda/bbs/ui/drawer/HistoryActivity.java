package com.wuda.bbs.ui.drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.History;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.HistoryDao;
import com.wuda.bbs.ui.adapter.HistoryRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView history_rv;
    HistoryRecyclerAdapter history_adapter;

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
        history_adapter = new HistoryRecyclerAdapter(HistoryActivity.this, new ArrayList<>());
        history_rv.setAdapter(history_adapter);
        history_rv.addItemDecoration(new DividerItemDecoration(HistoryActivity.this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
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
                History history = history_adapter.removeItem(viewHolder.getAdapterPosition());
                removeHistory(history);
            }
        });
        itemTouchHelper.attachToRecyclerView(history_rv);

        loadHistory();
    }

    private void loadHistory() {
        HistoryDao historyDao = AppDatabase.getDatabase(HistoryActivity.this).getHistoryDao();
        List<History> historyList = historyDao.loadAllHistories();
        history_adapter.addHistories(historyList);
    }

    private void removeHistory(History history) {
        HistoryDao historyDao = AppDatabase.getDatabase(HistoryActivity.this).getHistoryDao();
        historyDao.deleteHistory(history.id);
    }
}