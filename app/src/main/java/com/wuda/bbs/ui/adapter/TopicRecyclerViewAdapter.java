package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.DetailArticle;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.utils.network.NetConst;

import java.util.ArrayList;
import java.util.List;

public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<TopicRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    List<DetailArticle> mDetailArticleList;
    Intent userInfoIntent;

    public TopicRecyclerViewAdapter(Context context) {
        mContext = context;
        mDetailArticleList = new ArrayList<>();
        userInfoIntent = new Intent(mContext, UserInfoActivity.class);
        userInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_detail_reply_item, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailArticle article = mDetailArticleList.get(position);

        holder.username_tv.setText(article.getAuthor());
        holder.time_tv.setText(article.getTime());
        holder.content_tv.setText(replyContentBuilder(article));
        Glide.with(mContext).load(NetConst.BASE + "/" + article.getUserFaceImg()).into(holder.avatar_iv);
        holder.avatar_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoIntent.putExtra("userId", article.getAuthor());
                mContext.startActivity(userInfoIntent);
            }
        });
        if (position == 0) {
            holder.content_tv.setText(article.getContent());
        } else {
            holder.content_tv.setText(replyContentBuilder(article));
        }
    }

    @Override
    public int getItemCount() {
        return mDetailArticleList.size();
    }

    public void updateDataSet(List<DetailArticle> detailArticles) {
        this.mDetailArticleList = detailArticles;
        this.notifyDataSetChanged();
    }

    public void appendArticles(List<DetailArticle> detailArticles) {
        this.mDetailArticleList.addAll(detailArticles);
        this.notifyItemRangeInserted(this.mDetailArticleList.size()-detailArticles.size(), this.mDetailArticleList.size());
    }

    private SpannableStringBuilder replyContentBuilder(DetailArticle detailArticle) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append("@").append(detailArticle.getReply2username()).append(detailArticle.getReply2content());
        builder.setSpan(new RelativeSizeSpan(0.8f), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("\n");
        builder.append(detailArticle.getContent());

        return builder;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar_iv;
        TextView username_tv;
        TextView time_tv;
        TextView content_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_iv = itemView.findViewById(R.id.author_avatar_imageView);
            username_tv = itemView.findViewById(R.id.authorUsername_textView);
            time_tv = itemView.findViewById(R.id.postTime_textView);
            content_tv = itemView.findViewById(R.id.postContent_textView);
        }
    }
}
