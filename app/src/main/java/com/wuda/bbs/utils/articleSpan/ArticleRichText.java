package com.wuda.bbs.utils.articleSpan;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.wuda.bbs.utils.articleSpan.listener.OnImageClickListener;
import com.wuda.bbs.utils.articleSpan.style.TagHandler;

public class ArticleRichText {

    public static Builder build(String content) {
        return new Builder(content);
    }

    public static class Builder {
        SpannableStringBuilder content;
        OnImageClickListener mOnImageClickListener;

        public Builder(String content) {
            this.content = new SpannableStringBuilder(content);
        }

        public Builder setOnImageClickListener(OnImageClickListener onImageClickListener) {
            this.mOnImageClickListener = onImageClickListener;
            return this;
        }

        public void into(TextView textView) {
            TagHandler.handle(content, textView, mOnImageClickListener);
            textView.setText(content, TextView.BufferType.SPANNABLE);
        }

        public SpannableString format() {
            TagHandler.handle(content);
            return SpannableString.valueOf(this.content);
        }
    }
}
