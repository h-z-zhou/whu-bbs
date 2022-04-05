package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.utils.DensityUtil;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.DetailArticle;
import com.wuda.bbs.ui.article.AttachmentActivity;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.ui.widget.ArticleTextView;
import com.wuda.bbs.ui.widget.FixedMovementTextView;
import com.wuda.bbs.ui.widget.FullyGridLayoutManager;
import com.wuda.bbs.ui.widget.MaskedAttachmentImageView;
import com.wuda.bbs.utils.ContentTextUtil;
import com.wuda.bbs.utils.network.NetConst;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<DetailArticle> mDetailArticleList;
    Intent userInfoIntent;
    String mGroupId;
    String mBoardId;

    AdapterItemListener<DetailArticle> mItemListener;

    private final int TYPE_CONTENT = 0;
    private final int TYPE_REPLY = 1;

    public DetailArticleAdapter(Context context, String groupId, String boardId) {
        mContext = context;
        mGroupId = groupId;
        mBoardId = boardId;
        mDetailArticleList = new ArrayList<>();
        userInfoIntent = new Intent(mContext, UserInfoActivity.class);
        userInfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    public int getItemViewType(int position) {
        return position==0? TYPE_CONTENT: TYPE_REPLY;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_CONTENT) {
            holder =  new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_detail_content_item, parent, false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAbsoluteAdapterPosition();
                    mItemListener.onItemClick(mDetailArticleList.get(position), position);
                }
            });
        } else {
            holder = new ReplyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_detail_reply_item, parent, false));
            // 仅恢复部分使用ClickListener
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAbsoluteAdapterPosition();
                    mItemListener.onItemClick(mDetailArticleList.get(position), position);
                }
            });
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAbsoluteAdapterPosition();
                mItemListener.onItemLongClick(mDetailArticleList.get(position), position);
                return true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        DetailArticle article = mDetailArticleList.get(position);
        if (holder instanceof ReplyViewHolder) {
            ((ReplyViewHolder) holder).replierUsername_tv.setText(article.getAuthor());
            ((ReplyViewHolder) holder).replyTime_tv.setText(article.getTime());
            ((ReplyViewHolder) holder).replyContent_tv.setText(replyContentBuilder(((ReplyViewHolder) holder).replyContent_tv, article));
            if (!article.getUserFaceImg().equals("wForum/")) {
                Glide.with(mContext)
                        .load(NetConst.BASE + "/" + article.getUserFaceImg())
                        .error(R.drawable.ic_face)
                        .into(((ReplyViewHolder) holder).replierAvatar_iv);
            }
            if (!article.getAttachmentList().isEmpty()) {
                MaskedAttachmentImageView attachmentImageView = new MaskedAttachmentImageView(((ReplyViewHolder) holder).root_cl.getContext());
                attachmentImageView.addAttachments(article.getAttachmentList(), mBoardId, article.getId());
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(DensityUtil.dip2px(mContext, 100), ViewGroup.LayoutParams.WRAP_CONTENT);
                params.startToStart = ((ReplyViewHolder) holder).replyContent_tv.getId();
                params.topToBottom = ((ReplyViewHolder) holder).replyContent_tv.getId();
                attachmentImageView.setLayoutParams(params);
                ((ReplyViewHolder) holder).root_cl.addView(attachmentImageView);
            }

            ((ReplyViewHolder) holder).replierAvatar_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfoIntent.putExtra("userId", article.getAuthor());
                    mContext.startActivity(userInfoIntent);
                }
            });

        } else if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).authorUsername_tv.setText(article.getAuthor());
            ((ContentViewHolder) holder).postTime_tv.setText(article.getTime());
            ((ContentViewHolder) holder).postContent_tv.setContentText(article.getContent(), article.getAttachmentList(), mBoardId, article.getId());
            if (!article.getUserFaceImg().equals("wForum/")) {
                Glide.with(mContext)
                        .load(NetConst.BASE + "/" + article.getUserFaceImg())
                        .error(R.drawable.ic_face)
                        .into(((ContentViewHolder) holder).authorAvatar_iv);
            }

            ((ContentViewHolder) holder).authorAvatar_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfoIntent.putExtra("userId", article.getAuthor());
                    mContext.startActivity(userInfoIntent);
                }
            });

            if (!article.getAttachmentList().isEmpty()) {
                RecyclerView recyclerView = new RecyclerView(((ContentViewHolder) holder).root_cl.getContext());
                recyclerView.setLayoutManager(new FullyGridLayoutManager(mContext, 3));
                recyclerView.addItemDecoration(new GridSpacingItemDecoration(3,
                        DensityUtil.dip2px(mContext, 4), false));
                recyclerView.setAdapter(new AttachmentAdapter(mContext, mBoardId, article.getId(), article.getAttachmentList()));
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.startToStart = 0;
                params.endToEnd = 0;
                params.topToBottom = ((ContentViewHolder) holder).postContent_tv.getId();
                params.bottomToBottom = 0;
                recyclerView.setLayoutParams(params);
                ((ContentViewHolder) holder).root_cl.addView(recyclerView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDetailArticleList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateDataSet(List<DetailArticle> detailArticles) {
        this.mDetailArticleList = detailArticles;
        this.notifyDataSetChanged();
    }

    public void appendArticles(List<DetailArticle> detailArticles) {
        this.mDetailArticleList.addAll(detailArticles);
        this.notifyItemRangeInserted(this.mDetailArticleList.size()-detailArticles.size(), this.mDetailArticleList.size());
    }

    private SpannableStringBuilder replyContentBuilder(TextView textView, DetailArticle detailArticle) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append("@").append(detailArticle.getReply2username()).append(detailArticle.getReply2content());
        builder.setSpan(new RelativeSizeSpan(0.8f), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append("\n");
//        builder.append(detailArticle.getContent());
        builder.append(ContentTextUtil.getSpannableString(mContext, textView, detailArticle.getContent()));

        return builder;
    }

    public void setItemListener(AdapterItemListener<DetailArticle> itemListener) {
        this.mItemListener = itemListener;
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout root_cl;
        ImageView authorAvatar_iv;
        TextView authorUsername_tv;
        TextView postTime_tv;
        ArticleTextView postContent_tv;
        TextView replyNum_tv;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            root_cl = itemView.findViewById(R.id.root_contraintLayout);
            authorAvatar_iv = itemView.findViewById(R.id.author_avatar_imageView);
            authorUsername_tv = itemView.findViewById(R.id.authorUsername_textView);
            postTime_tv = itemView.findViewById(R.id.postTime_textView);
            postContent_tv = itemView.findViewById(R.id.postContent_textView);
            // item的点击事件消失
            postContent_tv.setMovementMethod(ArticleTextView.LocalLinkMovementMethod.getInstance());
            replyNum_tv = itemView.findViewById(R.id.replyNum_textView);
        }
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout root_cl;
        ImageView replierAvatar_iv;
        TextView replierUsername_tv;
        TextView replyTime_tv;
        TextView replyContent_tv;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            root_cl = itemView.findViewById(R.id.root_contraintLayout);
            replierAvatar_iv = itemView.findViewById(R.id.author_avatar_imageView);
            replierUsername_tv = itemView.findViewById(R.id.authorUsername_textView);
            replyTime_tv = itemView.findViewById(R.id.postTime_textView);
            replyContent_tv = itemView.findViewById(R.id.postContent_textView);
            replyContent_tv.setMovementMethod(FixedMovementTextView.LocalLinkMovementMethod.getInstance());
        }
    }

}
