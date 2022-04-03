package com.wuda.bbs.ui.board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wuda.bbs.R;
import com.wuda.bbs.ui.article.NewArticleActivity;

public class BoardArticleFabFragment extends BoardArticleFragment {


    @SuppressLint("UseCompatLoadingForDrawables")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        FloatingActionButton writeArticle_fab = new FloatingActionButton(requireContext());
        writeArticle_fab.setImageDrawable(requireContext().getDrawable(R.drawable.ic_create));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.bottomMargin = 32;
        params.rightMargin = 32;
        writeArticle_fab.setLayoutParams(params);

        writeArticle_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewArticleActivity.class);
                intent.putExtra("board", board);
                startActivity(intent);
            }
        });

        article_root_fl.addView(writeArticle_fab);

        return view;
    }

}
