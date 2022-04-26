package com.wuda.bbs.utils.articleSpan.urlImageSpan;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

public class UrlImageSpan {

    public static class Builder {

        private String url;
        private Drawable placeholderDrawable;
        private int placeholderId = 0;
        private boolean useInstinctPlaceholderSize = true;
        private Drawable errorPlaceholder;
        private int errorId = 0;
        private boolean useInstinctErrorPlaceholderSize = true;
        private int verticalAlignment = DynamicDrawableSpan.ALIGN_BOTTOM;
        private int desiredWidth = -1;
        private int desiredHeight = -1;

        DrawableProvider provider;

        public Builder(DrawableProvider provider) {
            this.provider = provider;
        }

        public Builder() {
            this.provider = new GlideDrawableProvider();
        }

        public Builder resize(int desireWidth, int desireHeight) {
            this.desiredWidth = desireWidth;
            this.desiredHeight = desireHeight;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder verticalAliment(int verticalAlignment) {
            this.verticalAlignment = verticalAlignment;
            return this;
        }

        public Builder placeholder(Drawable drawable) {
            this.placeholderDrawable = drawable;
            this.placeholderId = 0;
            this.useInstinctPlaceholderSize = true;
            return this;
        }

        public Builder placeholder(Drawable drawable, int width, int height) {
            drawable.setBounds(0, 0, width, height);
            this.placeholderDrawable = drawable;
            this.placeholderId = 0;
            this.useInstinctPlaceholderSize = false;
            return this;
        }

        public Builder placeholder(@DrawableRes int resourceId) {
            this.placeholderDrawable = null;
            this.placeholderId = resourceId;
            this.useInstinctPlaceholderSize = true;
            return this;
        }

        public Builder error(Drawable drawable) {
            this.errorPlaceholder = drawable;
            this.errorId = 0;
            this.useInstinctErrorPlaceholderSize = true;
            return this;
        }

        public Builder error(Drawable drawable, int width, int height) {
            drawable.setBounds(0, 0, width, height);
            this.errorPlaceholder = drawable;
            this.errorId = 0;
            this.useInstinctErrorPlaceholderSize = false;
            return this;
        }

        public Builder error(@DrawableRes int resourceId) {
            this.errorPlaceholder = null;
            this.errorId = resourceId;
            this.useInstinctErrorPlaceholderSize = true;
            return this;
        }

        Drawable getPlaceholderDrawable(Context context){
            Drawable drawable = placeholderDrawable;
            if (drawable == null && placeholderId > 0) {
                drawable = ContextCompat.getDrawable(context, placeholderId);
            }
            if (useInstinctPlaceholderSize && drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            return drawable;
        }

        Drawable getErrorDrawable(Context context) {
            Drawable drawable = errorPlaceholder;
            if (drawable == null && errorId > 0) {
                drawable = ContextCompat.getDrawable(context, errorId);
            }
            if (useInstinctErrorPlaceholderSize && drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            return drawable;
        }

        UrlImageSpanRequest buildRequest(TextView textView) {
            return new UrlImageSpanRequest(
                    textView,
                    url,
                    getPlaceholderDrawable(textView.getContext()),
                    getErrorDrawable(textView.getContext()),
                    desiredWidth,
                    desiredHeight,
                    verticalAlignment
            );
        }

        public DynamicDrawableSpan build(TextView textView) {
            UrlImageSpanRequest request = buildRequest(textView);

            return new DynamicDrawableSpan() {
                @Override
                public Drawable getDrawable() {
                    request.span = this;
                    return provider.getDrawable(request);
                }
            };
        }
    }
}
