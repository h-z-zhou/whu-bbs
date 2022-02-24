package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.History;
import com.wuda.bbs.ui.article.DetailArticleActivity;

import java.util.List;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.ViewHolder> {

    Context mContext;
    List<History> mHistoryList;

    public HistoryRecyclerAdapter(Context mContext, List<History> mHistoryList) {
        this.mContext = mContext;
        this.mHistoryList = mHistoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView name_tv = new TextView(parent.getContext());
        name_tv.setTextSize(18);
        name_tv.setPadding(16, 32, 16, 32);
        name_tv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new ViewHolder(name_tv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = mHistoryList.get(position);
        holder.title_tv.setText(history.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                BriefArticle briefArticle = new BriefArticle();
                briefArticle.setBoardID(history.getBoardID());
                briefArticle.setGID(history.getGID());
                briefArticle.setTitle(history.getTitle());
                intent.putExtra("briefArticle", briefArticle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }

    public void addHistories(List<History> historyList) {
        mHistoryList.addAll(historyList);
        this.notifyItemRangeInserted(mHistoryList.size()-historyList.size(), mHistoryList.size());
    }

    public History removeItem(int position) {
        if (position >= mHistoryList.size()) {
            return new History();
        } else {
            History history = mHistoryList.get(position);
            mHistoryList.remove(position);
            this.notifyItemRemoved(position);
            return history;
        }
    }

    public void clearHistory() {
        int cnt = mHistoryList.size();
        mHistoryList.clear();
        this.notifyItemRangeRemoved(0, cnt);
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = (TextView) itemView;
        }
    }
}
