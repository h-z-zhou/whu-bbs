package com.wuda.bbs.ui.article;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.ui.base.NavigationHost;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.HashMap;
import java.util.Map;

public class PostArticleFragment extends Fragment {

    private PostArticleViewModel mViewModel;
    private PostBoardViewModel mBoardViewModel;

    TextView boardName_tv;
    TextInputEditText title_et;
    TextInputEditText content_et;

    public static PostArticleFragment newInstance() {
        return new PostArticleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_article_fragment, container, false);


        boardName_tv = view.findViewById(R.id.postArticle_boardName_TextView);
        title_et = view.findViewById(R.id.postArticle_title_editText);
        content_et = view.findViewById(R.id.postArticle_content_editText);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PostArticleViewModel.class);
        mBoardViewModel = new ViewModelProvider(getActivity()).get(PostBoardViewModel.class);

        BaseBoard board = mBoardViewModel.boardMutableLiveData.getValue();
        if (board != null) {
            boardName_tv.setText(board.getName());
        }

        mBoardViewModel.boardMutableLiveData.observe(getViewLifecycleOwner(), new Observer<BaseBoard>() {
            @Override
            public void onChanged(BaseBoard board) {
                boardName_tv.setText(board.getName());
            }
        });

        boardName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigationTo(new SelectBoardFragment(), true);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.post_article_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_post_article) {
            postArticle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void postArticle(){
        Editable title = title_et.getText();
        Editable content = content_et.getText();
        if (title==null || content==null) {
            return;
        }

        if (title.length() == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("标题不可为空")
                    .setMessage("好看的人都写了，你怎么可以不写！")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        if (content.length() == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle("内容不可为空")
                    .setMessage("把你心里的想法告诉我吧！")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        // board=&relID=0&font=&subject=&Content=&signature=
        Map<String, String> form = new HashMap<>();
        form.put("board", mBoardViewModel.boardMutableLiveData.getValue().getId());
        form.put("relID", "0");
        form.put("font", "");
        form.put("subject", title.toString());
        form.put("Content", content.toString());
        form.put("signature", "");

        NetworkEntry.postArticle(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                String text = response.getContent().getResult();
            }
        });
    }
}