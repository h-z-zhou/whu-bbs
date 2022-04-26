package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.InfoBaseBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private final Context context;
    private List<InfoBaseBean> infoList;
    private AdapterItemListener<InfoBaseBean> mAdapterItemListener;

    public void setAdapterItemListener(AdapterItemListener<InfoBaseBean> mAdapterItemListener) {
        this.mAdapterItemListener = mAdapterItemListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView infoTitleTextView;
        TextView infoTimeTextView;
        TextView infoUnreadMarker;

        public ViewHolder(View view) {
            super(view);
            infoTitleTextView = view.findViewById(R.id.info_item_title);
            infoTimeTextView = view.findViewById(R.id.info_item_time);
            infoUnreadMarker = view.findViewById(R.id.info_item_unread_marker);
        }
    }

    public InfoAdapter(Context context, List<InfoBaseBean> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info, parent, false);
        ViewHolder holder = new ViewHolder(view);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InfoBaseBean info = infoList.get(holder.getAdapterPosition());
//                if (!info.isRead()) {
//                    info.setRead(true);
//                    holder.infoUnreadMarker.setVisibility(View.GONE);
//                }
//                Intent intent = new Intent(context, SubToolActivity.class);
////                Intent intent = new Intent(context, InfoDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("subTool", info);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterItemListener != null) {
                    int pos = holder.getAbsoluteAdapterPosition();
                    mAdapterItemListener.onItemClick(infoList.get(pos), pos);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InfoBaseBean info = infoList.get(position);
        holder.infoTitleTextView.setText(info.getTitle());
        if (!info.getTime().isEmpty())
            holder.infoTimeTextView.setText(info.getTime());
        else
            holder.infoTimeTextView.setVisibility(View.GONE);
        if (!info.isRead())
            holder.infoUnreadMarker.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    public void setInfoList(List<InfoBaseBean> infoList) {
        this.infoList = infoList;  // 更新引用
    }
}
