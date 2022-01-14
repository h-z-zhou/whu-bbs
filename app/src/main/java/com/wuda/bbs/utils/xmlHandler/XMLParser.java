package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.BriefArticleResponse;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.DetailArticleResponse;

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

    public static List<BaseBoard> parseDetailBoard(String xmlData) {

        xmlData = xmlData.replaceAll("true", "\"true\"");
        xmlData = xmlData.replace("&", "AND");

        BoardHandler boardHandler = new BoardHandler();
        xmlReader.setContentHandler(boardHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        return boardHandler.getHandledResult();
    }

    public static BriefArticleResponse parseRecommend(String xmlData) {
        RecommendHandlerBase recommendHandler = new RecommendHandlerBase();
        xmlReader.setContentHandler(recommendHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return recommendHandler.getArticleResponse();
    }

    public static BriefArticleResponse parseHot(String xmlData) {
        HotHandlerBase hotHandler = new HotHandlerBase();
        xmlReader.setContentHandler(hotHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return hotHandler.getArticleResponse();
    }

    public static BriefArticleResponse parseTopic(String xmlData) {
        TopicHandlerBase topicHandler = new TopicHandlerBase();
        xmlReader.setContentHandler(topicHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return topicHandler.getArticleResponse();
    }

    public static List<BaseBoard> parseFavorBoard(String xmlData) {
        FavorBoardHandler favorBoardHandler = new FavorBoardHandler();
        xmlReader.setContentHandler(favorBoardHandler);

        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return favorBoardHandler.getFavorBoards();
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
}
