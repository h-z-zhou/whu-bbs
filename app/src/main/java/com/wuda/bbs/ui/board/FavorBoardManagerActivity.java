package com.wuda.bbs.ui.board;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.bean.bbs.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.FavBoardSelectorAdapter;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableRecyclerView;

public class FavorBoardManagerActivity extends AppCompatActivity {

    FavorBoardManagerViewModel mViewModel;
    ExpandableRecyclerView board_rv;
    FavBoardSelectorAdapter adapter;

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

        board_rv = findViewById(R.id.favBoardManager_recyclerView);
        board_rv.setLayoutManager(new LinearLayoutManager(FavorBoardManagerActivity.this));

        eventBinding();

        mViewModel.requestFavBoardsFromServer();
    }

    private void eventBinding() {

        // 完成收藏板块后获取所有板块
        mViewModel.getFavBoardsMutableLiveData().observe(this, new Observer<List<FavBoard>>() {
            @Override
            public void onChanged(List<FavBoard> favBoards) {
                mViewModel.requestDetailBoardsFromServer();
            }
        });

        mViewModel.getAllBoardsMutableLiveData().observe(this, new Observer<List<DetailBoard>>() {
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

                List<String> sectionList = new ArrayList<>(allBoardGroupMap.keySet());
                List<List<Pair<BaseBoard, Boolean>>> allBoardGroupList = new ArrayList<>(allBoardGroupMap.values());

                adapter = new FavBoardSelectorAdapter(FavorBoardManagerActivity.this, sectionList, allBoardGroupList);

                adapter.setOnBoardFavChangedListener(new FavBoardSelectorAdapter.OnBoardFavChangedListener() {
                    @Override
                    public void onBoardFavChanged(BaseBoard board, boolean isFav) {
                        if (board instanceof DetailBoard) {
                            if (isFav) {
                                mViewModel.add2Favor((DetailBoard) board);
                            } else {
                                mViewModel.remove2Favor((DetailBoard) board);
                            }
                        }
                    }
                });

                board_rv.setAdapter(adapter);
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(FavorBoardManagerActivity.this)
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestFavBoardsFromServer();
                            }
                        })
                        .show();
            }
        });

    }


}