package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.DetailBoard;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class BoardHandler extends DefaultHandler {

    String section = "";

    private List<BaseBoard> detailBoardList;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        detailBoardList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("board")) {
            if (attributes.getLength() == 4)  // hasChildren
                return;
            String boardName = attributes.getValue("name");
            String boardID = attributes.getValue("id");
            detailBoardList.add(new DetailBoard(boardID, boardName, section));
        } else if (localName.equals("Section")) {
            section = attributes.getValue("name");
        }
    }

    public List<BaseBoard> getHandledResult() {
        return detailBoardList;
    }
}
