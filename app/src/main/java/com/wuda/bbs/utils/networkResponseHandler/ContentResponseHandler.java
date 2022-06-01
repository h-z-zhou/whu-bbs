package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;

/**
 * 网络数据处理：使用策略者模式，结合 network/...Callback使用
 * @param <T> 返回方式的内容格式
 */
public interface ContentResponseHandler<T> {
    /**
     * 网络数据的处理，根据具体接口实现
     * 实现该接口时应该同时实现该函数
     * @param data：可能为 utf-8 / gbk格式
     * @return ：处理后的数据
     */
    ContentResponse<T> handleNetworkResponse(@NonNull byte[] data);

    /**
     * 数据处理结束后将结果回调给调用者
     * 继承该接口时不应实现实现该方法
     * 应在View 或 ViewModel中实现该方法
     * @param response： 可能为handleNetworkResponse 处理的结果，也可能是网络异常的结果
     */
    void onResponseHandled(ContentResponse<T> response);
}
