package com.wuda.bbs.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.BoardListAdapter;
import com.wuda.bbs.ui.board.BoardActivity;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableRecyclerView;

public class BoardEntranceFragment extends Fragment {

    SwipeRefreshLayout entrance_srl;
    ExpandableRecyclerView entrance_erv;
    BoardEntranceViewModel mViewModel;

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
                mViewModel.requestDetailBoardsFromServer();
            }
        });
        entrance_erv = view.findViewById(R.id.boardEntrance_expandableRecyclerView);
        entrance_erv.setLayoutManager(new LinearLayoutManager(getContext()));

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BoardEntranceViewModel.class);

        mViewModel.getErrorResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                entrance_srl.setRefreshing(false);
                new ResponseErrorHandlerDialog(getContext())
                        .addErrorMsg(contentResponse.getResultCode(), contentResponse.getMassage())
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestDetailBoardsFromServer();
                            }
                        })
                        .show();
            }
        });

        mViewModel.getBoardListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<DetailBoard>>() {
            @Override
            public void onChanged(List<DetailBoard> detailBoards) {

                if (entrance_srl.isRefreshing()) {
                    entrance_srl.setRefreshing(false);
                }

                LinkedHashMap<String, List<BaseBoard>> allBoardGroupMap = new LinkedHashMap<>();

                for (DetailBoard detailBoard : detailBoards) {
                    String section = detailBoard.getSection();
                    if (!allBoardGroupMap.containsKey(section)) {
                        allBoardGroupMap.put(section, new ArrayList<>());
                    }
                    allBoardGroupMap.get(section).add(detailBoard);
                }

                List<String> sectionList = new ArrayList<>(allBoardGroupMap.keySet());
                List<List<BaseBoard>> allBoardGroupList = new ArrayList<>(allBoardGroupMap.values());

                BoardListAdapter adapter = new BoardListAdapter(getContext(), sectionList, allBoardGroupList);
                adapter.setOnBoardSelectedListener(new BoardListAdapter.OnBoardSelectedListener() {
                    @Override
                    public void onBoardSelected(BaseBoard board) {
                        Intent intent = new Intent(getContext(), BoardActivity.class);
                        intent.putExtra("board", board);
                        requireActivity().startActivity(intent);
                    }
                });

                entrance_erv.setAdapter(adapter);
            }
        });

        mViewModel.queryAllBoardFromDB();
    }
}