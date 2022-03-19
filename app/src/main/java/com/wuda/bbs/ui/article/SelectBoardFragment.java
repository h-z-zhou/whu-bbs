package com.wuda.bbs.ui.article;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.DetailBoardDao;
import com.wuda.bbs.ui.adapter.BoardListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableRecyclerView;

public class SelectBoardFragment extends Fragment {

    ExpandableRecyclerView board_erv;

    private PostBoardViewModel mPostBoardViewModel;

    public static SelectBoardFragment newInstance() {
        return new SelectBoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.select_board_fragment, container, false);

        board_erv = view.findViewById(R.id.selectBoard_expandableRecyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPostBoardViewModel = new ViewModelProvider(requireActivity()).get(PostBoardViewModel.class);
        LinkedHashMap<String, List<BaseBoard>> allBoardGroupMap = new LinkedHashMap<>();

        AppDatabase database = AppDatabase.getDatabase(getContext());
        DetailBoardDao detailBoardDao = database.getDetailBoardDao();
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
        database.close();

        List<String> sectionList = new ArrayList<>(allBoardGroupMap.keySet());
        List<List<BaseBoard>> allBoardGroupList = new ArrayList<>(allBoardGroupMap.values());

        BoardListAdapter adapter = new BoardListAdapter(getContext(), sectionList, allBoardGroupList);
        adapter.setOnBoardSelectedListener(new BoardListAdapter.OnBoardSelectedListener() {
            @Override
            public void onBoardSelected(BaseBoard board) {
                mPostBoardViewModel.boardMutableLiveData.postValue(board);
                requireActivity().onBackPressed();
            }
        });

        board_erv.setAdapter(adapter);
        board_erv.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}