package com.wuda.bbs.ui.board;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textview.MaterialTextView;
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
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.DetailBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavorBoardManagerActivity extends AppCompatActivity {

    FavorBoardManagerViewModel mViewModel;
    RecyclerView board_rv;
    LinearLayout boardItem_ll;
//    boolean hadRequestFavor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(FavorBoardManagerViewModel.class);

        setContentView(R.layout.activity_favor_board_manager);

        boardItem_ll = findViewById(R.id.boardItemContainer_linearLayout);

        board_rv = findViewById(R.id.recyclerView);

        bindingEvent();

        if (BBSApplication.isLogin()) {
            requestFavorBoardsFromServer();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void bindingEvent() {

        // 完成收藏板块后获取所有板块
        mViewModel.favorBoards.observe(this, new Observer<List<BaseBoard>>() {
            @Override
            public void onChanged(List<BaseBoard> baseBoards) {
                queryDetailBoardsFromDB();
            }
        });

        mViewModel.allBoards.observe(this, new Observer<List<DetailBoard>>() {
            @Override
            public void onChanged(List<DetailBoard> detailBoardList) {

                boardItem_ll.removeAllViews();

                ChipGroup sectionGroup_cg = new ChipGroup(FavorBoardManagerActivity.this);
                MaterialTextView sectionLabel_tv = new MaterialTextView(FavorBoardManagerActivity.this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 16, 0, 16);
                int labelColor = getColor(R.color.colorPrimary);
                @SuppressLint("UseCompatLoadingForDrawables") Drawable labelIcon = getDrawable(R.drawable.ic_new_label);
                labelIcon.setBounds(0, 0, labelIcon.getMinimumWidth(), labelIcon.getMinimumHeight());

                String currentSection = "";
                for (int i=0; i<detailBoardList.size(); ++i) {
                    if (!detailBoardList.get(i).getSection().equals(currentSection)) {
                        currentSection = detailBoardList.get(i).getSection();

                        sectionGroup_cg = new ChipGroup(FavorBoardManagerActivity.this);
                        sectionLabel_tv = new MaterialTextView(FavorBoardManagerActivity.this);

                        sectionLabel_tv.setText(currentSection);
                        sectionLabel_tv.setTextSize(18);
                        sectionLabel_tv.setBackgroundColor(labelColor);
                        sectionLabel_tv.setCompoundDrawables(labelIcon, null, null, null);
                        sectionLabel_tv.setCompoundDrawablePadding(16);
                        sectionLabel_tv.setLayoutParams(params);
                        sectionLabel_tv.setPadding(0, 8, 0, 8);

                        boardItem_ll.addView(sectionLabel_tv);
                        boardItem_ll.addView(sectionGroup_cg);
                    }

                    Chip boardItemChip = new Chip(FavorBoardManagerActivity.this);
//                    boardItemChip.setRippleColor(getColorStateList(R.color.chip_bg_state));
                    boardItemChip.setChipBackgroundColor(getColorStateList(R.color.chip_bg_state));
                    boardItemChip.setCheckable(true);
                    boardItemChip.setText(detailBoardList.get(i).getName());
                    if (mViewModel.isFavorite(detailBoardList.get(i))) {
                        boardItemChip.setChecked(true);
                    }
                    int finalI = i;

                    boardItemChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            addOrRemoveFavor(detailBoardList.get(finalI), isChecked);
                        }
                    });

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

        NetworkEntry.requestDetailBoard(new DetailBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<DetailBoard>> response) {
                List<DetailBoard> detailBoardList = response.getContent();
                DetailBoardDao detailBoardDao = AppDatabase.getDatabase(FavorBoardManagerActivity.this).getDetailBoardDao();
                detailBoardDao.clearAll();
                // cast => save to database
                List<DetailBoard> castFavorBoardList = new ArrayList<>();
                for (int i=0; i<detailBoardList.size(); ++i) {
                    castFavorBoardList.add((DetailBoard) detailBoardList.get(i));
                }
                detailBoardDao.insert(castFavorBoardList);
            }
        });
    }

    private void queryFavorBoardsFromDB() {
        FavorBoardDao favorBoardDao = AppDatabase.getDatabase(this).getFavorBoardDao();
        if (BBSApplication.getAccountId().equals(""))
            return;

        List<BaseBoard> favorBoardList = favorBoardDao.loadFavorBoardByUsername(BBSApplication.getAccountId());

        mViewModel.favorBoards.setValue(favorBoardList);
    }

    private void requestFavorBoardsFromServer() {

        NetworkEntry.requestFavBoard(new FavBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavBoard>> response) {
                if (response.isSuccessful()) {
                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase(FavorBoardManagerActivity.this).getFavorBoardDao();
                    // 清空，与云端保持同步

                    favorBoardDao.clearFavorBoardsByUsername(BBSApplication.getAccountId());
                    favorBoardDao.insert(response.getContent());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryFavorBoardsFromDB();
                        }
                    });
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