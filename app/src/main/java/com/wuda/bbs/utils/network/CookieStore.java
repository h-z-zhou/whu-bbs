package com.wuda.bbs.utils.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Xml;

import androidx.annotation.NonNull;

import com.wuda.bbs.application.BBSApplication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class CookieStore implements CookieJar {

    static Map<String, List<Cookie>> store;
    private static CookieStore cookieStore;

    static {
        store = new HashMap<>();
        List<Cookie> cookies = loadCookies();
        if (!cookies.isEmpty())
            store.put(NetConst.BASE_HOST, loadCookies());
    }

    public static CookieStore newInstance() {
        if (cookieStore == null) {
            cookieStore = new CookieStore();
        }
        return cookieStore;
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        if (url.host().equals(NetConst.BASE_HOST)) {
            Map<String, Cookie> filter = new HashMap<>();
            for (Cookie cookie: cookies) {
                filter.put(cookie.name(), cookie);
            }
            if (filter.containsKey("UTMPUSERID"))
                store.put(url.host(), new ArrayList<>(filter.values()));
        }
    }

    @NonNull
    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        List<Cookie> cookies = store.get(url.host());
        if (cookies != null) {
            return cookies;
        } else {
            return new ArrayList<>();
        }
    }

    public static boolean isLoginBBS() {
        List<Cookie> cookies = store.get(NetConst.BASE_HOST);
        assert cookies != null;
        for (Cookie cookie: cookies) {
            if (cookie.name().equals("UTMPUSERID")) {
                return !cookie.value().equals("guest");
            }
        }
        return true;
    }

    public static void storeCookiesWithXML() {

        List<Cookie> cookies = store.get(NetConst.BASE_HOST);

        XmlSerializer xmlSerializer = Xml.newSerializer();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            xmlSerializer.setOutput(new OutputStreamWriter(os));
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "cookies");
            xmlSerializer.attribute(null, "host", NetConst.BASE_HOST);
            assert cookies != null;
            for (Cookie cookie: cookies) {
                xmlSerializer.startTag(null, "cookie");
                xmlSerializer.attribute(null, cookie.name(), cookie.value());
                xmlSerializer.endTag(null, "cookie");
            }
            xmlSerializer.endTag(null, "cookies");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = BBSApplication.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE).edit();

        editor.putString("cookies", os.toString());

        editor.apply();

    }

    private static List<Cookie> loadCookies() {
        SharedPreferences sp = BBSApplication.getAppContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String xmlData = sp.getString("cookies", "<?xml version='1.0' encoding='utf-8' standalone='yes' ?><cookies></cookies>");

        XmlPullParser xmlPullParser = Xml.newPullParser();
        List<Cookie> cookies = new ArrayList<>();

        try {
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                    case XmlPullParser.END_TAG:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("cookie")) {
                            Cookie.Builder builder = new Cookie.Builder();
                            String name = xmlPullParser.getAttributeName(0);
                            String value = xmlPullParser.getAttributeValue(0);
                            builder.hostOnlyDomain(NetConst.BASE_HOST);
                            builder.name(name);
                            builder.value(value);
                            cookies.add(builder.build());
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return cookies;
    }
}
