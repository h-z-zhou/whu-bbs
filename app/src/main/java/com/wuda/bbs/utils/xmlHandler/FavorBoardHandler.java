package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.FavBoard;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardHandler extends DefaultHandler {
    List<FavBoard> favorBoards = new ArrayList<>();

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (localName.equals("FavBrd")) {

            String boardID = attributes.getValue("name");
            String boardName = attributes.getValue("desc");

            favorBoards.add(new FavBoard(boardID, boardName, BBSApplication.getAccountId()));
        }
    }

    public List<FavBoard> getFavorBoards() {
        return favorBoards;
    }
}
