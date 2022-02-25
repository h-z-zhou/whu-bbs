package com.wuda.bbs.ui.adapter;

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

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.BriefArticle;
import com.wuda.bbs.ui.article.DetailArticleActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BriefArticleAdapter extends FooterAdapter<BriefArticle> {
    public BriefArticleAdapter(Context mContext, List<BriefArticle> mContents) {
        super(mContext, mContents);
    }

    @Override
    protected ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);

        BriefArticleViewHolder holder = new BriefArticleViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BriefArticle article = mContents.get(holder.getAdapterPosition());
                Intent intent = new Intent(mContext, DetailArticleActivity.class);
                intent.putExtra("briefArticle", article);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    protected void onBindContentViewHolder(ContentViewHolder contentHolder, BriefArticle content) {

        if (contentHolder instanceof BriefArticleViewHolder) {

            BriefArticleViewHolder holder = (BriefArticleViewHolder) contentHolder;

            holder.author_tv.setText(content.getAuthor());
            holder.time_tv.setText(content.getTime());
            if (content.getReplyNum() == null) {  // 推荐无
                holder.replyNum_tv.setVisibility(View.GONE);
            } else {
                holder.replyNum_tv.setText(content.getReplyNum());
            }

            if (content.getFlag() == BriefArticle.FLAG_SYSTEM) {
                holder.content_tv.setText(buildSpannableContent(content));
            } else {
                SpannableStringBuilder contentBuilder = new SpannableStringBuilder();
                if (content.getFlag() == BriefArticle.FLAG_TOP) {
                    contentBuilder.append("#顶置# ");
                    contentBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#eb507e")), 0, contentBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                contentBuilder.append(content.getTitle());
                holder.content_tv.setText(contentBuilder);
            }
        }
    }

    private SpannableStringBuilder buildSpannableContent(BriefArticle briefArticle) {
        SpannableStringBuilder content = new SpannableStringBuilder();
        content.append("#").append(briefArticle.getBoardName()).append("#");
        content.setSpan(new ForegroundColorSpan(Color.parseColor("#5698c3")), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.append(" ");
        content.append(briefArticle.getTitle());
        return content;
    }

    static class BriefArticleViewHolder extends ContentViewHolder {
        CircleImageView avatar_iv;
        TextView author_tv;
        TextView time_tv;
        TextView replyNum_tv;
        TextView content_tv;

        public BriefArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_iv = itemView.findViewById(R.id.articleAvatar_imageView);
            author_tv = itemView.findViewById(R.id.articleAuthor_textView);
            time_tv = itemView.findViewById(R.id.articleTime_textView);
            replyNum_tv = itemView.findViewById(R.id.articleReplyNum_textView);
            content_tv = itemView.findViewById(R.id.articleContent_textView);
        }
    }
}
