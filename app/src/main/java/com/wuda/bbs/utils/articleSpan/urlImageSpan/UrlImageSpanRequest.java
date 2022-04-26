package com.wuda.bbs.utils.articleSpan.urlImageSpan;

import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.widget.TextView;

public class UrlImageSpanRequest {
    TextView textView;
    String url;
    Drawable placeHolderDrawable;
    Drawable errorDrawable;
    int desiredWidth;
    int desiredHeight;
    int aliment;

    DynamicDrawableSpan span;

    public UrlImageSpanRequest(TextView textView, String url, Drawable placeHolderDrawable, Drawable errorDrawable, int desiredWidth, int desiredHeight, int aliment) {
        this.textView = textView;
        this.url = url;
        this.placeHolderDrawable = placeHolderDrawable;
        this.errorDrawable = errorDrawable;
        this.desiredWidth = desiredWidth;
        this.desiredHeight = desiredHeight;
        this.aliment = aliment;
    }

}
