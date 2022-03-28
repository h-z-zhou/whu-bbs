package com.wuda.bbs.ui.article;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.DetailArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.EmoticonAdapter;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.EmoticonUtil;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelFrameLayout;

public class ReplyActivity extends AppCompatActivity {

    ReplyViewModel mViewModel;

    ImageView close_btn;
    ImageView send_btn;
    EditText content_et;
    ImageView emoticon_iv;
    ImageView photo_iv;
    KPSwitchPanelFrameLayout panelFrameLayout;
    RecyclerView insertEmoticon_rv;
    RecyclerView insertPhoto_rv;

    EmoticonAdapter mEmoticonAdapter;

    @SuppressLint("ResourceAsColor")
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

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        getWindow().getDecorView().setBackgroundColor(R.color.transparent);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
//        getWindow().setBackgroundDrawableResource(R.drawable.bg_trans);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setDimAmount(0.5f);

        setFinishOnTouchOutside(true);

        close_btn = findViewById(R.id.reply_close_imageButton);
        send_btn = findViewById(R.id.reply_send_imageButton);

        content_et = findViewById(R.id.reply_content_inputEditText);

        // 需要加延时，不然无法弹出
//        new Handler().postDelayed(new Runnable(){
//            public void run() {
//                content_et.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                imm.showSoftInput(content_et, InputMethodManager.SHOW_IMPLICIT);
//            }
//        }, 200);

        emoticon_iv = findViewById(R.id.reply_emoticon_imageView);
        photo_iv = findViewById(R.id.reply_photo_imageView);
        panelFrameLayout = findViewById(R.id.reply_panel_root);
        insertEmoticon_rv = findViewById(R.id.reply_insert_emotion_recyclerView);
        insertPhoto_rv = findViewById(R.id.reply_insert_photo_recyclerView);

        KeyboardUtil.attach(this, panelFrameLayout);
        KPSwitchConflictUtil.attach(
                panelFrameLayout,
                content_et,
                new KPSwitchConflictUtil.SubPanelAndTrigger(insertEmoticon_rv, emoticon_iv),
                new KPSwitchConflictUtil.SubPanelAndTrigger(insertPhoto_rv, photo_iv)
        );

        insertEmoticon_rv.setLayoutManager(new GridLayoutManager(ReplyActivity.this, 6));
        insertEmoticon_rv.setAdapter(new EmoticonAdapter(ReplyActivity.this));

        eventBinding();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(panelFrameLayout);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void eventBinding() {

        mViewModel.getPostResultMutableLiveData().observe(ReplyActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
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

        ((EmoticonAdapter) insertEmoticon_rv.getAdapter()).setOnItemSelectListener(new EmoticonAdapter.OnItemSelectListener() {
            @Override
            public void onSelect(String emoticon) {
                content_et.append(emoticon);
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
                    .setMessage("内容不可为空")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        mViewModel.post();

    }
}