package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.BriefArticle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class HotHandlerBase extends BaseBriefArticleHandler {
    BriefArticle briefArticle;
    private String nodeName;

    public HotHandlerBase() {
        briefArticleResponse.setCurrentPage(1);
        briefArticleResponse.setTotalPage(1);
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
                briefArticle.setTitle(new String(ch, start, length));
                break;
            case "author":
                briefArticle.setAuthor(new String(ch, start, length));
                break;
            case "board":
                briefArticle.setBoardID(new String(ch, start, length));
                break;
            case "boardname":
                briefArticle.setBoardName(new String(ch, start, length));
                break;
            case "groupid":
                briefArticle.setGID(new String(ch, start, length));
                break;
            case "lasttime":
                briefArticle.setTime(new String(ch, start, length));
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
        if (localName.equals("hot")) {
            briefArticleResponse.addArticle(briefArticle);
        }
    }
}
