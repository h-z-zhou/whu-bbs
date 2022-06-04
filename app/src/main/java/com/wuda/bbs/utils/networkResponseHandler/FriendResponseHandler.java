package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.Friend;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class FriendResponseHandler implements ContentResponseHandler<List<Friend>> {
    @Override
    public ContentResponse<List<Friend>> handleNetworkResponse(@NonNull byte[] data) {
        /*
<?xml version="1.0" encoding="utf-8"?>
<Friends Type="all" Total="3">
    <Friend experience="" ID="zhuzi" userface_img="wForum/uploadFace/Z/zhuzi.8395.jpg"/>
</Friends>
         */

        List<Friend> friendList = new ArrayList<>();
        ContentResponse<List<Friend>> response = new ContentResponse<>();

        String xml = new String(data);

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("Friend")) {
                        String id = parser.getAttributeValue("", "ID");
                        String exp = parser.getAttributeValue("", "experience");
                        String avatar = parser.getAttributeValue("", "userface_img");

                        friendList.add(new Friend(id, exp, avatar));
                    }
                }

                event = parser.next();
            }

            response.setContent(friendList);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;
    }
}
