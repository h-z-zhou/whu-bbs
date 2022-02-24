package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.ui.main.board.BoardActivity;


import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableAdapter;

public class BoardEntranceAdapter extends ExpandableAdapter<ExpandableAdapter.ViewHolder> {

    Context mContext;
    List<String> mSectionList;
    List<List<BaseBoard>> mSectionBoardList;

    public BoardEntranceAdapter(Context mContext, List<String> mSectionList, List<List<BaseBoard>> mSectionBoardList) {
        this.mContext = mContext;
        this.mSectionList = mSectionList;
        this.mSectionBoardList = mSectionBoardList;
    }

    @Override
    public int getChildCount(int i) {
        return mSectionBoardList.get(i).size();
    }

    @Override
    public int getGroupCount() {
        return mSectionList.size();
    }

    @Override
    protected void onBindChildViewHolder(@NonNull ViewHolder viewHolder, int i, int i1, @NonNull List<?> list) {
        BaseBoard board = mSectionBoardList.get(i).get(i1);
        ((BoardHolder) viewHolder).board_tv.setText(board.getName());
        ((BoardHolder) viewHolder).board_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BoardActivity.class);
                intent.putExtra("board", board);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void onBindGroupViewHolder(@NonNull ViewHolder viewHolder, int i, boolean b, @NonNull List<?> list) {
        ((SectionHolder) viewHolder).section_tv.setText(mSectionList.get(i));
    }

    @NonNull
    @Override
    protected ViewHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView board_tv = new TextView(viewGroup.getContext());
        board_tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        board_tv.setTextSize(16);
        board_tv.setPadding(128, 16, 0, 16);
        return new BoardHolder(board_tv);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateGroupViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView section_tv = new TextView(viewGroup.getContext());
        section_tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        section_tv.setTextSize(18);
//        section_tv.setTypeface(null, Typeface.BOLD);
        section_tv.setPadding(16, 24, 8, 24);
        return new SectionHolder(section_tv);
    }

    @Override
    protected void onGroupViewHolderExpandChange(@NonNull ViewHolder viewHolder, int i, long l, boolean b) {

    }

    static class SectionHolder extends ExpandableAdapter.ViewHolder {
        TextView section_tv;
        public SectionHolder(@NonNull View itemView) {
            super(itemView);
            section_tv = (TextView) itemView;
        }
    }

    static class BoardHolder extends ExpandableAdapter.ViewHolder {
        TextView board_tv;
        public BoardHolder(@NonNull View itemView) {
            super(itemView);
            board_tv = (TextView) itemView;
        }
    }
}
