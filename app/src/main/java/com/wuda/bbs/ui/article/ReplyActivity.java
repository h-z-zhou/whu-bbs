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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.utils.DensityUtil;
import com.luck.picture.lib.utils.MediaUtils;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.logic.bean.bbs.DetailArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.EmoticonAdapter;
import com.wuda.bbs.ui.adapter.GridAttachmentAdapter;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.FullyGridLayoutManager;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelFrameLayout;
import cn.dreamtobe.kpswitch.widget.KPSwitchRootLinearLayout;

public class ReplyActivity extends AppCompatActivity {

    ReplyViewModel mViewModel;

    KPSwitchRootLinearLayout root_ll;

    ImageView close_btn;
    ImageView send_btn;
    ProgressBar pBar;

    EditText content_et;
    ImageView emoticon_iv;
    ImageView photo_iv;

    KPSwitchPanelFrameLayout panelFrameLayout;
    RecyclerView insertEmoticon_rv;
    RecyclerView insertPhoto_rv;
    private GridAttachmentAdapter mAttachmentAdapter;
    private final List<LocalMedia> mData = new ArrayList<>();

    boolean posting = false;

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
        mViewModel.getBoardMutableLiveData().postValue(new BaseBoard(boardId, ""));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reply);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setDimAmount(0.5f);

        setFinishOnTouchOutside(true);

        root_ll = findViewById(R.id.reply_root);

        close_btn = findViewById(R.id.reply_close_imageButton);
        send_btn = findViewById(R.id.reply_send_imageButton);
        pBar = findViewById(R.id.reply_progressBar);

        content_et = findViewById(R.id.newArticle_content_inputEditText);
        content_et.setHint("回复@" + repliedArticle.getAuthor());

        emoticon_iv = findViewById(R.id.post_action_bar_emoticon_imageView);
        photo_iv = findViewById(R.id.post_action_bar_photo_imageView);
        panelFrameLayout = findViewById(R.id.keyboard_panel_root);
        insertEmoticon_rv = findViewById(R.id.newArticle_emotion_recyclerView);
        insertPhoto_rv = findViewById(R.id.new_insert_photo_recyclerView);

        KeyboardUtil.attach(this, panelFrameLayout);
        KPSwitchConflictUtil.attach(
                panelFrameLayout,
                content_et,
                new KPSwitchConflictUtil.SubPanelAndTrigger(insertEmoticon_rv, emoticon_iv),
                new KPSwitchConflictUtil.SubPanelAndTrigger(insertPhoto_rv, photo_iv)
        );

        insertEmoticon_rv.setLayoutManager(new FullyGridLayoutManager(ReplyActivity.this, 6));
        insertEmoticon_rv.addItemDecoration(new GridSpacingItemDecoration(3,
                DensityUtil.dip2px(ReplyActivity.this, 8), false));
        insertEmoticon_rv.setAdapter(new EmoticonAdapter(ReplyActivity.this));

        insertPhoto_rv.setLayoutManager(new FullyGridLayoutManager(ReplyActivity.this, 3, RecyclerView.VERTICAL, false));
        mAttachmentAdapter = new GridAttachmentAdapter(ReplyActivity.this, mData);
        mAttachmentAdapter.setSelectMax(9);
        insertPhoto_rv.setAdapter(mAttachmentAdapter);

        ImageEngine imageEngine = new GlideEngine();
        mAttachmentAdapter.setOnItemClickListener(new GridAttachmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 预览图片、视频、音频
                PictureSelector.create(ReplyActivity.this)
                        .openPreview()
                        .setImageEngine(imageEngine)
                        .setExternalPreviewEventListener(new OnExternalPreviewEventListener() {
                            @Override
                            public void onPreviewDelete(int position) {
                                mAttachmentAdapter.remove(position);
                                mAttachmentAdapter.notifyItemRemoved(position);
                            }

                            @Override
                            public boolean onLongPressDownload(LocalMedia media) {
                                return false;
                            }
                        })
                        .startActivityPreview(position, true, mAttachmentAdapter.getData());
            }

            @Override
            public void openPicture() {

                switch (mViewModel.attachmentState) {
                    case -1:
                        Toast.makeText(ReplyActivity.this, "该版块不可上传附件", Toast.LENGTH_SHORT).show();
                        return;
                    case 0:
                        Toast.makeText(ReplyActivity.this, "正在判断是否可以上传附件, 稍候重试", Toast.LENGTH_SHORT).show();
                        mViewModel.detectAttachable();
                        return;
                    case 1:
                        PictureSelector.create(ReplyActivity.this)
                                .openGallery(SelectMimeType.ofImage())
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isDisplayCamera(false)
                                .setSelectedData(mAttachmentAdapter.getData())
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(ArrayList<LocalMedia> result) {
                                        mViewModel.localMediaList = result;
                                        analyticalSelectResults(result);
                                    }
                                    @Override
                                    public void onCancel() {

                                    }
                                });
                }
            }
        });

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
                setResult(RESULT_OK);
                finish();
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(ReplyActivity.this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {

                pBar.setVisibility(View.GONE);

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

        root_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (posting) {
                    Toast.makeText(ReplyActivity.this, "回复中，请稍候", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
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
                pBar.setVisibility(View.VISIBLE);
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
            Toast.makeText(ReplyActivity.this, "内容不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        posting = true;
        mViewModel.post();

    }

    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(PictureAppMaster.getInstance().getAppContext(), media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isMaxSize = result.size() == mAttachmentAdapter.getSelectMax();
                int oldSize = mAttachmentAdapter.getData().size();
                mAttachmentAdapter.notifyItemRangeRemoved(0, isMaxSize ? oldSize + 1 : oldSize);
                mAttachmentAdapter.getData().clear();

                mAttachmentAdapter.getData().addAll(result);
                mAttachmentAdapter.notifyItemRangeInserted(0, result.size());
            }
        });
    }
}