package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Attachment;
import com.wuda.bbs.ui.article.AttachmentActivity;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.validator.MimeValidator;

import java.util.List;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.ViewHolder> {

    Context mContext;
    String mBoardId;
    String mArticleId;
    List<Attachment> mAttachmentList;

    public AttachmentAdapter(Context mContext, String mBoardId, String mArticleId, List<Attachment> mAttachmentList) {
        this.mContext = mContext;
        this.mBoardId = mBoardId;
        this.mArticleId = mArticleId;
        this.mAttachmentList = mAttachmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Attachment attachment = mAttachmentList.get(position);
        String url;
        url = NetConst.ATTACHMENT + "?board=" + mBoardId + "&id=" + mArticleId + "&ap=" + attachment.getId();

        MimeValidator.Mime mime = MimeValidator.getMimetype(attachment.getName());
        if (mime.type == MimeValidator.Mime.Type.IMAGE) {
            Glide.with(mContext).load(url).into(holder.bg_iv);
        } else {
            Glide.with(mContext).load(mime.icon).into(holder.bg_iv);
            holder.name_tv.setText(attachment.getName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AttachmentActivity.class);
                intent.putExtra("url", url);
                intent.putExtra("name", attachment.getName());
                intent.putExtra("mime", mime);

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAttachmentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bg_iv;
        TextView name_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bg_iv = itemView.findViewById(R.id.attachment_background_imageView);
            name_tv = itemView.findViewById(R.id.attachment_name_textView);
        }
    }
}
