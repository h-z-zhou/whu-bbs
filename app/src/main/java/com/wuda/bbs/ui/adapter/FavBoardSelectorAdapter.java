package com.wuda.bbs.ui.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.BaseBoard;

import java.util.List;

import pokercc.android.expandablerecyclerview.ExpandableAdapter;

public class FavBoardSelectorAdapter extends ExpandableAdapter<ExpandableAdapter.ViewHolder> {
    Context mContext;
    List<String> mSectionList;
    List<List<Pair<BaseBoard, Boolean>>> mSectionBoardList;
    OnBoardFavChangedListener mOnBoardFavChangedListener;

    public FavBoardSelectorAdapter(Context mContext, List<String> mSectionList, List<List<Pair<BaseBoard, Boolean>>> mSectionBoardList) {
        this.mContext = mContext;
        this.mSectionList = mSectionList;
        this.mSectionBoardList = mSectionBoardList;
    }

    public void setOnBoardFavChangedListener(OnBoardFavChangedListener mOnBoardFavChangedListener) {
        this.mOnBoardFavChangedListener = mOnBoardFavChangedListener;
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
    protected void onBindChildViewHolder(@NonNull ExpandableAdapter.ViewHolder viewHolder, int i, int i1, @NonNull List<?> list) {
        Pair<BaseBoard, Boolean> boardSelection = mSectionBoardList.get(i).get(i1);
        ((BoardHolder) viewHolder).board_cb.setText(boardSelection.first.getName());
        ((BoardHolder) viewHolder).board_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOnBoardFavChangedListener.onBoardFavChanged(boardSelection.first, isChecked);
            }
        });
        ((BoardHolder) viewHolder).board_cb.setChecked(boardSelection.second);
    }

    @Override
    protected void onBindGroupViewHolder(@NonNull ExpandableAdapter.ViewHolder viewHolder, int i, boolean b, @NonNull List<?> list) {
        ((SectionHolder) viewHolder).sectionName_tv.setText(mSectionList.get(i));
    }

    @NonNull
    @Override
    protected ExpandableAdapter.ViewHolder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fav_board_selector_item, viewGroup, false);
        return new BoardHolder(view);
    }

    @NonNull
    @Override
    protected ExpandableAdapter.ViewHolder onCreateGroupViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.board_section_item, viewGroup, false);
        return new SectionHolder(view);
    }

    @Override
    protected void onGroupViewHolderExpandChange(@NonNull ExpandableAdapter.ViewHolder viewHolder, int i, long l, boolean b) {
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
        CheckBox board_cb;
        public BoardHolder(@NonNull View itemView) {
            super(itemView);
            board_cb = itemView.findViewById(R.id.board_checkBox);
        }
    }

    public static interface OnBoardFavChangedListener {
        public void onBoardFavChanged(BaseBoard board, boolean isFav);
    }
}
