package com.wuda.bbs.utils.validator;

import com.wuda.bbs.R;

import java.io.Serializable;
import java.util.regex.Pattern;

public class MimeValidator {

    public static class Mime implements Serializable {

        public String extension;
        public Type type;
        public int icon;

        public Mime(String extension, Type type, int icon) {
            this.extension = extension;
            this.type = type;
            this.icon = icon;
        }

        public enum Type implements Serializable {
            IMAGE, AUDIO, VIDEO, ARCHIVE, APK,
            WORD, EXCEL, PPT, PDF, TEXT,
            UNKNOWN
        }
    }

    static final Mime[] mimes = new Mime[] {
            new Mime(".jpg|.jpeg|.gif|.png|.bmp|.svg", Mime.Type.IMAGE, R.drawable.mimetype_image),
            new Mime(".wav|.mp3|.flac", Mime.Type.AUDIO, R.drawable.mimetype_audio),
            new Mime(".mp4|.wmv|.avi|.mov", Mime.Type.VIDEO, R.drawable.mimetype_video),
            new Mime(".zip|.rar|.7z|.gz|.exe|.iso", Mime.Type.ARCHIVE, R.drawable.mimetype_archive),
            new Mime(".apk", Mime.Type.APK, R.drawable.mimetype_apk),
            new Mime(".doc|.docx", Mime.Type.WORD, R.drawable.mimetype_msword),
            new Mime(".xls|.xlsx", Mime.Type.EXCEL, R.drawable.mimetype_msexcel),
            new Mime(".ppt|.pptx", Mime.Type.PPT, R.drawable.mimetype_mspowerpoint),
            new Mime(".pdf", Mime.Type.PDF, R.drawable.mimetype_pdf),
            new Mime(".txt|.c|.cpp|.py|.java|.m", Mime.Type.TEXT, R.drawable.mimetype_text),
            new Mime("", Mime.Type.UNKNOWN, R.drawable.mimetype_unknown)
    };

    public static Mime getMimetype(String fileName) {
        for (Mime mime: mimes) {
            Pattern pattern = Pattern.compile(mime.extension+"$", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(fileName).find()) {
                return mime;
            }
        }

        return mimes[mimes.length-1];
    }
}
