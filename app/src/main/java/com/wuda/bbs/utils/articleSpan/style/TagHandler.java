package com.wuda.bbs.utils.articleSpan.style;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.wuda.bbs.utils.articleSpan.listener.OnImageClickListener;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagHandler {

    private static final String COLOR_REGEX = "(\\[color=(#[0-9A-Z]{6})\\])([\\s\\S]*?)(\\[/color\\])";
    private static final String IMG_REGEX = "\\[(?i)img](.*?)\\[/(?i)img]";
    private static final String URL_FORMAT_REGEX = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";
    private static final String URL_REGEX = "\\[(?i)URL=.*?](.*?)\\[/(?i)URL]";
    private static final String EMOTICON_REGEX = "\\[(em\\d+)]";
    private static final String UPLOAD_REGEX = "\\[upload=\\d+\\]\\[\\\\/upload\\](.*?)attach\\('.*?', .*?, (.*?)\\);";
    private static final String ITALIC_REGEX = "\\[I\\]([\\s\\S]*?)\\[/I\\]";
    private static final String UNDERLINE_REGEX = "\\[U\\]([\\s\\S]*?)\\[/U\\]";

    private static final Matcher MATCHER_URL = Pattern.compile(URL_REGEX).matcher("");
    private static final Matcher MATCHER_EMOTICON = Pattern.compile(EMOTICON_REGEX).matcher("");
    private static final Matcher MATCHER_IMAGE = Pattern.compile(IMG_REGEX).matcher("");
    private static final Map<Integer, Matcher> matchers = new LinkedHashMap<>();

    static {
        matchers.put(Tag.URL, MATCHER_URL);
        matchers.put(Tag.EMOTICON, MATCHER_EMOTICON);
        matchers.put(Tag.IMAGE, MATCHER_IMAGE);
    }

    public static void handle(SpannableStringBuilder content, TextView textView, OnImageClickListener onImageClickListener) {
        url(content);
        emoticon(content);
        image(content, textView, onImageClickListener);
    }

    public static void handle(SpannableStringBuilder content) {
        url(content);
        emoticon(content);
    }


    public static boolean url(SpannableStringBuilder content) {
        MATCHER_URL.reset(content);

        Matcher matcher = matchers.get(Tag.URL);
        assert matcher != null;
        matcher.reset(content);

        while (matcher.find()) {
            String text = matcher.group();
            String[] parts = text.split("[=\\]\\[]");
            if (parts.length == 5 && (parts[2]).matches(URL_FORMAT_REGEX)) {
                String url = parts[2];
                String title = "➣网页链接(" + parts[3] + ")";
                content.replace(matcher.start(), matcher.end(), title);
                content.setSpan(SpanBuilder.url(url), matcher.start(), matcher.start()+title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                matcher.reset(); // 位置已经变化
            }
        }
        return false;
    }

    public static boolean emoticon(SpannableStringBuilder content) {
        Matcher matcher = matchers.get(Tag.EMOTICON);
        assert matcher != null;
        matcher.reset(content);

        while (matcher.find()) {
            String em = matcher.group();
            content.setSpan(SpanBuilder.emoticon(em), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return false;
    }

    public static boolean image(SpannableStringBuilder content, TextView textView, OnImageClickListener onImageClickListener) {
        Matcher matcher = matchers.get(Tag.IMAGE);
        assert matcher != null;
        matcher.reset(content);

        while (matcher.find()) {
            String text = matcher.group();
            String url = text.substring(5, text.length()-6);
            if (url.matches(URL_FORMAT_REGEX)) {
                content.setSpan(SpanBuilder.image(url, textView), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (onImageClickListener != null) {
                    content.setSpan(
                            new ClickableSpan() {
                                @Override
                                public void onClick(@NonNull View widget) {
                                    onImageClickListener.onImageClick(url);
                                }
                            },
                            matcher.start(),
                            matcher.end(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }
        }
        return false;
    }
}
