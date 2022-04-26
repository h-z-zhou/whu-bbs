package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.util.List;

public abstract class DetailBoardHandler implements ContentResponseHandler<List<DetailBoard>> {
    @Override
    public ContentResponse<List<DetailBoard>> handleNetworkResponse(@NonNull byte[] data) {
        return XMLParser.parseDetailBoard(new String(data));
    }
}
