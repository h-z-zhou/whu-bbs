package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.Article;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TopicHandler extends BaseArticleHandler {

    Article article;
    String nodeName;
    String boardID;

    public TopicHandler(){
        boardID = "";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (nodeName.equals("topic")) {
            article = new Article();
        } else if (nodeName.equals("topics")) {
            articleResponse.setCurrentPage(Integer.parseInt(attributes.getValue("page")));
            articleResponse.setTotalPage(Integer.parseInt(attributes.getValue("totalPages")));
            boardID = attributes.getValue("board");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "GID":
                article.setGID(new String(ch, start, length));
                break;
            case "title":
                article.setTitle(new String(ch, start, length));
                break;
            case "author":
                article.setAuthor(new String(ch, start, length));
                break;
//            case "posttime":
//                time.append(ch, start, length);
//                time.append("->");
//                break;
            case "replyNum":
                article.setReplyNum(new String(ch, start, length));
                break;
            case "lastReplyTime":
                article.setTime(new String(ch, start, length));
                break;
            case "flag":
                int flag = new String(ch, start, length).equals("TOP")? Article.FLAG_TOP: Article.FLAG_NORMAL;
                article.setFlag(flag);
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("topic")) {
            article.setBoardID(boardID);
            articleResponse.addArticle(article);
        }
    }

}
