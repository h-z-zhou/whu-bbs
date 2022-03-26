package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.photoview.PhotoView;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Attachment;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.validator.MimeValidator;

import java.util.List;

public class AttachmentPager2Adapter extends RecyclerView.Adapter<AttachmentPager2Adapter.ViewHolder> {

    Context mContext;
    List<Attachment> mAttachmentList;
    String board;
    String articleId;

    public AttachmentPager2Adapter(Context mContext, List<Attachment> mAttachmentList, String board, String articleId) {
        this.mContext = mContext;
        this.mAttachmentList = mAttachmentList;
        this.board = board;
        this.articleId = articleId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_pager2_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Attachment attachment = mAttachmentList.get(position);
        MimeValidator.Mime mime = MimeValidator.getMimetype(attachment.getName());

        String url = NetConst.ATTACHMENT + "?board=" + board + "&id=" + articleId + "&ap=" + attachment.getId();

        if (mime.type == MimeValidator.Mime.Type.IMAGE) {
            Glide.with(mContext).load(url).into(holder.photoView);
            holder.photoView.setZoomable(true);
        } else {
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(500, 500);
            // 0 => parent id
            params.topToTop = 0;
            params.bottomToBottom = 0;
            params.startToStart = 0;
            params.endToEnd = 0;
            holder.photoView.setLayoutParams(params);
            Glide.with(mContext).load(mime.icon).into(holder.photoView);
            holder.filename_tv.setText(attachment.getName());
        }

    }

    @Override
    public int getItemCount() {
        return mAttachmentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        PhotoView photoView;
        TextView filename_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.attachment_photoView);
            photoView.setZoomable(false);
            filename_tv = itemView.findViewById(R.id.attachment_filename_textView);
        }
    }
}
