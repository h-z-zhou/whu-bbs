package com.wuda.bbs.ui.main.board;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.FavorBoard;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardViewModel extends ViewModel {
    MutableLiveData<List<BaseBoard>> favorBoardList = new MutableLiveData<List<BaseBoard>>();
    MutableLiveData<Integer> currentBoardIdx = new MutableLiveData<>(-1);
//    MutableLiveData<List<Board>>
    // TODO: Implement the ViewModel

    public FavorBoardViewModel() {
        favorBoardList.setValue(new ArrayList<>());
    }
}