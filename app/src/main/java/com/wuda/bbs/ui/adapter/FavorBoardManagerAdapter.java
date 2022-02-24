package com.wuda.bbs.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.logic.bean.DetailBoard;

import java.util.List;

public class FavorBoardManagerAdapter extends RecyclerView.Adapter<FavorBoardManagerAdapter.ViewHolder> {

    List<DetailBoard> allBoardList;

    public FavorBoardManagerAdapter(List<DetailBoard> allBoardList) {
        this.allBoardList = allBoardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item_tv.setText(allBoardList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return allBoardList.size();
    }

    public void updateAllBoardList(List<DetailBoard> allBoardList) {
        this.allBoardList = allBoardList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_tv = (TextView) itemView;
        }
    }
}
