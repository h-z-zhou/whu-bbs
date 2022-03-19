package com.wuda.bbs.ui.board;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FavorBoardDao;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardViewModel extends BaseResponseViewModel {
    private MutableLiveData<List<FavBoard>> favBoardListMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Integer> currentBoardIdx = new MutableLiveData<>();

    public MutableLiveData<List<FavBoard>> getFavBoardListMutableLiveData() {
        if (favBoardListMutableLiveData == null) {
            favBoardListMutableLiveData = new MutableLiveData<>();
            favBoardListMutableLiveData.setValue(new ArrayList<>());
        }
        return favBoardListMutableLiveData;
    }

    public void queryFavorBoardsFromDB() {
        if (BBSApplication.getAccountId().equals(""))
            return;

        FavorBoardDao favorBoardDao = AppDatabase.getDatabase().getFavorBoardDao();
        List<FavBoard> favorBoardList = favorBoardDao.loadFavorBoardByUsername(BBSApplication.getAccountId());

        if (favorBoardList.isEmpty()) {
            requestFavorBoardsFromServer();
        } else {
            getFavBoardListMutableLiveData().postValue(favorBoardList);
        }
    }

    public void requestFavorBoardsFromServer() {

        NetworkEntry.requestFavBoard(new FavBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavBoard>> response) {

                if (response.isSuccessful()) {
                    List<FavBoard> favorBoardList = response.getContent();
                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase().getFavorBoardDao();
                    favorBoardDao.clearAll();
                    favorBoardDao.insert(favorBoardList);
                    getFavBoardListMutableLiveData().postValue(favorBoardList);
                } else {
                    getErrorResponseMutableLiveData().postValue(response);
                }
            }
        });
    }

}