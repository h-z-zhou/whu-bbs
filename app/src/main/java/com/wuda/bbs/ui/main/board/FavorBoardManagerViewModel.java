package com.wuda.bbs.ui.main.board;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.DetailBoard;
import com.wuda.bbs.bean.FavorBoard;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardManagerViewModel extends ViewModel {
    MutableLiveData<List<DetailBoard>> allBoards;
    MutableLiveData<List<FavorBoard>> favorBoards;
    // TODO: Implement the ViewModel

    public FavorBoardManagerViewModel() {
        allBoards = new MutableLiveData<>();
        allBoards.setValue(new ArrayList<>());

        favorBoards = new MutableLiveData<>();
        favorBoards.setValue(new ArrayList<>());
    }
}