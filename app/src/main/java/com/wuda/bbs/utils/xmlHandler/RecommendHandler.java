package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.BriefArticle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RecommendHandler extends BaseBriefArticleHandler {
    BriefArticle briefArticle;

    private String nodeName;

    StringBuilder title;
    StringBuilder time;
    StringBuilder author;
    StringBuilder boardID;
    StringBuilder boardName;
    StringBuilder groupID;

    public RecommendHandler() {
        briefArticleResponse.setCurrentPage(1);
        briefArticleResponse.setTotalPage(1);

        title = new StringBuilder();
        time = new StringBuilder();
        author = new StringBuilder();
        boardID = new StringBuilder();
        boardName = new StringBuilder();
        groupID = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (nodeName.equals("recomm")) {
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
            case "recommTime":
                time.append(ch, start, length);
                break;
            case "Author":
                author.append(ch, start, length);
                break;
            case "originBoard":
                boardID.append(ch, start, length);
                break;
            case "originBoardName":
                boardName.append(ch, start, length);
                break;
            case "originGID":
                groupID.append(ch, start, length);
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        switch (localName) {
            case "recomm":
                briefArticleResponse.addArticle(briefArticle);
                break;
            case "title":
                briefArticle.setTitle(title.toString());
                title.setLength(0);
                break;
            case "recommTime":
                briefArticle.setTime(time.toString());
                time.setLength(0);
                break;
            case "Author":
                briefArticle.setAuthor(author.toString());
                author.setLength(0);
                break;
            case "originBoard":
                briefArticle.setBoardID(boardID.toString());
                boardID.setLength(0);
                break;
            case "originBoardName":
                briefArticle.setBoardName(boardName.toString());
                boardName.setLength(0);
                break;
            case "originGID":
                briefArticle.setGID(groupID.toString());
                groupID.setLength(0);
                break;
            default:
        }
    }
}
