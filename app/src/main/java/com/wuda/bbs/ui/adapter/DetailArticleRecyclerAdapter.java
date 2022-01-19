package com.wuda.bbs.ui.adapter;

import android.content.Context;
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
import com.wuda.bbs.utils.network.NetConst;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailArticleRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<DetailArticle> mDetailArticleList;

    private final int TYPE_CONTENT = 0;
    private final int TYPE_REPLY = 1;

    public DetailArticleRecyclerAdapter(Context context) {
        mContext = context;
        mDetailArticleList = new ArrayList<>();
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
            Glide.with(mContext).load(NetConst.BASE + "/" + article.getUserFaceImg()).into(((ReplyViewHolder) holder).replierAvatar_iv);
        } else if (holder instanceof ContentViewHolder) {
//            ((ContentViewHolder) holder).authorAvatar_iv
            ((ContentViewHolder) holder).authorUsername_tv.setText(article.getAuthor());
            ((ContentViewHolder) holder).postTime_tv.setText(article.getTime());
            ((ContentViewHolder) holder).postContent_tv.setText(article.getContent());
            Glide.with(mContext).load(NetConst.BASE + "/" + article.getUserFaceImg()).into(((ContentViewHolder) holder).authorAvatar_iv);
//            ((ContentViewHolder) holder).replyNum_tv.setText(article.);
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
}
