package com.wuda.bbs.utils.articleSpan.urlImageSpan;

import android.graphics.drawable.Drawable;

public interface DrawableProvider {
    Drawable getDrawable(UrlImageSpanRequest request);
}
