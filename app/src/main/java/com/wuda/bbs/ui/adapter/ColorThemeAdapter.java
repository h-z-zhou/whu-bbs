package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.utils.ThemeManager;

import java.util.List;

public class ColorThemeAdapter extends RecyclerView.Adapter<ColorThemeAdapter.ColorThemeViewHolder> {

    Context mContext;
    List<ThemeManager.ColorThemeItem> colorThemeList;

    AdapterItemListener<ThemeManager.ColorThemeItem> mItemListener;

    public ColorThemeAdapter(Context mContext) {
        this.mContext = mContext;
        colorThemeList = ThemeManager.getColorThemeList();
    }

    public void setItemListener(AdapterItemListener<ThemeManager.ColorThemeItem> mItemListener) {
        this.mItemListener = mItemListener;
    }

    @NonNull
    @Override
    public ColorThemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_theme_item, parent, false);
        ColorThemeViewHolder holder = new ColorThemeViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getBindingAdapterPosition();
                ThemeManager.ColorThemeItem item = colorThemeList.get(pos);
                mItemListener.onItemClick(item, pos);
            }
        });

        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ColorThemeViewHolder holder, int position) {
        ThemeManager.ColorThemeItem item = colorThemeList.get(position);
        int colorPrimary = ThemeManager.getThemePrimaryColor(mContext, item.getResId());

        int currentColorThemeID = ThemeManager.getCurrentColorThemeID();
        if (item.getResId() == currentColorThemeID) {
            holder.check_iv.setVisibility(View.VISIBLE);
        } else {
            holder.check_iv.setVisibility(View.GONE);
        }

        holder.color_iv.setColorFilter(colorPrimary);
    }

    @Override
    public int getItemCount() {
        return colorThemeList.size();
    }

    static class ColorThemeViewHolder extends RecyclerView.ViewHolder {

        ImageView color_iv;
        ImageView check_iv;

        public ColorThemeViewHolder(@NonNull View itemView) {
            super(itemView);

            color_iv = itemView.findViewById(R.id.theme_color_imageView);
            check_iv = itemView.findViewById(R.id.theme_check_imageView);
        }
    }

}
