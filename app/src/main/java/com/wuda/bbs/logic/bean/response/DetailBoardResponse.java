package com.wuda.bbs.logic.bean.response;

import com.wuda.bbs.logic.bean.DetailBoard;

import java.util.List;

public class DetailBoardResponse extends BaseResponse {
    List<DetailBoard> detailBoardList;

    public List<DetailBoard> getDetailBoardList() {
        return detailBoardList;
    }

    public void setDetailBoardList(List<DetailBoard> detailBoardList) {
        this.detailBoardList = detailBoardList;
    }
}
