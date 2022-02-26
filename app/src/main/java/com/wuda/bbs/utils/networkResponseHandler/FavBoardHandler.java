package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

public abstract class FavBoardHandler implements BaseResponseHandler{
    @Override
    public BaseResponse handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseFavorBoard(new String(data));
    }
}
