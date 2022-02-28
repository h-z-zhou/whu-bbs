package com.wuda.bbs.ui.board;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.FavBoard;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardViewModel extends ViewModel {
    MutableLiveData<List<FavBoard>> favorBoardList = new MutableLiveData<List<FavBoard>>();
    MutableLiveData<Integer> currentBoardIdx = new MutableLiveData<>();
//    MutableLiveData<List<Board>>
    // TODO: Implement the ViewModel

    public FavorBoardViewModel() {
        favorBoardList.setValue(new ArrayList<>());
        currentBoardIdx.setValue(-1);
    }
}