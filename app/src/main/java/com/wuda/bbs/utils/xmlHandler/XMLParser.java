package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.DetailBoard;

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

    public static ArticleResponse parseRecommend(String xmlData) {
        RecommendHandler recommendHandler = new RecommendHandler();
        xmlReader.setContentHandler(recommendHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }

        return recommendHandler.getArticleResponse();
    }

    public static ArticleResponse parseHot(String xmlData) {
        HotHandler hotHandler = new HotHandler();
        xmlReader.setContentHandler(hotHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(xmlData)));
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        return hotHandler.getArticleResponse();
    }

    public static ArticleResponse parseTopic(String xmlData) {
        TopicHandler topicHandler = new TopicHandler();
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
}
