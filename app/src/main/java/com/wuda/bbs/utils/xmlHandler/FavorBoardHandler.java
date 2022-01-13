package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.FavorBoard;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardHandler extends DefaultHandler {
    List<BaseBoard> favorBoards = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (localName.equals("FavBrd")) {

            String boardID = attributes.getValue("name");
            String boardName = attributes.getValue("desc");

            favorBoards.add(new FavorBoard(boardID, boardName, BBSApplication.getUsername()));
        }
    }

    public List<BaseBoard> getFavorBoards() {
        return favorBoards;
    }
}
