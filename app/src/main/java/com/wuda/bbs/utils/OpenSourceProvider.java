package com.wuda.bbs.utils;

import com.wuda.bbs.logic.bean.bbs.OpenSourceProject;

import java.util.ArrayList;
import java.util.List;

public class OpenSourceProvider {

    static final String LICENCE_APACHE_2 = "Apache License, Version 2.0";
    static final String LICENCE_MIT = "MIT License";
    static final String LICENCE_UNKNOWN = "Unknown License";

    public static List<OpenSourceProject> getUsedOpenSourceProjects() {
        List<OpenSourceProject> openSourceProjectList = new ArrayList<>();
        openSourceProjectList.add(new OpenSourceProject(
                "AndroidX",
                "Google",
                "https://github.com/androidx",
                LICENCE_APACHE_2,
                "Development environment for Android Jetpack extension libraries" +
                        " under the androidx namespace. Synchronized with Android Jetpack's" +
                        " primary development branch on AOSP. "));
        openSourceProjectList.add(new OpenSourceProject(
                "OkHttp3",
                "Square",
                "https://square.github.io",
                LICENCE_APACHE_2,
                ""
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "Retrofit",
                "Square",
                "https://square.github.io/retrofit/",
                LICENCE_APACHE_2,
                "A type-safe HTTP client for Android and Java"
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "jsoup",
                "Jonathan Hedley",
                "https://jsoup.org/",
                LICENCE_MIT,
                "jsoup is a Java library for working with real-world HTML. " +
                        "It provides a very convenient API for fetching URLs and extracting and " +
                        "manipulating data, using the best of HTML5 DOM methods and CSS selectors."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "CircleImageView",
                "hdodenhof",
                "https://github.com/hdodenhof/CircleImageView",
                LICENCE_APACHE_2,
                "A fast circular ImageView perfect for profile images. " +
                        "This is based on RoundedImageView from Vince Mi which itself is based on " +
                        "techniques recommended by Romain Guy."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "Glide",
                "Sam Judd",
                "https://bumptech.github.io/glide/",
                "BSD, part MIT and Apache 2.0",
                "Glide is a fast and efficient image loading library for " +
                        "Android focused on smooth scrolling. Glide offers an easy to use API, " +
                        "a performant and extensible resource decoding pipeline and automatic resource pooling."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "ExpandableRecyclerView",
                "Xigong93",
                "https://github.com/Xigong93/ExpandableRecyclerView",
                LICENCE_MIT,
                "ExpandableRecyclerView with smoothly animation."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "PictureSelector",
                "LuckSiege",
                "https://github.com/LuckSiege/PictureSelector",
                LICENCE_APACHE_2,
                "A PictureSelector for Android platform, which supports obtaining " +
                        "pictures, videos, audio & photos from photo albums, cutting (single picture or multi picture cutting), " +
                        "compression, theme custom configuration and other functions, and supports dynamic access & " +
                        "an open source picture selection framework suitable for Android 5.0 + system"
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "uCrop",
                "Yalantis",
                "https://github.com/Yalantis/uCrop",
                LICENCE_APACHE_2,
                "This project aims to provide an ultimate and flexible image cropping experience."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "android-gif-drawable",
                "koral--",
                "https://github.com/koral--/android-gif-drawable",
                LICENCE_MIT,
                "Bundled GIFLib via JNI is used to render frames. " +
                        "This way should be more efficient than WebView or Movie classes."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "JKeyboardPanelSwitch",
                "Jacksgong",
                "https://github.com/Jacksgong/JKeyboardPanelSwitch",
                LICENCE_APACHE_2,
                "The handler for the keyboard and panel layout conflict in Android."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "HtmlTextView",
                "SufficientlySecure",
                "https://github.com/SufficientlySecure/html-textview",
                LICENCE_APACHE_2,
                "HtmlTextView is an extended TextView component for Android, " +
                        "which can load very simple HTML by converting it into Android Spannables for viewing."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "URLImageSpan",
                "benioZhang",
                "https://github.com/benioZhang/URLImageSpan",
                LICENCE_UNKNOWN,
                "An ImageSpan that can load remote image."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "MarkDown",
                "zzhoujay",
                "https://github.com/zzhoujay/Markdown",
                LICENCE_MIT,
                "Android平台下的原生Markdown解析器."
        ));
        openSourceProjectList.add(new OpenSourceProject(
                "TiebaLite",
                "HuanCheng65",
                "https://github.com/HuanCheng65/TiebaLite",
                LICENCE_APACHE_2,
                "非官方的贴吧客户端."
        ));

        return openSourceProjectList;
    }
}
