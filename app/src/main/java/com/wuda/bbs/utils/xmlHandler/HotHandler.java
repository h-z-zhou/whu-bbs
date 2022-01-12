package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.Article;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class HotHandler extends BaseArticleHandler {
    Article article;
    private String nodeName;

    public HotHandler() {
        articleResponse.setCurrentPage(1);
        articleResponse.setTotalPage(1);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (nodeName.equals("hot")) {
            article = new Article();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "title":
                article.setTitle(new String(ch, start, length));
                break;
            case "author":
                article.setAuthor(new String(ch, start, length));
                break;
            case "board":
                article.setBoardID(new String(ch, start, length));
                break;
            case "boardname":
                article.setBoardName(new String(ch, start, length));
                break;
            case "groupid":
                article.setGID(new String(ch, start, length));
                break;
            case "lasttime":
                article.setTime(new String(ch, start, length));
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
            articleResponse.addArticle(article);
        }
    }
}
