package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.util.List;

public abstract class FavBoardHandler implements ContentResponseHandler<List<FavBoard>> {
    @Override
    public ContentResponse<List<FavBoard>> handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseFavorBoard(new String(data));
    }
}
