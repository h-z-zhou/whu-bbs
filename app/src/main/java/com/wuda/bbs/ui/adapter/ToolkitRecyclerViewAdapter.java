package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.ToolBean;

import java.util.List;


public class ToolkitRecyclerViewAdapter extends RecyclerView.Adapter<ToolkitRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<ToolBean> mValues;
    private AdapterItemListener<ToolBean> mAdapterItemListener;

    public void setAdapterItemListener(AdapterItemListener<ToolBean> mAdapterItemListener) {
        this.mAdapterItemListener = mAdapterItemListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_iv;
        TextView title_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_iv = itemView.findViewById(R.id.tool_icon_imgView);
            title_tv = itemView.findViewById(R.id.tool_title_textView);
        }
    }

    public ToolkitRecyclerViewAdapter(Context context, List<ToolBean> items) {
        mContext = context;
        mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_toolkit_tool, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterItemListener != null) {
                    int pos = holder.getBindingAdapterPosition();
                    mAdapterItemListener.onItemClick(mValues.get(pos), pos);
                }
            }
        });

        return holder;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ToolBean tool = mValues.get(position);
        holder.icon_iv.setImageDrawable(mContext.getDrawable(tool.getIconId()));
        holder.icon_iv.setColorFilter(Color.parseColor(tool.getIconColor()));
        holder.title_tv.setText(tool.getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}