package com.wuda.bbs.ui.board;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.bean.FavBoard;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardManagerViewModel extends ViewModel {

    MutableLiveData<List<DetailBoard>> allBoards;
    MutableLiveData<List<FavBoard>> favBoards;
    // TODO: Implement the ViewModel

    public FavorBoardManagerViewModel() {
        allBoards = new MutableLiveData<>();
        allBoards.setValue(new ArrayList<>());

        favBoards = new MutableLiveData<>();
        favBoards.setValue(new ArrayList<>());
    }

    public boolean isFavorite(BaseBoard board) {
        List<FavBoard> favBoardList = favBoards.getValue();
        if (favBoardList != null) {
            for (BaseBoard favorBoard : favBoardList) {
                if (favorBoard.getId().equals(board.getId()))
                    return true;
            }
        }
        return false;
    }
}