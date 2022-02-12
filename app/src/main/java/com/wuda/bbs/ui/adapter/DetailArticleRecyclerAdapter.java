package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.DetailArticle;
import com.wuda.bbs.ui.main.article.ReplyActivity;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.utils.network.NetConst;

import java.util.ArrayList;
import java.util.List;

public class DetailArticleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<DetailArticle> mDetailArticleList;
    Intent userInfoIntent;
    String mBoardId;

    private final int TYPE_CONTENT = 0;
    private final int TYPE_REPLY = 1;

    public DetailArticleRecyclerAdapter(Context context, String boardId) {
        mContext = context;
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
        if (viewType == TYPE_CONTENT) {
            return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_detail_content_item, parent, false));
        } else {
            return new ReplyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.article_detail_reply_item, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DetailArticle article = mDetailArticleList.get(position);
        if (holder instanceof ReplyViewHolder) {
            ((ReplyViewHolder) holder).replierUsername_tv.setText(article.getAuthor());
            ((ReplyViewHolder) holder).replyTime_tv.setText(article.getTime());
            ((ReplyViewHolder) holder).replyContent_tv.setText(replyContentBuilder(article));
            Glide.with(mContext)
                    .load(NetConst.BASE + "/" + article.getUserFaceImg())
                    .error(R.drawable.ic_face)
                    .into(((ReplyViewHolder) holder).replierAvatar_iv);
            ((ReplyViewHolder) holder).replierAvatar_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfoIntent.putExtra("userId", article.getAuthor());
                    mContext.startActivity(userInfoIntent);
                }
            });
            ((ReplyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ReplyDialog replyDialog = new ReplyDialog(mContext);
//                    replyDialog.show();
                    Intent intent = new Intent(mContext, ReplyActivity.class);
                    intent.putExtra("article", article);
                    intent.putExtra("boardId", mBoardId);
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof ContentViewHolder) {
//            ((ContentViewHolder) holder).authorAvatar_iv
            ((ContentViewHolder) holder).authorUsername_tv.setText(article.getAuthor());
            ((ContentViewHolder) holder).postTime_tv.setText(article.getTime());
            ((ContentViewHolder) holder).postContent_tv.setText(article.getContent());
            Glide.with(mContext)
                    .load(NetConst.BASE + "/" + article.getUserFaceImg())
                    .error(R.drawable.ic_face)
                    .into(((ContentViewHolder) holder).authorAvatar_iv);
//            ((ContentViewHolder) holder).replyNum_tv.setText(article.);
            ((ContentViewHolder) holder).authorAvatar_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userInfoIntent.putExtra("userId", article.getAuthor());
                    mContext.startActivity(userInfoIntent);
                }
            });
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

        Log.d("reply", builder.toString());

        return builder;
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        ImageView authorAvatar_iv;
        TextView authorUsername_tv;
        TextView postTime_tv;
        TextView postContent_tv;
        TextView replyNum_tv;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            authorAvatar_iv = itemView.findViewById(R.id.author_avatar_imageView);
            authorUsername_tv = itemView.findViewById(R.id.authorUsername_textView);
            postTime_tv = itemView.findViewById(R.id.postTime_textView);
            postContent_tv = itemView.findViewById(R.id.postContent_textView);
            replyNum_tv = itemView.findViewById(R.id.replyNum_textView);
        }
    }

    static class ReplyViewHolder extends RecyclerView.ViewHolder {

        ImageView replierAvatar_iv;
        TextView replierUsername_tv;
        TextView replyTime_tv;
        TextView replyContent_tv;

        public ReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            replierAvatar_iv = itemView.findViewById(R.id.author_avatar_imageView);
            replierUsername_tv = itemView.findViewById(R.id.authorUsername_textView);
            replyTime_tv = itemView.findViewById(R.id.postTime_textView);
            replyContent_tv = itemView.findViewById(R.id.postContent_textView);
        }
    }

    public interface OnItemClickListener {
        public void onItemClicked(DetailArticle detailArticle);
    }
}
