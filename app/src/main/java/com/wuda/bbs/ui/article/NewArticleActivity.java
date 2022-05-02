package com.wuda.bbs.ui.article;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.material.textfield.TextInputEditText;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.utils.DensityUtil;
import com.luck.picture.lib.utils.MediaUtils;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.BaseBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.EmoticonAdapter;
import com.wuda.bbs.ui.adapter.GridAttachmentAdapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.FullyGridLayoutManager;
import com.wuda.bbs.ui.widget.InfoDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.GlideEngine;

import java.util.ArrayList;
import java.util.List;

import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelFrameLayout;

public class NewArticleActivity extends CustomizedThemeActivity {

    private TextView boardName_tv;
    private TextInputEditText title_et;
    private TextInputEditText content_et;
    private RecyclerView attachment_rv;

    ImageView emoticon_iv;
    ImageView photo_iv;
    KPSwitchPanelFrameLayout panelFrameLayout;
    RecyclerView emoticon_rv;

    private EmoticonAdapter mEmoticonAdapter;

    private GridAttachmentAdapter mAttachmentAdapter;
    private final List<LocalMedia> mAttachmentList = new ArrayList<>();

    ActivityResultLauncher<Intent> boardSelectorLauncher;

    private NewArticleViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_article);

        mViewModel = new ViewModelProvider(NewArticleActivity.this).get(NewArticleViewModel.class);
        BaseBoard board = (BaseBoard) getIntent().getSerializableExtra("board");
        if (board == null) {
            finish();
        } else {
            mViewModel.getBoardMutableLiveData().postValue(board);
        }

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("发帖子");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        boardName_tv = findViewById(R.id.newArticle_boardName_TextView);
        title_et = findViewById(R.id.newArticle_title_editText);
        content_et = findViewById(R.id.newArticle_content_inputEditText);
        attachment_rv = findViewById(R.id.newArticle_attachment_recyclerView);

        photo_iv = findViewById(R.id.post_action_bar_photo_imageView);

        emoticon_iv = findViewById(R.id.post_action_bar_emoticon_imageView);
        panelFrameLayout = findViewById(R.id.keyboard_panel_root);
        emoticon_rv = findViewById(R.id.newArticle_emotion_recyclerView);

        emoticon_rv.setLayoutManager(new FullyGridLayoutManager(NewArticleActivity.this, 6));
        emoticon_rv.addItemDecoration(new GridSpacingItemDecoration(3,
                DensityUtil.dip2px(NewArticleActivity.this, 8), false));
        mEmoticonAdapter = new EmoticonAdapter(NewArticleActivity.this);
        emoticon_rv.setAdapter(mEmoticonAdapter);

        attachment_rv.setLayoutManager(new FullyGridLayoutManager(NewArticleActivity.this, 3, RecyclerView.VERTICAL, false));
        RecyclerView.ItemAnimator itemAnimator = attachment_rv.getItemAnimator();
        if (itemAnimator != null) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        attachment_rv.addItemDecoration(new GridSpacingItemDecoration(3,
                DensityUtil.dip2px(NewArticleActivity.this, 4), false));
        mAttachmentAdapter = new GridAttachmentAdapter(NewArticleActivity.this, mAttachmentList);
        mAttachmentAdapter.setSelectMax(9);
        attachment_rv.setAdapter(mAttachmentAdapter);
        attachment_rv.setVisibility(View.GONE);

        boardSelectorLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode()==RESULT_OK && result.getData()!=null) {
                    BaseBoard board = (BaseBoard) result.getData().getSerializableExtra("board");
                    mViewModel.getBoardMutableLiveData().postValue(board);
                }
            }
        });


        eventBinding();

        // 需要加延时，不然无法弹出
        new Handler().postDelayed(new Runnable(){
            public void run() {
                title_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(title_et, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    private void eventBinding() {

        boardName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardSelectorLauncher.launch(new Intent(NewArticleActivity.this, SelectBoardActivity.class));
            }
        });

        mViewModel.getBoardMutableLiveData().observe(NewArticleActivity.this, new Observer<BaseBoard>() {
            @Override
            public void onChanged(BaseBoard baseBoard) {
                mViewModel.attachmentState = 0;
                mViewModel.detectAttachable();
                boardName_tv.setText(baseBoard.getName());

                mViewModel.localMediaList = null;
                attachment_rv.setVisibility(View.GONE);
            }
        });

        mViewModel.getPostedResultMutableLiveData().observe(NewArticleActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(NewArticleActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(NewArticleActivity.this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(NewArticleActivity.this)
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                postArticle();
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


        KeyboardUtil.attach(this, panelFrameLayout);
        KPSwitchConflictUtil.attach(
                panelFrameLayout,
                content_et,
                new KPSwitchConflictUtil.SubPanelAndTrigger(emoticon_rv, emoticon_iv)
        );

        mEmoticonAdapter.setOnItemSelectListener(new EmoticonAdapter.OnItemSelectListener() {
            @Override
            public void onSelect(String emoticon) {
                content_et.append(emoticon);
            }
        });

        mAttachmentAdapter.setOnItemClickListener(
                new GridAttachmentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // 预览图片、视频、音频
                        PictureSelector.create(NewArticleActivity.this)
                                .openPreview()
                                .setImageEngine(new GlideEngine())
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
                        PictureSelector.create(NewArticleActivity.this)
                                .openGallery(SelectMimeType.ofImage())
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isDisplayCamera(false)
                                .setSelectedData(mAttachmentAdapter.getData())
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(ArrayList<LocalMedia> result) {
                                        analyticalSelectResults(result);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }
                }
        );

        photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mViewModel.attachmentState) {
                    case -1:
                        Toast.makeText(NewArticleActivity.this, "该版块不可上传附件", Toast.LENGTH_SHORT).show();
                        return;
                    case 0:
                        Toast.makeText(NewArticleActivity.this, "正在判断是否可以上传附件", Toast.LENGTH_SHORT).show();
                        mViewModel.detectAttachable();
                        return;
                    case 1:
                        PictureSelector.create(NewArticleActivity.this)
                                .openGallery(SelectMimeType.ofImage())
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .isDisplayCamera(false)
                                .setSelectedData(mAttachmentAdapter.getData())
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(ArrayList<LocalMedia> result) {
                                        analyticalSelectResults(result);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                        // 先点开表情框，后点图片，此时无法消失
                        if (panelFrameLayout.isVisible()) {
                            panelFrameLayout.setVisibility(View.GONE);
                        }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.post_article_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
            new InfoDialog(NewArticleActivity.this)
                    .setInfo("没有标题哟！")
                    .show();
            return;
        }

        if (content.length() == 0) {
            new InfoDialog(NewArticleActivity.this)
                    .setInfo("说点啥呢！")
                    .show();
            return;
        }

        mViewModel.title = title.toString();
        mViewModel.content = content.toString();

        mViewModel.post();
    }

    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        long size = 5*1024*1024;
        ArrayList<LocalMedia> medias = new ArrayList<>();

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

        for (LocalMedia media: result) {
            size -= media.getSize();
            if (size > 0) {
                medias.add(media);
            } else {
                Toast.makeText(NewArticleActivity.this, "文件总大小不得超过5M", Toast.LENGTH_LONG).show();
                break;
            }
        }

        mViewModel.localMediaList = medias;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                attachment_rv.setVisibility(View.VISIBLE);

                boolean isMaxSize = medias.size() == mAttachmentAdapter.getSelectMax();
                int oldSize = mAttachmentAdapter.getData().size();
                mAttachmentAdapter.notifyItemRangeRemoved(0, isMaxSize ? oldSize + 1 : oldSize);
                mAttachmentAdapter.getData().clear();

                mAttachmentAdapter.getData().addAll(medias);
                mAttachmentAdapter.notifyItemRangeInserted(0, medias.size());
            }
        });
    }

}