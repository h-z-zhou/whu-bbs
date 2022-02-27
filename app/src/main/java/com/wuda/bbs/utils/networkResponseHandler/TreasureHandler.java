package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.Treasure;
import com.wuda.bbs.logic.bean.response.ContentResponse;

import java.util.List;

public abstract class TreasureHandler implements ContentResponseHandler<List<Treasure>> {
    @Override
    public ContentResponse<List<Treasure>> handleNetworkResponse(@NonNull byte[] data) {
        return null;
    }
}
