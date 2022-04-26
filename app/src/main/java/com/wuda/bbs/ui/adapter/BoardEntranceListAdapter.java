package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.ui.board.BoardActivity;

import java.util.List;

public class BoardEntranceListAdapter extends BaseExpandableListAdapter {

    Context mContext;
    List<String> mSectionList;
    List<List<BaseBoard>> mSectionBoardList;

    public BoardEntranceListAdapter(Context mContext, List<String> mSectionList, List<List<BaseBoard>> mSectionBoardList) {
        this.mContext = mContext;
        this.mSectionList = mSectionList;
        this.mSectionBoardList = mSectionBoardList;
    }

    @Override
    public int getGroupCount() {
        return mSectionList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mSectionBoardList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mSectionList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mSectionBoardList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView section_tv = new TextView(parent.getContext());
        section_tv.setTextSize(18);
//        section_tv.setTypeface(null, Typeface.BOLD);
        section_tv.setPadding(108, 24, 8, 24);
        section_tv.setText(mSectionList.get(groupPosition));
        return section_tv;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView board_tv =  new TextView(parent.getContext());
        board_tv.setTextSize(16);
        board_tv.setPadding(160, 16, 0, 16);
        BaseBoard board = mSectionBoardList.get(groupPosition).get(childPosition);
        board_tv.setText(board.getName());
        board_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BoardActivity.class);
                intent.putExtra("board", board);
                mContext.startActivity(intent);
            }
        });
        return board_tv;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
