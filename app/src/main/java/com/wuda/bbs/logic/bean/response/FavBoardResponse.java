package com.wuda.bbs.logic.bean.response;

import com.wuda.bbs.logic.bean.FavBoard;

import java.util.List;

public class FavBoardResponse extends BaseResponse {
    List<FavBoard> favBoardList;

    public List<FavBoard> getFavBoardList() {
        return favBoardList;
    }

    public void setFavBoardList(List<FavBoard> favBoardList) {
        this.favBoardList = favBoardList;
    }
}
