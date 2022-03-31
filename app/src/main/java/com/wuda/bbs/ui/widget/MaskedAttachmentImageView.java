package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Attachment;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.validator.MimeValidator;

import java.util.List;

public class MaskedAttachmentImageView extends RelativeLayout {

    private Context mContext;
    private ImageView imageView;
    private TextView textView;

    String mBoardId;
    String mArticleId;
    List<Attachment> mAttachmentList;

    public MaskedAttachmentImageView(Context context) {
        super(context);

        mContext = context;

        imageView = new ImageView(context);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(imageView);

        textView = new TextView(context);
        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        textView.setLayoutParams(params);
        textView.setBackgroundColor(Color.LTGRAY);
        textView.getBackground().setAlpha(100);

        textView.setVisibility(GONE);
    }

    public MaskedAttachmentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskedAttachmentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setMaskText(String text) {
        this.textView.setText(text);
        this.textView.setVisibility(VISIBLE);
    }

    public void addAttachments(@NonNull List<Attachment> attachmentList, @NonNull String boardId, @NonNull String articleId) {
        mAttachmentList = attachmentList;
        mBoardId = boardId;
        mArticleId = articleId;

        Attachment attachment = attachmentList.get(0);
        String url;
        url = NetConst.ATTACHMENT + "?board=" + mBoardId + "&id=" + mArticleId + "&ap=" + attachment.getId();

        MimeValidator.Mime mime = MimeValidator.getMimetype(attachment.getName());
        if (mime.type == MimeValidator.Mime.Type.IMAGE) {
            Glide.with(mContext).load(url).placeholder(R.drawable.mimetype_image).into(imageView);
        } else {
            Glide.with(mContext).load(mime.icon).into(imageView);
        }
        if (attachmentList.size() > 1) {
            setMaskText("还有" + Integer.valueOf(attachmentList.size()-1).toString());
        }
    }
}
