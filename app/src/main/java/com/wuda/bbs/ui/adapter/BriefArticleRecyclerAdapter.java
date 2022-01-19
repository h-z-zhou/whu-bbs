package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.ui.main.article.DetailArticleActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BriefArticleRecyclerAdapter extends RecyclerView.Adapter<BriefArticleRecyclerAdapter.ViewHolder> {

    Context mContext;
//    boolean mAddBoardName;  // 在推荐文章，热点，新帖等多板块的内容上添加 board
    List<BriefArticle> mBriefArticleList;

//    public ArticleRecyclerAdapter(Context context, List<Article> articleList, boolean addBoard) {
//        this.mContext = context;
//        this.mArticleList = articleList;
//        this.mAddBoardName = addBoard;
//    }

    public BriefArticleRecyclerAdapter(Context mContext, List<BriefArticle> mBriefArticleList) {
        this.mContext = mContext;
        this.mBriefArticleList = mBriefArticleList;
//        this.mAddBoardName = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        BriefArticle briefArticle = mBriefArticleList.get(position);
        holder.author_tv.setText(briefArticle.getAuthor());
        holder.time_tv.setText(briefArticle.getTime());
        if (briefArticle.getReplyNum() == null) {  // 推荐无
            holder.replyNum_tv.setVisibility(View.GONE);
        } else {
            holder.replyNum_tv.setText(briefArticle.getReplyNum());
        }

        if (briefArticle.getFlag() == BriefArticle.FLAG_SYSTEM) {
            holder.content_tv.setText(buildSpannableContent(briefArticle));
        } else {
            SpannableStringBuilder content = new SpannableStringBuilder();
            if (briefArticle.getFlag() == BriefArticle.FLAG_TOP) {
                content.append("#顶置# ");
                content.setSpan(new ForegroundColorSpan(Color.parseColor("#eb507e")), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            content.append(briefArticle.getTitle());
            holder.content_tv.setText(content);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                intent.putExtra("article", mBriefArticleList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBriefArticleList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView avatar_iv;
        TextView author_tv;
        TextView time_tv;
        TextView replyNum_tv;
        TextView content_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_iv = itemView.findViewById(R.id.articleAvatar_imageView);
            author_tv = itemView.findViewById(R.id.articleAuthor_textView);
            time_tv = itemView.findViewById(R.id.articleTime_textView);
            replyNum_tv = itemView.findViewById(R.id.articleReplyNum_textView);
            content_tv = itemView.findViewById(R.id.articleContent_textView);
        }
    }

    public void updateArticleList(List<BriefArticle> briefArticleList) {
        mBriefArticleList = briefArticleList;
    }

    public void appendArticles(List<BriefArticle> briefArticles) {
        // 为空时使用insert有强烈的跳变感
        if (mBriefArticleList.isEmpty()) {
            mBriefArticleList = briefArticles;
            this.notifyDataSetChanged();
        } else {
            mBriefArticleList.addAll(briefArticles);
            this.notifyItemRangeInserted(mBriefArticleList.size() - briefArticles.size(), mBriefArticleList.size());
        }
    }

    public void removeAll() {
        mBriefArticleList.clear();
    }

    private SpannableStringBuilder buildSpannableContent(BriefArticle briefArticle) {
        SpannableStringBuilder content = new SpannableStringBuilder();
        content.append("#").append(briefArticle.getBoardName()).append("#");
        content.setSpan(new ForegroundColorSpan(Color.parseColor("#5698c3")), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.append(" ");
        content.append(briefArticle.getTitle());
        return content;
    }
}
