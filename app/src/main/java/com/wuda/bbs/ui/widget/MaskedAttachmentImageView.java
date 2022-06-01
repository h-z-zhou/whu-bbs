package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.Attachment;
import com.wuda.bbs.ui.article.AttachmentActivity;
import com.wuda.bbs.utils.validator.MimeValidator;

import java.io.Serializable;
import java.util.List;

public class MaskedAttachmentImageView extends RelativeLayout {

    private Context mContext;
    private ImageView imageView;

    String mBoardId;
    String mArticleId;
    List<Attachment> mAttachmentList;

    public MaskedAttachmentImageView(Context context) {
        super(context);

        mContext = context;

        imageView = new ImageView(context);
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imageView);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AttachmentActivity.class);
                intent.putExtra("attachments", (Serializable) mAttachmentList);
                intent.putExtra("board", mBoardId);
                intent.putExtra("articleId", mArticleId);
                intent.putExtra("position", 0);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
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

    public void addAttachments(@NonNull List<Attachment> attachmentList) {
        mAttachmentList = attachmentList;

        Attachment attachment = attachmentList.get(0);

        MimeValidator.Mime mime = MimeValidator.getMimetype(attachment.getName());
        if (mime.type == MimeValidator.Mime.Type.IMAGE) {
            Glide.with(mContext)
                    .load(attachment.getUrl())
                    .placeholder(R.drawable.mimetype_image)
                    .into(imageView);
        } else {
            Glide.with(mContext)
                    .load(mime.icon).
                    into(imageView);
        }
        if (attachmentList.size() > 1) {

            SpannableStringBuilder text = new SpannableStringBuilder();
            text.append("+" + attachmentList.size(), new ForegroundColorSpan(Color.WHITE), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            TextView textView = new TextView(mContext);
            RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textView.setLayoutParams(params);
            textView.setBackgroundColor(Color.LTGRAY);
            textView.getBackground().setAlpha(70);
            textView.setText(text);
            textView.setGravity(Gravity.END);
            textView.setTextSize(16);
            this.addView(textView);
        }
    }

}
