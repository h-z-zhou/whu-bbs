package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.BriefArticle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RecommendHandlerBase extends BaseBriefArticleHandler {
    BriefArticle briefArticle;

    private String nodeName;

    public RecommendHandlerBase() {
        briefArticleResponse.setCurrentPage(1);
        briefArticleResponse.setTotalPage(1);
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
                briefArticle.setTitle(new String(ch, start, length));
                break;
            case "recommTime":
                briefArticle.setTime(new String(ch, start, length));
                break;
            case "Author":
                briefArticle.setAuthor(new String(ch, start, length));
                break;
            case "originBoard":
                briefArticle.setBoardID(new String(ch, start, length));
                break;
            case "originBoardName":
                briefArticle.setBoardName(new String(ch, start, length));
            case "originGID":
                briefArticle.setGID(new String(ch, start, length));
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("recomm")) {
            briefArticleResponse.addArticle(briefArticle);
        }
    }
}
