package com.wuda.bbs.ui.article;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.DetailArticle;

public class ArticleLongClickBottomSheet extends BottomSheetDialogFragment {

    DetailArticle mArticle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_long_click_bottom_sheet, container, false);

        Button copyId_btn = view.findViewById(R.id.articleLongClick_copy_id_button);
        copyId_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText(null, mArticle.getAuthor()));
                    Toast.makeText(getContext(), "用户ID已复制", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        Button copyAll_btn = view.findViewById(R.id.articleLongClick_copy_all_button);
        copyAll_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    ClipboardManager cm = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setPrimaryClip(ClipData.newPlainText(null, mArticle.getContent()));
                    Toast.makeText(getContext(), "内容已复制", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        Button copyAny_btn = view.findViewById(R.id.articleLongClick_copy_any_button);
        copyAny_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectContentActivity.class);
                intent.putExtra("content", mArticle.getContent());
                startActivity(intent);
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        if (mArticle == null) {
            return;
        }
        super.show(manager, tag);
    }

    public void setArticle(DetailArticle article) {
        this.mArticle = article;
    }
}
