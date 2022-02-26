package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.FavBoard;
import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.DetailArticleResponse;
import com.wuda.bbs.logic.bean.response.DetailBoardResponse;
import com.wuda.bbs.logic.bean.response.FriendResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.logic.bean.response.UserInfoResponse;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class XMLParser {
    private static XMLReader xmlReader;

    static {
        try {
            xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static DetailBoardResponse parseDetailBoard(String xmlData) {

        DetailBoardResponse response = new DetailBoardResponse();

        xmlData = xmlData.replaceAll("true", "\"true\"");
        xmlData = xmlData.replace("&", "AND");

        BoardHandler boardHandler = new BoardHandler();
        xmlReader.setContentHandler(boardHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response.setDetailBoardList(boardHandler.getHandledResult());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            response.setSuccessful(false);
            response.setMassage(e.getMessage());
        }
        return response;
    }

    public static BriefArticleResponse parseRecommend(String xmlData) {
        RecommendHandler recommendHandler = new RecommendHandler();
        xmlReader.setContentHandler(recommendHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return recommendHandler.getArticleResponse();
    }

    public static BriefArticleResponse parseHot(String xmlData) {
        HotHandler hotHandler = new HotHandler();
        xmlReader.setContentHandler(hotHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return hotHandler.getArticleResponse();
    }

    public static BriefArticleResponse parseTopic(String xmlData) {
        TopicHandler topicHandler = new TopicHandler();
        xmlReader.setContentHandler(topicHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return topicHandler.getArticleResponse();
    }

    public static ContentResponse<List<FavBoard>> parseFavorBoard(String xmlData) {

        ContentResponse<List<FavBoard>> response = new ContentResponse<List<FavBoard>>();

        FavorBoardHandler favorBoardHandler = new FavorBoardHandler();
        xmlReader.setContentHandler(favorBoardHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response.setContent(favorBoardHandler.getFavorBoards());

        } catch (IOException | SAXException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.ERROR);
            response.setMassage(e.getMessage());
        }

        return response;
    }

    public static DetailArticleResponse parseDetailArticle(String xmlData) {
        DetailArticleHandler detailArticleHandler = new DetailArticleHandler();
        xmlReader.setContentHandler(detailArticleHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return detailArticleHandler.getDetailArticleResponse();
    }

    public static UserInfoResponse parseUserInfo(String xmlData) {

        UserInfoResponse response = new UserInfoResponse();

        UserInfoHandler userInfoHandler = new UserInfoHandler();
        xmlReader.setContentHandler(userInfoHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response.setUserInfo(userInfoHandler.getUserInfo());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            response.setSuccessful(false);
            response.setMassage(e.getMessage());
        }

        return response;
    }

    public static FriendResponse parseFriends(String xmlData) {
        FriendResponse response = new FriendResponse();

        FriendHandler friendHandler = new FriendHandler();
        xmlReader.setContentHandler(friendHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response.setFriendList(friendHandler.getFriendList());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            response.setSuccessful(false);
            response.setMassage(e.getMessage());
        }

        return response;
    }

    public static ContentResponse<List<Mail>> parseMailList(String xmlData) {
        ContentResponse<List<Mail>> mailResponse;

        MailHandler mailHandler = new MailHandler();
        xmlReader.setContentHandler(mailHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            mailResponse = mailHandler.getMailResponse();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            mailResponse = new ContentResponse<>(ResultCode.DATA_ERR, e.getMessage());
        }

        return mailResponse;
    }


    public static ContentResponse<String> parseMailContent(String xmlData) {

        ContentResponse<String> mailContentResponse;

        MailContentHandler mailContentHandler = new MailContentHandler();
        xmlReader.setContentHandler(mailContentHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            mailContentResponse = mailContentHandler.getMailContentResponse();
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            mailContentResponse = new ContentResponse<>(ResultCode.ERROR, e.getMessage());
        }

        return mailContentResponse;
    }
}
