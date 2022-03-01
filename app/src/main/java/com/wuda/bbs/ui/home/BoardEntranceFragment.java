package com.wuda.bbs.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.wuda.bbs.ui.adapter.BoardListAdapter;
import com.wuda.bbs.ui.board.BoardActivity;
import com.wuda.bbs.utils.networkResponseHandler.DetailBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableRecyclerView;

public class BoardEntranceFragment extends Fragment {

    SwipeRefreshLayout entrance_srl;
    ExpandableRecyclerView entrance_erv;
    boolean isFavorBoardRequested = false;

    public BoardEntranceFragment() {
    }

    public static BoardEntranceFragment newInstance() {
        BoardEntranceFragment fragment = new BoardEntranceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_board_entrance, container, false);

        entrance_srl = view.findViewById(R.id.boardEntrance_swipeRefreshLayout);
        entrance_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestFavorBoardsFromServer();
                requestDetailBoardsFromServer();
            }
        });
        entrance_erv = view.findViewById(R.id.boardEntrance_expandableRecyclerView);
        entrance_erv.setLayoutManager(new LinearLayoutManager(getContext()));

        queryAllBoardFromDB();

        return  view;
    }

    private void queryAllBoardFromDB() {
        LinkedHashMap<String, List<BaseBoard>> allBoardGroupMap = new LinkedHashMap<>();

        if (getContext() == null) return;
        AppDatabase database = AppDatabase.getDatabase(getContext());

        FavorBoardDao favorBoardDao = database.getFavorBoardDao();
        List<FavBoard> favBoardList = favorBoardDao.loadAllFavorBoards();
        // Favor Board may empty
        if (favBoardList.isEmpty() && !isFavorBoardRequested) {
            requestFavorBoardsFromServer();
            isFavorBoardRequested = true;
            return;
        }
        allBoardGroupMap.put("我的收藏", cast2BaseBoard(favBoardList));

        DetailBoardDao detailBoardDao = database.getDetailBoardDao();
//        room group by ??
//        List<DetailBoard> detailBoardList = detailBoardDao.loadBoardsGroupBySection();
        List<DetailBoard> detailBoardList = detailBoardDao.loadAllBoards();
        if (detailBoardList.isEmpty()) {
            requestDetailBoardsFromServer();
            return;
        } else {
            for (DetailBoard detailBoard : detailBoardList) {
                String section = detailBoard.getSection();
                if (!allBoardGroupMap.containsKey(section)) {
                    allBoardGroupMap.put(section, new ArrayList<>());
                }
                allBoardGroupMap.get(section).add(detailBoard);
            }
        }
        database.close();

        List<String> sectionList = new ArrayList<>();
        sectionList.addAll(allBoardGroupMap.keySet());
        List<List<BaseBoard>> allBoardGroupList = new ArrayList<>();
        allBoardGroupList.addAll(allBoardGroupMap.values());

        BoardListAdapter adapter = new BoardListAdapter(getContext(), sectionList, allBoardGroupList);
        adapter.setOnBoardSelectedListener(new BoardListAdapter.OnBoardSelectedListener() {
            @Override
            public void onBoardSelected(BaseBoard board) {
                Intent intent = new Intent(getContext(), BoardActivity.class);
                intent.putExtra("board", board);
                getActivity().startActivity(intent);
            }
        });

        entrance_erv.setAdapter(adapter);

    }

    private void requestFavorBoardsFromServer() {

        entrance_srl.setRefreshing(true);

        NetworkEntry.requestFavBoard(new FavBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavBoard>> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrance_srl.setRefreshing(false);
                    }
                });
                if (response.isSuccessful()) {
                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase(getContext()).getFavorBoardDao();
                    // 清空，与云端保持同步
                    favorBoardDao.clearFavorBoardsByUsername(BBSApplication.getAccountId());
                    favorBoardDao.insert(response.getContent());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryAllBoardFromDB();
                        }
                    });
                }
            }
        });
    }


    private void requestDetailBoardsFromServer() {

        entrance_srl.setRefreshing(true);

        NetworkEntry.requestDetailBoard(new DetailBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<DetailBoard>> response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        entrance_srl.setRefreshing(false);
                    }
                });

                List<DetailBoard> detailBoardList = response.getContent();
                DetailBoardDao detailBoardDao = AppDatabase.getDatabase(getContext()).getDetailBoardDao();
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

    private List<BaseBoard> cast2BaseBoard(List<?> boards) {
        // FavorBoard / DetailBoard => BaseBoard
        List<BaseBoard> baseBoardList = new ArrayList<>();
        for (Object board: boards) {
            baseBoardList.add((BaseBoard) board);
        }
        return baseBoardList;
    }
}