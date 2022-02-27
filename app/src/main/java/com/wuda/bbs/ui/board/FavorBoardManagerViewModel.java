package com.wuda.bbs.ui.board;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.DetailBoard;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardManagerViewModel extends ViewModel {

    MutableLiveData<List<DetailBoard>> allBoards;
    MutableLiveData<List<BaseBoard>> favorBoards;
    // TODO: Implement the ViewModel

    public FavorBoardManagerViewModel() {
        allBoards = new MutableLiveData<>();
        allBoards.setValue(new ArrayList<>());

        favorBoards = new MutableLiveData<>();
        favorBoards.setValue(new ArrayList<>());
    }

    public boolean isFavorite(BaseBoard board) {
        for (BaseBoard favorBoard: favorBoards.getValue()) {
            if (favorBoard.getId().equals(board.getId()))
                return true;
        }
        return false;
    }
}