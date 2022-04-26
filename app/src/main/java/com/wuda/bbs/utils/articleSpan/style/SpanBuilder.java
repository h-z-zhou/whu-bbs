package com.wuda.bbs.utils.articleSpan.style;

import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.widget.TextView;

import com.wuda.bbs.R;
import com.wuda.bbs.utils.EmoticonUtil;
import com.wuda.bbs.utils.articleSpan.urlImageSpan.UrlImageSpan;

public class SpanBuilder {

    public static URLSpan url(String url) {
        return new URLSpan(url);
    }

    public static ImageSpan emoticon(String em) {
        return new ImageSpan(EmoticonUtil.getDrawable(em));
    }

    public static DynamicDrawableSpan image(String url, TextView textView) {

        return new UrlImageSpan.Builder()
                .url(url)
                .placeholder(R.drawable.ic_add_image)
                .build(textView);
    }
}
