package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
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

public class FavArticleAdapter extends RecyclerView.Adapter<FavArticleAdapter.ViewHolder> {

    Context mContext;
    List<FavArticle> mFavArticleList;

    public FavArticleAdapter(Context mContext, List<FavArticle> mFavArticleList) {
        this.mContext = mContext;
        this.mFavArticleList = mFavArticleList;
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
        FavArticle favArticle = mFavArticleList.get(position);
        holder.title_tv.setText(favArticle.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                BriefArticle briefArticle = new BriefArticle();
                briefArticle.setBoardID(favArticle.getBoardId());
                briefArticle.setGID(favArticle.getGroupId());
                briefArticle.setTitle(favArticle.getName());
                intent.putExtra("briefArticle", briefArticle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFavArticleList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateFavArticles(List<FavArticle> favArticleList) {
        this.mFavArticleList = favArticleList;
        this.notifyDataSetChanged();
    }

    public FavArticle removeItem(int position) {
        if (position >= mFavArticleList.size()) {
            return new FavArticle();
        } else {
            FavArticle favArticle = mFavArticleList.get(position);
            mFavArticleList.remove(position);
            this.notifyItemRemoved(position);
            return favArticle;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = (TextView) itemView;
        }
    }
}
