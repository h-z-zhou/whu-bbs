package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.bbs.Friend;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class FriendHandler extends DefaultHandler {
    List<Friend> friendList;

    public FriendHandler() {
        friendList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("Friend")) {
            String id = attributes.getValue("ID");
            String alias = attributes.getValue("experience");
            String avatar = attributes.getValue("userface_img");
            friendList.add(new Friend(id, alias, avatar));
        }
    }

    public List<Friend> getFriendList() {
        return friendList;
    }
}
