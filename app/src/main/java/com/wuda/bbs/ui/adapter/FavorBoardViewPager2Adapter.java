package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.DetailBoard;

import java.util.List;

public class FavorBoardViewPager2Adapter extends RecyclerView.Adapter<FavorBoardViewPager2Adapter.ViewHolder> {

    List<DetailBoard> mDetailBoardList;
    Context mContext;

    public FavorBoardViewPager2Adapter(Context context, List<DetailBoard> detailBoardList) {
        mContext = context;
        mDetailBoardList = detailBoardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDetailBoardList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView board_rv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            board_rv = itemView.findViewById(R.id.recyclerView);
        }
    }

    public void updateBoardList(List<DetailBoard> mDetailBoardList) {
        this.mDetailBoardList = mDetailBoardList;
    }
}
