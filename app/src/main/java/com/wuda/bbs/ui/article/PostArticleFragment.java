package com.wuda.bbs.ui.article;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
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
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.GridAttachmentAdapter;
import com.wuda.bbs.ui.base.NavigationHost;
import com.wuda.bbs.ui.widget.FullyGridLayoutManager;
import com.wuda.bbs.utils.GlideEngine;
import com.wuda.bbs.utils.networkResponseHandler.AttachmentDetectHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostArticleFragment extends Fragment {

    private PostArticleViewModel mArticleViewModel;
    private PostBoardViewModel mBoardViewModel;

    private TextView boardName_tv;
    private TextInputEditText title_et;
    private TextInputEditText content_et;
    private RecyclerView attachment_rv;
    private GridAttachmentAdapter mAttachmentAdapter;
    private final List<LocalMedia> mData = new ArrayList<>();

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
        attachment_rv = view.findViewById(R.id.postArticle_attachment_recyclerView);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArticleViewModel = new ViewModelProvider(this).get(PostArticleViewModel.class);
        mBoardViewModel = new ViewModelProvider(getActivity()).get(PostBoardViewModel.class);

        title_et.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(title_et, InputMethodManager.SHOW_IMPLICIT);

        BaseBoard board = mBoardViewModel.boardMutableLiveData.getValue();
        if (board != null) {
            boardName_tv.setText(board.getName());
        }

        mBoardViewModel.boardMutableLiveData.observe(getViewLifecycleOwner(), new Observer<BaseBoard>() {
            @Override
            public void onChanged(BaseBoard board) {
                boardName_tv.setText(board.getName());
                mArticleViewModel.attachmentState = 0;
                detectAttachable(false);
            }
        });

        boardName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigationTo(new SelectBoardFragment(), true);
            }
        });

        FullyGridLayoutManager manager = new FullyGridLayoutManager(getContext(),
                3, GridLayoutManager.VERTICAL, false);
        attachment_rv.setLayoutManager(manager);
        RecyclerView.ItemAnimator itemAnimator = attachment_rv.getItemAnimator();
        if (itemAnimator != null) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        attachment_rv.addItemDecoration(new GridSpacingItemDecoration(3,
                DensityUtil.dip2px(getContext(), 8), false));
        mAttachmentAdapter = new GridAttachmentAdapter(getContext(), mData);
        mAttachmentAdapter.setSelectMax(9);
        attachment_rv.setAdapter(mAttachmentAdapter);
//        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
//            mData.clear();
//            mData.addAll(savedInstanceState.getParcelableArrayList("selectorList"));
//        }


        ImageEngine imageEngine = new GlideEngine();
        mAttachmentAdapter.setOnItemClickListener(new GridAttachmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 预览图片、视频、音频
                PictureSelector.create(getContext())
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

                switch (mArticleViewModel.attachmentState) {
                    case -1:
                        Toast.makeText(getContext(), "该版块不可上传附件", Toast.LENGTH_SHORT).show();
                        return;
                    case 0:
                        Toast.makeText(getContext(), "正在判断是否可以上传附件", Toast.LENGTH_SHORT).show();
                        detectAttachable(true);
                        return;
                    case 1:
                        PictureSelector.create(getContext())
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
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == PictureConfig.REQUEST_CAMERA) {
                ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                analyticalSelectResults(result);
            }
        } else if (resultCode == RESULT_CANCELED) {
//            Log.i(TAG, "onActivityResult PictureSelector Cancel");
        }
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
//            Log.i(TAG, "文件名: " + media.getFileName());
//            Log.i(TAG, "是否压缩:" + media.isCompressed());
//            Log.i(TAG, "压缩:" + media.getCompressPath());
//            Log.i(TAG, "原图:" + media.getPath());
//            Log.i(TAG, "绝对路径:" + media.getRealPath());
//            Log.i(TAG, "是否裁剪:" + media.isCut());
//            Log.i(TAG, "裁剪:" + media.getCutPath());
//            Log.i(TAG, "是否开启原图:" + media.isOriginal());
//            Log.i(TAG, "原图路径:" + media.getOriginalPath());
//            Log.i(TAG, "沙盒路径:" + media.getSandboxPath());
//            Log.i(TAG, "原始宽高: " + media.getWidth() + "x" + media.getHeight());
//            Log.i(TAG, "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
//            Log.i(TAG, "文件大小: " + media.getSize());
        }
        getActivity().runOnUiThread(new Runnable() {
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

    private void detectAttachable(boolean echoOnResponse) {
        Map<String, String> form = new HashMap<>();
        form.put("board", mBoardViewModel.boardMutableLiveData.getValue().getId());
        NetworkEntry.detectAttachment(form, new AttachmentDetectHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Boolean> response) {
                if (response.isSuccessful()) {
                    boolean isAttachable = response.getContent();
                    mArticleViewModel.attachmentState = isAttachable? 1: -1;
                    if (echoOnResponse) {
                        String info = isAttachable? "判断成功：该版块可以上传附件": "判断成功：该版块不可以上传附件";
                        Toast.makeText(getContext(), info, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}