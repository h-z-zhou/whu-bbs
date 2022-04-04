package com.wuda.bbs.ui.adapter;

public interface AdapterItemListener<T> {
    void onItemClick(T data, int position);
    void onItemLongClick(T data, int position);
}
