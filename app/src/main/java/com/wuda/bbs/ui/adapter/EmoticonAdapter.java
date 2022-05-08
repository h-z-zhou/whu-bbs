package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.utils.EmoticonUtil;

import java.util.List;

public class EmoticonAdapter extends RecyclerView.Adapter<EmoticonAdapter.ViewHolder> {
    Context mContext;
    List<String> emoticons;
    OnItemSelectListener mOnItemSelectListener;

    public EmoticonAdapter(Context context) {
        mContext = context;
        emoticons = EmoticonUtil.getNameList();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView iv = new ImageView(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(128, 128);
//        params.gravity = Gravity.CENTER;
        iv.setLayoutParams(params);
        return new ViewHolder(iv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drawable drawable = EmoticonUtil.getDrawable(emoticons.get(position));
        holder.em_iv.setLayoutParams(new ViewGroup.LayoutParams(drawable.getBounds().width(), drawable.getBounds().height()));
        Glide.with(mContext).load(drawable).into(holder.em_iv);
        if (mOnItemSelectListener != null) {
            holder.em_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemSelectListener.onSelect(emoticons.get(holder.getAbsoluteAdapterPosition()));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return emoticons.size();
    }

    public void setOnItemSelectListener(OnItemSelectListener mOnItemSelectListener) {
        this.mOnItemSelectListener = mOnItemSelectListener;
    }

    public interface OnItemSelectListener {
        public void onSelect(String emoticon);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView em_iv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            em_iv = (ImageView) itemView;
        }
    }
}
