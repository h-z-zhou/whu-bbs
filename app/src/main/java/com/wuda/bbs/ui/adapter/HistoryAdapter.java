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

public class HistoryAdapter extends FooterAdapter<History> {
    public HistoryAdapter(Context mContext, List<History> mContents) {
        super(mContext, mContents);
    }

    @Override
    protected ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent) {
        TextView name_tv = new TextView(parent.getContext());
        name_tv.setTextSize(18);
        name_tv.setPadding(16, 32, 16, 32);
        name_tv.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        HistoryViewHolder holder = new HistoryViewHolder(name_tv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                History history = mContents.get(holder.getAdapterPosition());
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                BriefArticle briefArticle = new BriefArticle();
                briefArticle.setBoardID(history.getBoardID());
                briefArticle.setGID(history.getGID());
                briefArticle.setTitle(history.getTitle());
                intent.putExtra("briefArticle", briefArticle);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    protected void onBindContentViewHolder(ContentViewHolder holder, History content) {
        if (holder instanceof HistoryViewHolder) {
            ((HistoryViewHolder) holder).title_tv.setText(content.getTitle());
        }
    }

    static class HistoryViewHolder extends ContentViewHolder {
        TextView title_tv;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = (TextView) itemView;
        }
    }
}
