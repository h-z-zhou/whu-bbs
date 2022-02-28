package com.wuda.bbs.ui.board;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.bean.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.DetailBoardDao;
import com.wuda.bbs.logic.dao.FavorBoardDao;
import com.wuda.bbs.ui.adapter.FavBoardSelectorAdapter;
import com.wuda.bbs.utils.networkResponseHandler.DetailBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pokercc.android.expandablerecyclerview.ExpandableRecyclerView;

public class FavorBoardManagerActivity extends AppCompatActivity {

    FavorBoardManagerViewModel mViewModel;
    ExpandableRecyclerView board_erv;
    FavBoardSelectorAdapter adapter;
    boolean isFavBoardRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(FavorBoardManagerViewModel.class);

        setContentView(R.layout.activity_favor_board_manager);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("板块管理");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        board_erv = findViewById(R.id.favBoardManager_expandableRecyclerView);
        board_erv.setLayoutManager(new LinearLayoutManager(FavorBoardManagerActivity.this));

        eventBinding();

        if (BBSApplication.isLogin()) {
            requestFavBoardsFromServer();
        } else {
            new AlertDialog.Builder(FavorBoardManagerActivity.this)
                    .setTitle("未登录")
                    .setMessage("请先完成登录")
                    .setCancelable(false)
                    .setPositiveButton("登录", null)
                    .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onBackPressed();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void eventBinding() {

        // 完成收藏板块后获取所有板块
        mViewModel.favBoards.observe(this, new Observer<List<FavBoard>>() {
            @Override
            public void onChanged(List<FavBoard> favBoards) {
                queryDetailBoardsFromDB();
            }
        });

        mViewModel.allBoards.observe(this, new Observer<List<DetailBoard>>() {
            @Override
            public void onChanged(List<DetailBoard> detailBoardList) {

                LinkedHashMap<String, List<Pair<BaseBoard, Boolean>>> allBoardGroupMap = new LinkedHashMap<>();
                for (DetailBoard board: detailBoardList) {
                    String section = board.getSection();
                    if (!allBoardGroupMap.containsKey(section)) {
                        allBoardGroupMap.put(section, new ArrayList<>());
                    }
                    allBoardGroupMap.get(section).add(new Pair<>(board, mViewModel.isFavorite(board)));
                }

                List<String> sectionList = new ArrayList<>();
                sectionList.addAll(allBoardGroupMap.keySet());
                List<List<Pair<BaseBoard, Boolean>>> allBoardGroupList = new ArrayList<>();
                allBoardGroupList.addAll(allBoardGroupMap.values());

                adapter = new FavBoardSelectorAdapter(FavorBoardManagerActivity.this, sectionList, allBoardGroupList);

                adapter.setOnBoardFavChangedListener(new FavBoardSelectorAdapter.OnBoardFavChangedListener() {
                    @Override
                    public void onBoardFavChanged(BaseBoard board, boolean isFav) {
                        if (board instanceof DetailBoard) {
                            addOrRemoveFavor((DetailBoard) board, isFav);
                        }
                    }
                });

                board_erv.setAdapter(adapter);
            }
        });
    }

    private void queryDetailBoardsFromDB() {
        DetailBoardDao detailBoardDao = AppDatabase.getDatabase(getApplicationContext()).getDetailBoardDao();

        List<DetailBoard> detailBoardList = detailBoardDao.loadAllBoards();

        if (detailBoardList.isEmpty()) {
            requestDetailBoardsFromServer();
        } else {
            mViewModel.allBoards.postValue(detailBoardList);
        }

    }

    private void requestDetailBoardsFromServer() {

        NetworkEntry.requestDetailBoard(new DetailBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<DetailBoard>> response) {
                List<DetailBoard> detailBoardList = response.getContent();
                DetailBoardDao detailBoardDao = AppDatabase.getDatabase(FavorBoardManagerActivity.this).getDetailBoardDao();
                detailBoardDao.clearAll();
                detailBoardDao.insert(detailBoardList);
            }
        });
    }

    private void queryFavorBoardsFromDB() {
        FavorBoardDao favorBoardDao = AppDatabase.getDatabase(this).getFavorBoardDao();
        if (BBSApplication.getAccountId().equals(""))
            return;

        List<FavBoard> favorBoardList = favorBoardDao.loadFavorBoardByUsername(BBSApplication.getAccountId());

        mViewModel.favBoards.postValue(favorBoardList);
    }

    private void requestFavBoardsFromServer() {

        NetworkEntry.requestFavBoard(new FavBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavBoard>> response) {
                if (response.isSuccessful()) {
                    isFavBoardRequested = true;

                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase(FavorBoardManagerActivity.this).getFavorBoardDao();
                    // 清空，与云端保持同步

                    favorBoardDao.clearFavorBoardsByUsername(BBSApplication.getAccountId());
                    favorBoardDao.insert(response.getContent());
                    queryFavorBoardsFromDB();
                }
            }
        });

    }

    private void addOrRemoveFavor(DetailBoard board, boolean isAdd) {
        Map<String, String> form = new HashMap<>();
        FavorBoardDao favorBoardDao = AppDatabase.getDatabase(this).getFavorBoardDao();
        if (isAdd) {
            form.put("bname", board.getId());
            favorBoardDao.insert(new FavBoard(board.getId(), board.getName(), BBSApplication.getAccountId()));
        } else {
            // 编号减一
            form.put("delete", Integer.valueOf(Integer.parseInt(board.getNumber()) - 1).toString());
            favorBoardDao.delete(board.getId());
        }
        form.put("select", "0");

        NetworkEntry.operateFavBoard(form, new SimpleResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Object> response) {
                if (response.isSuccessful()) {
                    String text = response.getMassage();
                }
            }
        });
    }

}