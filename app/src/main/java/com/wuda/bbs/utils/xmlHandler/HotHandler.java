package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.BriefArticle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class HotHandler extends BaseBriefArticleHandler {
    BriefArticle briefArticle;
    private String nodeName;

    StringBuilder title;
    StringBuilder author;
    StringBuilder boardID;
    StringBuilder boardName;
    StringBuilder groupID;
    StringBuilder time;

    public HotHandler() {
        briefArticleResponse.setCurrentPage(1);
        briefArticleResponse.setTotalPage(1);

        title = new StringBuilder();
        author = new StringBuilder();
        boardID = new StringBuilder();
        boardName = new StringBuilder();
        groupID = new StringBuilder();
        time = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (nodeName.equals("hot")) {
            briefArticle = new BriefArticle();
            briefArticle.setFlag(BriefArticle.FLAG_SYSTEM);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "title":
                title.append(ch, start, length);
                break;
            case "author":
                author.append(ch, start, length);
                break;
            case "board":
                boardID.append(ch, start, length);
                break;
            case "boardname":
                boardName.append(ch, start, length);
                break;
            case "groupid":
                groupID.append(ch, start, length);
                break;
            case "lasttime":
                time.append(ch, start, length);
                break;
//            case "number":
//                article.setReplyNum(new String(ch, start, length));
//                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        switch (localName) {
            case "hot":
                briefArticleResponse.addArticle(briefArticle);
                break;
            case "title":
                briefArticle.setTitle(title.toString());
                title.setLength(0);
                break;
            case "author":
                briefArticle.setAuthor(author.toString());
                author.setLength(0);
                break;
            case "board":
                briefArticle.setBoardID(boardID.toString());
                boardID.setLength(0);
                break;
            case "boardname":
                briefArticle.setBoardName(boardName.toString());
                boardName.setLength(0);
                break;
            case "groupid":
                briefArticle.setGID(groupID.toString());
                groupID.setLength(0);
                break;
            case "lasttime":
                briefArticle.setTime(time.toString());
                time.setLength(0);
                break;
        }
    }
}
