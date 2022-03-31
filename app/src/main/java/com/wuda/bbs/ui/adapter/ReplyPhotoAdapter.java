package com.wuda.bbs.ui.adapter;

import static android.os.Build.VERSION_CODES.R;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.application.BBSApplication;

public class ReplyPhotoAdapter extends RecyclerView.Adapter<ReplyPhotoAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView iv = new ImageView(parent.getContext());
        iv.setLayoutParams(new LinearLayout.LayoutParams(300, 300, Gravity.CENTER_VERTICAL));
        return new ViewHolder(iv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.iv.setBackgroundColor(Color.BLUE);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = (ImageView) itemView;
        }
    }
}
