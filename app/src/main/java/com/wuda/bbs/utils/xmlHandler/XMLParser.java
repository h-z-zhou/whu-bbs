package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.bean.bbs.FavBoard;
import com.wuda.bbs.logic.bean.bbs.Friend;
import com.wuda.bbs.logic.bean.bbs.Mail;
import com.wuda.bbs.logic.bean.bbs.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.DetailArticleResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

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

    public static ContentResponse<List<DetailBoard>> parseDetailBoard(String xmlData) {

        ContentResponse<List<DetailBoard>> response;

        xmlData = xmlData.replaceAll("true", "\"true\"");
        xmlData = xmlData.replace("&", "AND");

        BoardHandler boardHandler = new BoardHandler();
        xmlReader.setContentHandler(boardHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response = new ContentResponse<>();
            response.setContent(boardHandler.getHandledResult());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e.getMessage());
        }
        return response;
    }

    public static ContentResponse<List<BriefArticle>> parseRecommend(String xmlData) {
        RecommendHandler recommendHandler = new RecommendHandler();
        xmlReader.setContentHandler(recommendHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return recommendHandler.getBriefArticleResponse();
    }

    public static ContentResponse<List<BriefArticle>> parseHot(String xmlData) {
        HotHandler hotHandler = new HotHandler();
        xmlReader.setContentHandler(hotHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return hotHandler.getBriefArticleResponse();
    }

    public static ContentResponse<List<BriefArticle>> parseTopic(String xmlData) {
        TopicHandler topicHandler = new TopicHandler();
        xmlReader.setContentHandler(topicHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return topicHandler.getBriefArticleResponse();
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
            response.setResultCode(ResultCode.UNMATCHED_CONTENT_ERR);
            response.setMassage(e.getMessage());
        }

        return response;
    }

    public static DetailArticleResponse parseDetailArticle(String xmlData) {
        xmlData = xmlData.replaceAll("&", "&amp;");
//        xmlData = xmlData.replaceAll("\\\\", "");
        DetailArticleResponse response;

        DetailArticleHandler detailArticleHandler = new DetailArticleHandler();
        xmlReader.setContentHandler(detailArticleHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response = detailArticleHandler.getDetailArticleResponse();
        } catch (IOException | SAXException e) {
            if (BBSApplication.isLogin())
                response = new DetailArticleResponse(ResultCode.HANDLE_DATA_ERR, e.getMessage());
            else {
                response = new DetailArticleResponse(ResultCode.NO_LOGIN_ERR, "未登陆用户无法查看一年前的帖子");
            }
        }

        return response;
    }

    public static ContentResponse<UserInfo> parseUserInfo(String xmlData) {

//        UserInfoResponse response = new UserInfoResponse();
        ContentResponse<UserInfo> response;

        UserInfoHandler userInfoHandler = new UserInfoHandler();
        xmlReader.setContentHandler(userInfoHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response = new ContentResponse<>();
            response.setContent(userInfoHandler.userInfo);
//            response.setUserInfo(userInfoHandler.getUserInfo());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e.getMessage());
        }

        return response;
    }

    public static ContentResponse<List<Friend>> parseFriends(String xmlData) {
        ContentResponse<List<Friend>> response;

        FriendHandler friendHandler = new FriendHandler();
        xmlReader.setContentHandler(friendHandler);

        try {
            response = new ContentResponse<>();
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
            response.setContent(friendHandler.getFriendList());
        } catch (IOException | SAXException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.CONNECT_ERR, e.getMessage());
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
            mailResponse = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e.getMessage());
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
            mailContentResponse = new ContentResponse<>(ResultCode.UNMATCHED_CONTENT_ERR, e.getMessage());
        }

        return mailContentResponse;
    }
}
