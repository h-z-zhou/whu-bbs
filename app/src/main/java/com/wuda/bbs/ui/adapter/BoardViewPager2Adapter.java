package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.Board;

import java.util.List;

public class BoardViewPager2Adapter extends RecyclerView.Adapter<BoardViewPager2Adapter.ViewHolder> {

    List<Board> mBoardList;
    Context mContext;

    public BoardViewPager2Adapter(Context context, List<Board> boardList) {
        mContext = context;
        mBoardList = boardList;
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
        return mBoardList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView board_rv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            board_rv = itemView.findViewById(R.id.article_recyclerView);
        }
    }

    public void updateBoardList(List<Board> mBoardList) {
        this.mBoardList = mBoardList;
    }
}
