package com.wuda.bbs.ui.adapter;

import android.content.Context;
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
import com.wuda.bbs.bean.Article;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArticleRecyclerAdapter extends RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder> {

    Context mContext;
//    boolean mAddBoardName;  // 在推荐文章，热点，新帖等多板块的内容上添加 board
    List<Article> mArticleList;

//    public ArticleRecyclerAdapter(Context context, List<Article> articleList, boolean addBoard) {
//        this.mContext = context;
//        this.mArticleList = articleList;
//        this.mAddBoardName = addBoard;
//    }

    public ArticleRecyclerAdapter(Context mContext, List<Article> mArticleList) {
        this.mContext = mContext;
        this.mArticleList = mArticleList;
//        this.mAddBoardName = false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Article article = mArticleList.get(position);
        holder.author_tv.setText(article.getAuthor());
        holder.time_tv.setText(article.getTime());
        if (article.getReplyNum() == null) {  // 推荐无
            holder.replyNum_tv.setVisibility(View.GONE);
        } else {
            holder.replyNum_tv.setText(article.getReplyNum());
        }

        if (article.getFlag() == null) {
            holder.content_tv.setText(buildSpannableContent(article));
        } else {
            SpannableStringBuilder content = new SpannableStringBuilder();
            if (article.getFlag().equals("TOP")) {
                content.append("#顶置# ");
                content.setSpan(new ForegroundColorSpan(Color.parseColor("#eb507e")), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            content.append(article.getTitle());
            holder.content_tv.setText(content);
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList.size();
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

    public void updateArticleList(List<Article> articleList) {
        mArticleList = articleList;
    }

    private SpannableStringBuilder buildSpannableContent(Article article) {
        SpannableStringBuilder content = new SpannableStringBuilder();
        content.append("#").append(article.getBoardName()).append("#");
        content.setSpan(new ForegroundColorSpan(Color.parseColor("#5698c3")), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.append(" ");
        content.append(article.getTitle());
        return content;
    }
}
