package com.wuda.bbs.ui.home;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.DetailBoardDao;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.DetailBoardHandler;

import java.util.List;

public class BoardEntranceViewModel extends BaseResponseViewModel {
    private MutableLiveData<List<DetailBoard>> boardListMutableLiveData;

    public MutableLiveData<List<DetailBoard>> getBoardListMutableLiveData() {
        if (boardListMutableLiveData == null) {
            boardListMutableLiveData = new MutableLiveData<>();
        }
        return boardListMutableLiveData;
    }

    public void queryAllBoardFromDB() {

        DetailBoardDao detailBoardDao = AppDatabase.getDatabase().getDetailBoardDao();
//        room group by ??
//        List<DetailBoard> detailBoardList = detailBoardDao.loadBoardsGroupBySection();
        List<DetailBoard> detailBoardList = detailBoardDao.loadAllBoards();
        if (detailBoardList.isEmpty()) {
            requestDetailBoardsFromServer();
        } else {
            boardListMutableLiveData.postValue(detailBoardList);
        }
    }

    public void requestDetailBoardsFromServer() {
        NetworkEntry.requestDetailBoard(new DetailBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<DetailBoard>> response) {
                if (response.isSuccessful()) {
                    List<DetailBoard> detailBoardList = response.getContent();
                    DetailBoardDao detailBoardDao = AppDatabase.getDatabase().getDetailBoardDao();
                    detailBoardDao.clearAll();
                    detailBoardDao.insert(detailBoardList);
                    queryAllBoardFromDB();
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

}
