package com.wuda.bbs.ui.board;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.BaseBoard;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardViewModel extends ViewModel {
    MutableLiveData<List<BaseBoard>> favorBoardList = new MutableLiveData<List<BaseBoard>>();
    MutableLiveData<Integer> currentBoardIdx = new MutableLiveData<>();
//    MutableLiveData<List<Board>>
    // TODO: Implement the ViewModel

    public FavorBoardViewModel() {
        favorBoardList.setValue(new ArrayList<>());
        currentBoardIdx.setValue(-1);
    }
}