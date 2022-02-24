package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.BriefArticle;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TopicHandler extends BaseBriefArticleHandler {

    BriefArticle briefArticle;
    String nodeName;
    String boardID;

    StringBuilder groupID;
    StringBuilder title;
    StringBuilder author;
    StringBuilder replyNum;
    StringBuilder time;
    StringBuilder flag;

    public TopicHandler(){
        boardID = "";

        groupID = new StringBuilder();
        title = new StringBuilder();
        author = new StringBuilder();
        replyNum = new StringBuilder();
        time = new StringBuilder();
        flag = new StringBuilder();
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
                groupID.append(ch, start, length);
                break;
            case "title":
                title.append(ch, start, length);
                break;
            case "author":
                author.append(ch, start, length);
                break;
//            case "posttime":
//                time.append(ch, start, length);
//                time.append("->");
//                break;
            case "replyNum":
                replyNum.append(ch, start, length);
                break;
            case "lastReplyTime":
                time.append(ch, start, length);
                break;
            case "flag":
                flag.append(ch, start, length);
//                int flag = new String(ch, start, length).equals("TOP")? BriefArticle.FLAG_TOP: BriefArticle.FLAG_NORMAL;
//                briefArticle.setFlag(flag);
                break;
            default:
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        switch (localName) {
            case "topic":
                briefArticle.setBoardID(boardID);
                briefArticleResponse.addArticle(briefArticle);
                break;
            case "GID":
                briefArticle.setGID(groupID.toString());
                groupID.setLength(0);
                break;
            case "title":
                briefArticle.setTitle(title.toString());
                title.setLength(0);
                break;
            case "author":
                briefArticle.setAuthor(author.toString());
                author.setLength(0);
                break;
//            case "posttime":
//                time.append(ch, start, length);
//                time.append("->");
//                break;
            case "replyNum":
                briefArticle.setReplyNum(replyNum.toString());
                replyNum.setLength(0);
                break;
            case "lastReplyTime":
                briefArticle.setTime(time.toString());
                time.setLength(0);
                break;
            case "flag":
                int iFlag = flag.toString().equals("TOP")? BriefArticle.FLAG_TOP: BriefArticle.FLAG_NORMAL;
                briefArticle.setFlag(iFlag);
                flag.setLength(0);
                break;
            default:
        }
    }

}
