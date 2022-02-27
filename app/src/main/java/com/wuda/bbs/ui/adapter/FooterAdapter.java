package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class FooterAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int TYPE_CONTENT = 1, TYPE_FOOTER = 2;

    Context mContext;
    List<T> mContents;
    boolean isMore = true;

    public FooterAdapter(Context mContext, List<T> mContents) {
        this.mContext = mContext;
        this.mContents = mContents;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            TextView footer_tv = new TextView(mContext);
            footer_tv.setTextSize(18);
            footer_tv.setGravity(Gravity.CENTER);
            footer_tv.setPadding(0, 16, 0, 16);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -2);
            footer_tv.setLayoutParams(layoutParams);
            return new FooterViewHolder(footer_tv);
        } else {
            return onCreateContentViewHolder(parent);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mContents.size()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FooterAdapter.FooterViewHolder) {

            String text;
            if (isMore) {
                text = "加载中...";
            } else {
                if (mContents.isEmpty()) {
                    text = "空空如也";
                } else {
                    text = "没有更多了";
                }
            }
            ((FooterViewHolder) holder).footer_tv.setText(text);

        } else {
            onBindContentViewHolder((ContentViewHolder) holder, mContents.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (mContents == null) {
            return 1;
        } else {
            return mContents.size() + 1;
        }
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public void updateFooter() {
        notifyItemChanged(mContents.size());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setContents(List<T> contents) {
        mContents.clear();
        mContents.addAll(contents);
        notifyDataSetChanged();
    }

    public void appendContents(List<T> contents) {
        mContents.addAll(contents);
        notifyItemRangeInserted(mContents.size()-contents.size(), contents.size());
        // isMore => footer
        notifyItemChanged(mContents.size());
    }


    protected abstract ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent);
    protected abstract void onBindContentViewHolder(ContentViewHolder contentHolder, T content);

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footer_tv;
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
            if (itemView instanceof TextView) {
                footer_tv = (TextView) itemView;
            }
        }
    }

    public static abstract class ContentViewHolder extends RecyclerView.ViewHolder {
        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
