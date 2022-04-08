package com.wuda.bbs.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifDrawable;

public class ContentTextUtil {
    private static final String COLOR_REGEX = "(\\[color=(#[0-9A-Z]{6})\\])([\\s\\S]*?)(\\[/color\\])";
    private static final String IMG_REGEX = "\\[(?i)img](.*?)\\[/(?i)img\\]";
    private static final String URL_REGEX = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
    private static final String LINK_REGEX = "\\[(?i)URL=.*?](.*?)\\[/(?i)URL]";
    private static final String EXPRESSION_REGEX = "\\[(em\\d+)]";
    private static final String UPLOAD_REGEX = "\\[upload=\\d+\\]\\[\\\\/upload\\](.*?)attach\\('.*?', .*?, (.*?)\\);";
    private static final String ITALIC_REGEX = "\\[I\\]([\\s\\S]*?)\\[/I\\]";
    private static final String UNDERLINE_REGEX = "\\[U\\]([\\s\\S]*?)\\[/U\\]";


    public static SpannableStringBuilder getSpannableString(Context mContext, TextView textView, String commentString) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(commentString);

        // emoticon
        Pattern pattern = Pattern.compile(EXPRESSION_REGEX);
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            //matcher.group():匹配到的字符串，作为key，获取对应的图片
            Drawable em = EmoticonUtil.getGifDrawable(matcher.group());
            if (em != null) {
                ImageSpan imageSpan = new ImageSpan(em);
                spannableString.setSpan(imageSpan, matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        // link
        Pattern linkPattern = Pattern.compile(LINK_REGEX);
        matcher = linkPattern.matcher(commentString);
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
                Glide.with(mContext)
                        .load(url)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                double maxWidget = textView.getMeasuredWidth() - textView.getPaddingLeft() - textView.getPaddingRight();//获取 EditView 的宽度
                                double widget = resource.getIntrinsicWidth();// 获取资源的固定宽度
                                double height = resource.getIntrinsicHeight();// 获取资源的固定高度
                                double bl = height / widget; // 计算宽高比例,注意这里都是用的 double 类型的变量,以防止整除,也就是3/2=1这样.
                                double maxHeight = bl * maxWidget; // 通过宽高比例计算图片高度.
                                resource.setBounds(0, 0, (int) maxWidget, (int) maxHeight); // 重点代码 设置图片的宽高

                                ImageSpan imageSpan = new ImageSpan(resource);
                                spannableString.setSpan(imageSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                //bug 重复动作
                                textView.setText(spannableString);
                                textView.requestLayout();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

//                UrlImageSpan urlImageSpan = new UrlImageSpan(mContext, url, textView);
//                spannableString.setSpan(urlImageSpan, matcher.start(), matcher.end(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }

    /**
     * 获取网络图片的ImageSpan
     * Created by Yomii on 2016/10/13.
     */
    static class UrlImageSpan extends ImageSpan {

        private String url;
        private TextView tv;
        private boolean picShowed;

        public UrlImageSpan(Context context, String url, TextView tv) {
            super(context, R.drawable.mimetype_image);
            this.url = url;
            this.tv = tv;
        }

        @Override
        public Drawable getDrawable() {
            if (!picShowed) {
                Glide.with(tv.getContext())
                        .load(url)
                        .placeholder(R.drawable.ic_add_image)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                                double maxWidget = tv.getMeasuredWidth() - tv.getPaddingLeft() - tv.getPaddingRight();//获取 EditView 的宽度
                                double widget = resource.getIntrinsicWidth();// 获取资源的固定宽度
                                double height = resource.getIntrinsicHeight();// 获取资源的固定高度
                                double bl = height / widget; // 计算宽高比例,注意这里都是用的 double 类型的变量,以防止整除,也就是3/2=1这样.
                                double maxHeight = bl * maxWidget; // 通过宽高比例计算图片高度.
                                resource.setBounds(0, 0, (int) maxWidget, (int) maxHeight); // 重点代码 设置图片的宽高

//                        ImageSpan imageSpan = new ImageSpan(resource);
                                tv.requestLayout();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                });
            }
            return super.getDrawable();
        }
    }


}
