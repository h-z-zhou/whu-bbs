package com.wuda.bbs.ui.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.DetailBoardDao;
import com.wuda.bbs.ui.adapter.BoardListAdapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableRecyclerView;

public class SelectBoardActivity extends CustomizedThemeActivity {

    ExpandableRecyclerView board_erv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_board);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("版块选择");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        board_erv = findViewById(R.id.selectBoard_expandableRecyclerView);

        LinkedHashMap<String, List<BaseBoard>> allBoardGroupMap = new LinkedHashMap<>();

        DetailBoardDao detailBoardDao = AppDatabase.getDatabase().getDetailBoardDao();
//        room group by ??
//        List<DetailBoard> detailBoardList = detailBoardDao.loadBoardsGroupBySection();
        List<DetailBoard> detailBoardList = detailBoardDao.loadAllBoards();
        for (DetailBoard detailBoard : detailBoardList) {
            String section = detailBoard.getSection();
            if (!allBoardGroupMap.containsKey(section)) {
                allBoardGroupMap.put(section, new ArrayList<>());
            }

            allBoardGroupMap.get(section).add(detailBoard);
        }

        List<String> sectionList = new ArrayList<>(allBoardGroupMap.keySet());
        List<List<BaseBoard>> allBoardGroupList = new ArrayList<>(allBoardGroupMap.values());

        BoardListAdapter adapter = new BoardListAdapter(SelectBoardActivity.this, sectionList, allBoardGroupList);
        adapter.setOnBoardSelectedListener(new BoardListAdapter.OnBoardSelectedListener() {
            @Override
            public void onBoardSelected(BaseBoard board) {
                Intent intent = new Intent();
                intent.putExtra("board", board);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        board_erv.setAdapter(adapter);
        board_erv.setLayoutManager(new LinearLayoutManager(SelectBoardActivity.this));
    }
}