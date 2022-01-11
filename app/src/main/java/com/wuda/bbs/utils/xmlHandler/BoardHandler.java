package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.Board;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class BoardHandler extends DefaultHandler {

    String section = "";

    private List<Board> boardList;

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        boardList = new ArrayList<>();
        boardList.add(new Board("recomm", "推荐", "com.wuda.bbs.local", true));
        boardList.add(new Board("hot", "十大热点", "com.wuda.bbs.local", true));
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if (localName.equals("board")) {
            if (attributes.getLength() == 4)  // hasChildren
                return;
            String boardName = attributes.getValue("name");
            String boardID = attributes.getValue("id");
            boardList.add(new Board(boardID, boardName, section, false));
        } else if (localName.equals("Section")) {
            section = attributes.getValue("name");
        }
    }

    public List<Board> getHandledResult() {
        return boardList;
    }
}
