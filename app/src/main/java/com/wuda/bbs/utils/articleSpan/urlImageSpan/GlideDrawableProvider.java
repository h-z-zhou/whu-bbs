package com.wuda.bbs.utils.articleSpan.urlImageSpan;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class GlideDrawableProvider implements DrawableProvider{
    @Override
    public Drawable getDrawable(UrlImageSpanRequest request) {
        Drawable drawable;
        if (request.url == null || request.url.isEmpty()) {
            drawable = request.placeHolderDrawable != null? request.placeHolderDrawable: request.errorDrawable;
        } else {
            execute(request);
            drawable = request.placeHolderDrawable;
        }
        return drawable != null? drawable: new ColorDrawable();
    }

    private void execute(UrlImageSpanRequest request) {
        if (request.textView == null) return;
        DynamicDrawableSpan span =  request.span;

        Glide.with(request.textView)
                .load(request.url)
                .error(request.errorDrawable)
                .placeholder(request.placeHolderDrawable)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                        if (request.desiredWidth == -1 && request.desiredHeight == -1) {
                            double maxWidget = request.textView.getMeasuredWidth() - request.textView.getPaddingLeft() - request.textView.getPaddingRight();//获取 EditView 的宽度
                            double widget = resource.getIntrinsicWidth();// 获取资源的固定宽度
                            double height = resource.getIntrinsicHeight();// 获取资源的固定高度
                            double bl = height / widget; // 计算宽高比例,注意这里都是用的 double 类型的变量,以防止整除,也就是3/2=1这样.
                            double maxHeight = bl * maxWidget; // 通过宽高比例计算图片高度.
                            resource.setBounds(0, 0, (int) maxWidget, (int) maxHeight); // 重点代码 设置图片的宽高
                        } else if (request.desiredWidth == -2 && request.desiredHeight == -2){
                            resource.setBounds(0, 0, resource.getIntrinsicWidth(), resource.getIntrinsicHeight());
                        } else if (request.desiredWidth > 0 && request.desiredHeight > 0) {
                            resource.setBounds(0, 0, request.desiredWidth, request.desiredHeight);
                        }
                        onResponse(request, resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        onResponse(request, errorDrawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }

                    private void onResponse(UrlImageSpanRequest urlImageSpanRequest, Drawable drawable) {
                        Spannable spannable = (Spannable) urlImageSpanRequest.textView.getText();
                        if (spannable == null) return;

                        ImageSpan newSpan = new ImageSpan(drawable);

                        DynamicDrawableSpan oldSpan = request.span;
                        int start = spannable.getSpanStart(oldSpan);
                        int end = spannable.getSpanEnd(oldSpan);

                        if (start == -1 || end == -1) {
                            return;
                        }
                        spannable.removeSpan(oldSpan);
                        spannable.setSpan(newSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                });
    }
}
