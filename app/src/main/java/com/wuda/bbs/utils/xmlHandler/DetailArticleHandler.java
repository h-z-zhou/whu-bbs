package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.bbs.DetailArticle;
import com.wuda.bbs.logic.bean.response.DetailArticleResponse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class DetailArticleHandler extends DefaultHandler {
    DetailArticleResponse detailArticleResponse;
    List<DetailArticle> articleList;
    DetailArticle detailArticle;
    String nodeName;

    StringBuilder floor;
    StringBuilder id;
    StringBuilder author;
    StringBuilder userFaceImg;
    StringBuilder content;

    public DetailArticleHandler() {
        floor = new StringBuilder();
        id = new StringBuilder();
        author = new StringBuilder();
        userFaceImg = new StringBuilder();
        content = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (localName.equals("page")) {
            articleList = new ArrayList<>();
            detailArticleResponse = new DetailArticleResponse();
            detailArticleResponse.setGID(attributes.getValue("GID"));
            detailArticleResponse.setCurrentPage(Integer.parseInt(attributes.getValue("num")));
            detailArticleResponse.setTotalPage(Integer.parseInt(attributes.getValue("total")));
            detailArticleResponse.setReplyNum(Integer.parseInt(attributes.getValue("replynum")));
            detailArticleResponse.setFlag(attributes.getValue("flag"));
        } else if (localName.equals("article")) {
            detailArticle = new DetailArticle();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "floor":
                floor.append(ch, start, length);
                break;
            case "id":
                id.append(ch, start, length);
                break;
            case "author":
                author.append(ch, start, length);
                break;
            case "userface_img":
                userFaceImg.append(ch, start, length);
                break;
            case "content":
                content.append(ch, start, length);
//                detailArticle.setTime(ArticleContentRegex.getTime(rawContent));
//                detailArticle.setContent(ArticleContentRegex.getContent(rawContent));
//                Log.d("content", detailArticle.getContent());
//                detailArticle.setReply2username(ArticleContentRegex.getReply2username(rawContent));
//                detailArticle.setReply2content(ArticleContentRegex.getReply2content(rawContent));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        switch (localName) {
            case "page":
                detailArticleResponse.setContent(articleList);
                break;
            case "article":
                articleList.add(detailArticle);
                break;
            case "floor":
                detailArticle.setFloor(floor.toString());
                floor.setLength(0);
                break;
            case "id":
                detailArticle.setId(id.toString());
                id.setLength(0);
                break;
            case "author":
                detailArticle.setAuthor(author.toString());
                author.setLength(0);
                break;
            case "userface_img":
                detailArticle.setUserFaceImg(userFaceImg.toString());
                userFaceImg.setLength(0);
                break;
            case "content":
                String rawContent = content.toString();
                content.setLength(0);
                detailArticle.setTime(ArticleContentRegex.getTime(rawContent));
                detailArticle.setContent(ArticleContentRegex.getContent(rawContent));
                detailArticle.setReply2username(ArticleContentRegex.getReply2username(rawContent));
                detailArticle.setReply2content(ArticleContentRegex.getReply2content(rawContent));
                detailArticle.setAttachmentList(ArticleContentRegex.getAttachments(rawContent));
        }

    }

    public DetailArticleResponse getDetailArticleResponse() {
        return detailArticleResponse;
    }
}
