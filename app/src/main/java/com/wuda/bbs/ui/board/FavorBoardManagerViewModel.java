package com.wuda.bbs.ui.board;


import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.bean.bbs.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.DetailBoardDao;
import com.wuda.bbs.logic.dao.FavorBoardDao;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.DetailBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavorBoardManagerViewModel extends BaseResponseViewModel {

    private MutableLiveData<List<DetailBoard>> allBoardsMutableLiveData;
    private MutableLiveData<List<FavBoard>> favBoardsMutableLiveData;

    public MutableLiveData<List<DetailBoard>> getAllBoardsMutableLiveData() {
        if (allBoardsMutableLiveData == null) {
            allBoardsMutableLiveData = new MutableLiveData<>();
        }
        return allBoardsMutableLiveData;
    }

    public MutableLiveData<List<FavBoard>> getFavBoardsMutableLiveData() {
        if (favBoardsMutableLiveData == null) {
            favBoardsMutableLiveData = new MutableLiveData<>();
        }
        return favBoardsMutableLiveData;
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
                    allBoardsMutableLiveData.postValue(response.getContent());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void requestFavBoardsFromServer() {
        NetworkEntry.requestFavBoard(new FavBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavBoard>> response) {
                if (response.isSuccessful()) {
                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase().getFavorBoardDao();
                    // 清空，与云端保持同步
                    favorBoardDao.clearFavorBoardsByUsername(BBSApplication.getAccountId());
                    favorBoardDao.insert(response.getContent());
                    favBoardsMutableLiveData.postValue(response.getContent());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public void add2Favor(DetailBoard board) {
        addOrRemoveFavor(board, true);
    }

    public void remove2Favor(DetailBoard board) {
        addOrRemoveFavor(board, false);
    }

    private void addOrRemoveFavor(DetailBoard board, boolean isAdd) {
        Map<String, String> form = new HashMap<>();
        FavorBoardDao favorBoardDao = AppDatabase.getDatabase().getFavorBoardDao();
        if (isAdd) {
            form.put("bname", board.getId());
            favorBoardDao.insert(new FavBoard(board.getId(), board.getName(), board.getNum(), BBSApplication.getAccountId()));
        } else {
            // 编号减一
            form.put("delete", Integer.valueOf(Integer.parseInt(board.getNum()) - 1).toString());
            favorBoardDao.delete(board.getId());
        }
        form.put("select", "0");

        NetworkEntry.operateFavBoard(form, new SimpleResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Object> response) {
                if (!response.isSuccessful()) {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

    public boolean isFavorite(BaseBoard board) {
        List<FavBoard> favBoardList = favBoardsMutableLiveData.getValue();
        if (favBoardList != null) {
            for (BaseBoard favorBoard : favBoardList) {
                if (favorBoard.getId().equals(board.getId()))
                    return true;
            }
        }
        return false;
    }
}