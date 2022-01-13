package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.Article;
import com.wuda.bbs.bean.ArticleResponse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RecommendHandler extends BaseArticleHandler {
    Article article;

    private String nodeName;

    public RecommendHandler() {
        articleResponse.setCurrentPage(1);
        articleResponse.setTotalPage(1);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (nodeName.equals("recomm")) {
            article = new Article();
            article.setFlag(Article.FLAG_SYSTEM);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "title":
                article.setTitle(new String(ch, start, length));
                break;
            case "recommTime":
                article.setTime(new String(ch, start, length));
                break;
            case "Author":
                article.setAuthor(new String(ch, start, length));
                break;
            case "originBoard":
                article.setBoardID(new String(ch, start, length));
                break;
            case "originBoardName":
                article.setBoardName(new String(ch, start, length));
            case "originGID":
                article.setGID(new String(ch, start, length));
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("recomm")) {
            articleResponse.addArticle(article);
        }
    }
}
