package com.wuda.bbs.utils.networkResponseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;

import java.io.UnsupportedEncodingException;

/*
*   do nothing，just return BaseResponse with SUCCESS
*
*   => login / logout / (add/delete)friend
* */
public abstract class SimpleResponseHandler implements ContentResponseHandler<Object> {
    @Override
    public ContentResponse<Object> handleNetworkResponse(@NonNull byte[] data) {
        try {
            String text = new String(data, "GBK");
            Log.d("response", text);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ContentResponse<>();
    }
}
