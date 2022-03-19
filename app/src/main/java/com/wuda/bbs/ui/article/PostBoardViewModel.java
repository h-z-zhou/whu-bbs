package com.wuda.bbs.ui.article;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.BaseBoard;

public class PostBoardViewModel extends ViewModel {
    MutableLiveData<BaseBoard> boardMutableLiveData = new MutableLiveData<>();
}
