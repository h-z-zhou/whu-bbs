package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.ui.widget.CenterImageSpan;

import java.util.List;

public class BriefArticleRecyclerAdapter extends RecyclerView.Adapter<BriefArticleRecyclerAdapter.ViewHolder> {

    List<BriefArticle> mBriefArticleList;
    Context mContext;

    public BriefArticleRecyclerAdapter(Context context, List<BriefArticle> briefArticleList) {
        this.mContext = context;
        this.mBriefArticleList = briefArticleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MaterialTextView textView = new MaterialTextView(parent.getContext());
        textView.setPadding(32, 32, 32, 32);
        textView.setTextSize(16);

        return new ViewHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.content_tv.setText(buildSpannableContent(mBriefArticleList.get(position)));
    }

    @Override
    public int getItemCount() {
        return mBriefArticleList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView content_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content_tv = (MaterialTextView) itemView;
        }
    }

    public void updateArticleList(List<BriefArticle> briefArticleList) {
        mBriefArticleList = briefArticleList;
    }

    private SpannableStringBuilder buildSpannableContent(BriefArticle briefArticle) {
        SpannableStringBuilder content = new SpannableStringBuilder();

        content.append(briefArticle.getTitle());
        content.setSpan(new RelativeSizeSpan(1.2f), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        content.append("\n");
        content.append(briefArticle.getAuthor());
        content.append(" | ");
        content.append(briefArticle.getTime());

//        content.append("\n");
        Drawable replyIcon = mContext.getDrawable(R.drawable.ic_chat);
        replyIcon.setBounds(0, 0, replyIcon.getIntrinsicWidth(), replyIcon.getIntrinsicHeight());
        ImageSpan reply_is = new CenterImageSpan(replyIcon);
        content.append("__");
        content.setSpan(reply_is, content.length()-2, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        content.append(briefArticle.getReplyNum());
        return  content;
    }
}
