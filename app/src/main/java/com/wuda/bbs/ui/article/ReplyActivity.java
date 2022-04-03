package com.wuda.bbs.ui.article;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.DetailArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

public class ReplyActivity extends AppCompatActivity {

    ReplyViewModel mViewModel;

    ImageView close_btn;
    ImageView send_btn;
    TextInputEditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DetailArticle repliedArticle = (DetailArticle) getIntent().getSerializableExtra("article");
        String groupId = getIntent().getStringExtra("groupId");
        String boardId = getIntent().getStringExtra("boardId");
        if (repliedArticle == null)
            finish();

        mViewModel = new ViewModelProvider(ReplyActivity.this).get(ReplyViewModel.class);
        mViewModel.repliedArticle = repliedArticle;
        mViewModel.groupId = groupId;
        mViewModel.boardId = boardId;


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reply);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setFinishOnTouchOutside(true);

        close_btn = findViewById(R.id.reply_close_imageButton);
        send_btn = findViewById(R.id.reply_send_imageButton);

        content_et = findViewById(R.id.newArticle_content_inputEditText);

        // 需要加延时，不然无法弹出
        new Handler().postDelayed(new Runnable(){
            public void run() {
                content_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(content_et, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);

        eventBinding();
    }

    private void eventBinding() {

        mViewModel.getPostResultMutableLiveData().observe(ReplyActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
//                Toast.makeText(ReplyActivity.this, s, Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(ReplyActivity.this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(ReplyActivity.this)
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.post();
                            }
                        })
                        .setOnNegativeButtonClickedLister(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                finish();
                            }
                        })
                        .show();
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.content = content_et.getText().toString();
                reply();
            }
        });
    }

    private void reply(){
        Editable content = content_et.getText();
        if (content==null) {
            return;
        }

        if (content.length() == 0) {
            new AlertDialog.Builder(ReplyActivity.this)
                    .setTitle("内容不可为空")
                    .setMessage("把你心里的想法告诉我吧！")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        mViewModel.post();
    }
}