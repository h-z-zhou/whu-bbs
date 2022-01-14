package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.BriefArticle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TopicHandlerBase extends BaseBriefArticleHandler {

    BriefArticle briefArticle;
    String nodeName;
    String boardID;

    public TopicHandlerBase(){
        boardID = "";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (nodeName.equals("topic")) {
            briefArticle = new BriefArticle();
        } else if (nodeName.equals("topics")) {
            briefArticleResponse.setCurrentPage(Integer.parseInt(attributes.getValue("page")));
            briefArticleResponse.setTotalPage(Integer.parseInt(attributes.getValue("totalPages")));
            boardID = attributes.getValue("board");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "GID":
                briefArticle.setGID(new String(ch, start, length));
                break;
            case "title":
                briefArticle.setTitle(new String(ch, start, length));
                break;
            case "author":
                briefArticle.setAuthor(new String(ch, start, length));
                break;
//            case "posttime":
//                time.append(ch, start, length);
//                time.append("->");
//                break;
            case "replyNum":
                briefArticle.setReplyNum(new String(ch, start, length));
                break;
            case "lastReplyTime":
                briefArticle.setTime(new String(ch, start, length));
                break;
            case "flag":
                int flag = new String(ch, start, length).equals("TOP")? BriefArticle.FLAG_TOP: BriefArticle.FLAG_NORMAL;
                briefArticle.setFlag(flag);
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (localName.equals("topic")) {
            briefArticle.setBoardID(boardID);
            briefArticleResponse.addArticle(briefArticle);
        }
    }

}
