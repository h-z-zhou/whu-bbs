package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Attachment;
import com.wuda.bbs.ui.article.ImageActivity;
import com.wuda.bbs.utils.EmoticonUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArticleTextView extends FixedMovementTextView {
    public ArticleTextView(Context context) {
        super(context);
    }

    public ArticleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArticleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //
    List<Attachment> attachmentList;
    String boardId;
    String articleId;


    public void setContentText(String content) {
        super.setText(getSpannableString(content));
    }

    public void setContentText(String content, List<Attachment> attachmentList, String boardId, String gid) {
        this.attachmentList = attachmentList;
        this.boardId = boardId;
        this.articleId = gid;
        super.setText(getSpannableString(content));
    }

    private static final String IMG_REGEX = "\\[(?i)img](.*?)\\[/(?i)img]";
    private static final String URL_REGEX = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";
    private static final String LINK_REGEX = "\\[(?i)URL=.*?](.*?)\\[/(?i)URL]";
    private static final String EXPRESSION_REGEX = "\\[(em\\d+)]";
    private static final String UPLOAD_REGEX = "\\[upload=\\d+]\\[/upload]";
//    private static final String COLOR_REGEX = "(\\[color=(#[0-9A-Z]{6})])([\\s\\S]*?)(\\[/color])";
//    private static final String ITALIC_REGEX = "\\[I\\]([\\s\\S]*?)\\[/I\\]";
//    private static final String UNDERLINE_REGEX = "\\[U\\]([\\s\\S]*?)\\[/U\\]";


    private SpannableStringBuilder getSpannableString(String content) {

        content = content.replaceAll(UPLOAD_REGEX, "");

        SpannableStringBuilder spannableString = new SpannableStringBuilder(content);

        // emoticon
        Pattern emoticonPattern = Pattern.compile(EXPRESSION_REGEX);
        Matcher matcher = emoticonPattern.matcher(spannableString);
        while (matcher.find()) {
            Drawable em = EmoticonUtil.getGifDrawable(matcher.group());
            if (em != null) {
                ImageSpan imageSpan = new ImageSpan(em);
                spannableString.setSpan(imageSpan, matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        // link
        Pattern linkPattern = Pattern.compile(LINK_REGEX);
        matcher = linkPattern.matcher(content);
        while (matcher.find()) {
            String text = matcher.group();
            String[] parts = text.split("[=\\]\\[]");
            if (parts.length == 5 && (parts[2]).matches(URL_REGEX)) {
                URLSpan urlSpan = new URLSpan(parts[2]);
                String urlText = "➣网页链接(" + parts[3] + ")";
                spannableString.replace(matcher.start(), matcher.end(), urlText);
                spannableString.setSpan(urlSpan, matcher.start(), matcher.start()+urlText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                matcher = linkPattern.matcher(spannableString);
            }
        }

        // image
        Pattern imgPattern = Pattern.compile(IMG_REGEX);
        matcher = imgPattern.matcher(spannableString);
        while (matcher.find()) {
            String text = matcher.group();
            String url = text.substring(5, text.length()-6);
            if (url.matches(URL_REGEX)) {

                int start = matcher.start();
                int end = matcher.end();
                TextView textView = ArticleTextView.this;

                Glide.with(getContext())
                        .load(url)
                        .placeholder(R.drawable.mimetype_image)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                double maxWidget = textView.getMeasuredWidth() - textView.getPaddingLeft() - textView.getPaddingRight();//获取 EditView 的宽度
                                double widget = resource.getIntrinsicWidth();// 获取资源的固定宽度
                                double height = resource.getIntrinsicHeight();// 获取资源的固定高度
                                double ratio = height / widget; // 计算宽高比例
                                double maxHeight = ratio * maxWidget; // 计算图片高度.
                                resource.setBounds(0, 0, (int) maxWidget, (int) maxHeight);

                                ImageSpan imageSpan = new ImageSpan(resource);
                                spannableString.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                ClickableSpan clickableSpan = new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View widget) {
//                                        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(), ImageActivity.class);
                                        intent.putExtra("url", url);
                                        getContext().startActivity(intent);
                                    }
                                };
                                spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                //bug 重复动作
                                textView.setText(spannableString);
                                textView.requestLayout();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        }

        // upload
        /*
        Pattern uploadPattern = Pattern.compile(UPLOAD_REGEX);
        matcher = uploadPattern.matcher(spannableString);
        while (matcher.find()) {
            int position = 0;
            for (int i=matcher.start()+8; i<matcher.end(); i++) {
                if (Character.isDigit(spannableString.charAt(i))) {
                    position = position * 10  + (spannableString.charAt(i) - '0');
                } else {
                    break;
                }
            }
            if (position > attachmentList.size())
                continue;

            Attachment attachment = this.attachmentList.get(position-1);
            String url = NetConst.ATTACHMENT + "?board=" + this.boardId + "&id=" + this.articleId + "&ap=" + attachment.getId();

            MimeValidator.Mime mime = MimeValidator.getMimetype(attachment.getName());
            int start = matcher.start();
            int end = matcher.end();
            TextView textView = ArticleTextView.this;

            if (mime.type == MimeValidator.Mime.Type.IMAGE) {
                Glide.with(getContext())
                        .load(url)
                        .placeholder(R.drawable.mimetype_image)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                double maxWidget = textView.getMeasuredWidth() - textView.getPaddingLeft() - textView.getPaddingRight();//获取 EditView 的宽度
                                double widget = resource.getIntrinsicWidth();// 获取资源的固定宽度
                                double height = resource.getIntrinsicHeight();// 获取资源的固定高度
                                double ratio = height / widget; // 计算宽高比例
                                double maxHeight = ratio * maxWidget; // 计算图片高度.
                                resource.setBounds(0, 0, (int) maxWidget, (int) maxHeight);

                                ImageSpan imageSpan = new ImageSpan(resource);
                                spannableString.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                ClickableSpan clickableSpan = new ClickableSpan() {
                                    @Override
                                    public void onClick(@NonNull View widget) {
                                        Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                                    }
                                };
                                spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                                //bug 重复动作
                                textView.setText(spannableString);
                                textView.requestLayout();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            } else {
                Glide.with(getContext()).load(mime.icon).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        double maxWidget = textView.getMeasuredWidth() - textView.getPaddingLeft() - textView.getPaddingRight();//获取 EditView 的宽度
                        double widget = resource.getIntrinsicWidth();// 获取资源的固定宽度
                        double height = resource.getIntrinsicHeight();// 获取资源的固定高度
                        double ratio = height / widget; // 计算宽高比例
                        double maxHeight = ratio * maxWidget; // 计算图片高度.
                        resource.setBounds(0, 0, (int) maxWidget, (int) maxHeight);

                        ImageSpan imageSpan = new ImageSpan(resource);
                        spannableString.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                            }
                        };
                        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                        //bug 重复动作
                        textView.setText(spannableString);
                        textView.requestLayout();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            }

        }
         */

        return spannableString;
    }
}
