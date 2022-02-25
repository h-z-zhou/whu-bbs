package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.logic.bean.FavArticle;
import com.wuda.bbs.ui.article.DetailArticleActivity;

import java.util.List;

public class FavArticleAdapter extends FooterAdapter<FavArticle> {
    public FavArticleAdapter(Context mContext, List<FavArticle> mContents) {
        super(mContext, mContents);
    }

    @Override
    protected ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent) {
        TextView name_tv = new TextView(parent.getContext());
        name_tv.setTextSize(18);
        name_tv.setPadding(16, 32, 16, 32);
        name_tv.setLayoutParams(new RecyclerView.LayoutParams(-1, -2));

        FavArticleViewHolder holder = new FavArticleViewHolder(name_tv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavArticle favArticle = mContents.get(holder.getAdapterPosition());

                BriefArticle briefArticle = new BriefArticle();
                briefArticle.setBoardID(favArticle.getBoardId());
                briefArticle.setGID(favArticle.getGroupId());
                briefArticle.setTitle(favArticle.getName());

                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                intent.putExtra("briefArticle", briefArticle);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    protected void onBindContentViewHolder(ContentViewHolder contentHolder, FavArticle content) {

        if (contentHolder instanceof FavArticleViewHolder) {
            FavArticleViewHolder holder = (FavArticleViewHolder) contentHolder;
            holder.title_tv.setText(content.getName());
        }

    }

    public FavArticle removeItem(int position) {
        if (position >= mContents.size()) {
            return null;
        } else {
            FavArticle favArticle = mContents.get(position);
            mContents.remove(position);
            this.notifyItemRemoved(position);
            return favArticle;
        }
    }

    static class FavArticleViewHolder extends ContentViewHolder {

        TextView title_tv;

        public FavArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = (TextView) itemView;
        }
    }
}
