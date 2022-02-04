package com.wuda.bbs.ui.main.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.DetailBoard;
import com.wuda.bbs.bean.FavorBoard;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.DetailBoardDao;
import com.wuda.bbs.dao.FavorBoardDao;
import com.wuda.bbs.ui.adapter.BoardEntranceListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class BoardEntranceFragment extends Fragment {

    ExpandableListView entrance_elv;

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

        entrance_elv = view.findViewById(R.id.boardEntrance_expandableListView);

        LinkedHashMap<String, List<BaseBoard>> allBoardGroupMap = queryAllBoardFromDB();
        List<String> sectionList = new ArrayList<>();
        sectionList.addAll(allBoardGroupMap.keySet());
        List<List<BaseBoard>> allBoardGroupList = new ArrayList<>();
        allBoardGroupList.addAll(allBoardGroupMap.values());

        entrance_elv.setAdapter(new BoardEntranceListAdapter(getContext(), sectionList, allBoardGroupList));

        return  view;
    }

    private LinkedHashMap<String, List<BaseBoard>> queryAllBoardFromDB() {
        LinkedHashMap<String, List<BaseBoard>> allBoardGroupMap = new LinkedHashMap<>();

        AppDatabase database = AppDatabase.getDatabase(getContext());

        FavorBoardDao favorBoardDao = database.getFavorBoardDao();
        List<FavorBoard> favorBoardList = favorBoardDao.loadAllFavorBoards();
        allBoardGroupMap.put("我的收藏", cast2BaseBoard(favorBoardList));

        DetailBoardDao detailBoardDao = database.getDetailBoardDao();
//        room group by ??
//        List<DetailBoard> detailBoardList = detailBoardDao.loadBoardsGroupBySection();
        List<DetailBoard> detailBoardList = detailBoardDao.loadAllBoards();
        for (DetailBoard detailBoard: detailBoardList) {
            String section = detailBoard.getSection();
            if (!allBoardGroupMap.containsKey(section)) {
                allBoardGroupMap.put(section, new ArrayList<>());
            }
            allBoardGroupMap.get(section).add(detailBoard);
        }


        database.close();
        return allBoardGroupMap;
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