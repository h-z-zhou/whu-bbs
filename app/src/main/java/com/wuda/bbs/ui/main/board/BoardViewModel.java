package com.wuda.bbs.ui.main.board;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.bean.Board;

import java.util.ArrayList;
import java.util.List;

public class BoardViewModel extends ViewModel {
    MutableLiveData<List<Board>> allBoardList = new MutableLiveData<List<Board>>();
    MutableLiveData<List<Board>> displacedBoardList = new MutableLiveData<List<Board>>();
    MutableLiveData<Integer> currentBoardIdx = new MutableLiveData<>(-1);
//    MutableLiveData<List<Board>>
    // TODO: Implement the ViewModel

    public BoardViewModel() {
        allBoardList.setValue(new ArrayList<>());
        displacedBoardList.setValue(new ArrayList<>());
    }
}