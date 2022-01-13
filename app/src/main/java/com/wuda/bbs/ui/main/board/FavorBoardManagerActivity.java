package com.wuda.bbs.ui.main.board;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textview.MaterialTextView;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.DetailBoard;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.DetailBoardDao;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavorBoardManagerActivity extends AppCompatActivity {

    FavorBoardManagerViewModel mViewModel;
    RecyclerView board_rv;
    LinearLayout boardItem_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(FavorBoardManagerViewModel.class);

        setContentView(R.layout.activity_favor_board_manager);

        boardItem_ll = findViewById(R.id.boardItemContainer_linearLayout);

        board_rv = findViewById(R.id.recyclerView);

        bindingEvent();

        queryDetailBoardsFromDB();

    }

    private void bindingEvent() {
        mViewModel.allBoards.observe(this, new Observer<List<DetailBoard>>() {
            @Override
            public void onChanged(List<DetailBoard> detailBoardList) {

                boardItem_ll.removeAllViews();

                ChipGroup sectionGroup_cg = new ChipGroup(FavorBoardManagerActivity.this);
                MaterialTextView sectionLabel_tv = new MaterialTextView(FavorBoardManagerActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                String currentSection = "";
                for (int i=0; i<detailBoardList.size(); ++i) {
                    if (!detailBoardList.get(i).getSection().equals(currentSection)) {
                        currentSection = detailBoardList.get(i).getSection();

                        sectionGroup_cg = new ChipGroup(FavorBoardManagerActivity.this);
                        sectionLabel_tv = new MaterialTextView(FavorBoardManagerActivity.this);

                        sectionLabel_tv.setText(currentSection);
                        sectionLabel_tv.setTextSize(24);
                        sectionLabel_tv.setBackgroundColor(Color.GRAY);

                        boardItem_ll.addView(sectionLabel_tv);
                        boardItem_ll.addView(sectionGroup_cg);
                    }

                    Chip boardItemChip = new Chip(FavorBoardManagerActivity.this);
                    boardItemChip.setText(detailBoardList.get(i).getName());

                    sectionGroup_cg.addView(boardItemChip);
                }

            }
        });
    }

    private void queryDetailBoardsFromDB() {
        DetailBoardDao detailBoardDao = AppDatabase.getDatabase(getApplicationContext()).getDetailBoardDao();

        List<DetailBoard> detailBoardList = detailBoardDao.loadAllBoards();

        if (detailBoardList.isEmpty()) {
            requestDetailBoardsFromServer();
        } else {
            mViewModel.allBoards.setValue(detailBoardList);
        }

    }

    private void requestDetailBoardsFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        mobileService.request("boards", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = response.body().string();

                    List<BaseBoard> detailBoardList = XMLParser.parseDetailBoard(text);
                    if (getApplicationContext() == null)
                        return;

                    DetailBoardDao detailBoardDao = AppDatabase.getDatabase(getApplicationContext()).getDetailBoardDao();
                    detailBoardDao.clearAll();
                    // cast => save to database
                    List<DetailBoard> castFavorBoardList = new ArrayList<>();
                    for (int i=0; i<detailBoardList.size(); ++i) {
                        castFavorBoardList.add((DetailBoard) detailBoardList.get(i));
                    }
                    detailBoardDao.insert(castFavorBoardList);

                    queryDetailBoardsFromDB();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}