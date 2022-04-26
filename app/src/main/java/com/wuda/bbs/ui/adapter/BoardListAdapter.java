package com.wuda.bbs.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;


import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableAdapter;

public class BoardListAdapter extends ExpandableAdapter<ExpandableAdapter.ViewHolder> {

    Context mContext;
    List<String> mSectionList;
    List<List<BaseBoard>> mSectionBoardList;
    OnBoardSelectedListener mOnBoardSelectedListener;

    public BoardListAdapter(Context mContext, List<String> mSectionList, List<List<BaseBoard>> mSectionBoardList) {
        this.mContext = mContext;
        this.mSectionList = mSectionList;
        this.mSectionBoardList = mSectionBoardList;
    }

    public void setOnBoardSelectedListener(OnBoardSelectedListener onBoardSelectedListener) {
        this.mOnBoardSelectedListener = onBoardSelectedListener;
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
                if (mOnBoardSelectedListener != null) {
                    mOnBoardSelectedListener.onBoardSelected(board);
                }

            }
        });
    }

    @Override
    protected void onBindGroupViewHolder(@NonNull ViewHolder viewHolder, int i, boolean b, @NonNull List<?> list) {
        ((SectionHolder) viewHolder).sectionName_tv.setText(mSectionList.get(i));
    }

    @NonNull
    @Override
    protected ViewHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TextView board_tv = new TextView(viewGroup.getContext());
        board_tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        board_tv.setTextSize(16);
        board_tv.setPadding(128, 32, 0, 32);
        board_tv.setBackgroundColor(mContext.getColor(R.color.bg));
        return new BoardHolder(board_tv);
    }

    @NonNull
    @Override
    protected ViewHolder onCreateGroupViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_section_item, viewGroup, false);
        return new SectionHolder(view);
    }

    @Override
    protected void onGroupViewHolderExpandChange(@NonNull ViewHolder viewHolder, int i, long l, boolean b) {
        if (viewHolder instanceof SectionHolder) {
            ImageView arrow_iv = ((SectionHolder) viewHolder).sectionArrow_iv;
            if (b) {
                ObjectAnimator.ofFloat(arrow_iv, View.ROTATION, -90.0f).start();
            } else {
                ObjectAnimator.ofFloat(arrow_iv, View.ROTATION, 0.0f).start();
            }
        }
    }

    static class SectionHolder extends ExpandableAdapter.ViewHolder {
        TextView sectionName_tv;
        ImageView sectionArrow_iv;
        public SectionHolder(@NonNull View itemView) {
            super(itemView);
            sectionName_tv = itemView.findViewById(R.id.board_sectionName_textView);
            sectionArrow_iv = itemView.findViewById(R.id.board_sectionArrow_image);

        }
    }

    static class BoardHolder extends ExpandableAdapter.ViewHolder {
        TextView board_tv;
        public BoardHolder(@NonNull View itemView) {
            super(itemView);
            board_tv = (TextView) itemView;
        }
    }

    public interface OnBoardSelectedListener {
        public void onBoardSelected(BaseBoard board);
    }
}
