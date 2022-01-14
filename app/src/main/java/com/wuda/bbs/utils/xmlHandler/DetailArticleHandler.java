package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.bean.DetailArticle;
import com.wuda.bbs.bean.DetailArticleResponse;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DetailArticleHandler extends DefaultHandler {
    DetailArticleResponse detailArticleResponse;
    DetailArticle detailArticle;
    String nodeName;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
        if (nodeName.equals("page")) {
            detailArticleResponse = new DetailArticleResponse();
            detailArticleResponse.setGID(attributes.getValue("GID"));
            detailArticleResponse.setCurrentPage(Integer.parseInt(attributes.getValue("num")));
            detailArticleResponse.setTotalPage(Integer.parseInt(attributes.getValue("total")));
            detailArticleResponse.setReplyNum(Integer.parseInt(attributes.getValue("replynum")));
            detailArticleResponse.setFlag(attributes.getValue("flag"));
        } else if (nodeName.equals("article")) {
            detailArticle = new DetailArticle();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "id":
                detailArticle.setId(new String(ch, start, length));
                break;
            case "author":
                detailArticle.setAuthor(new String(ch, start, length));
                break;
            case "userface_img":
                detailArticle.setUserFaceImg(new String(ch, start, length));
                break;
            case "content":
                detailArticle.setContent(new String(ch, start, length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if (nodeName.equals("article")) {
            detailArticleResponse.getDetailArticleList().add(detailArticle);
        }
    }

    public DetailArticleResponse getDetailArticleResponse() {
        return detailArticleResponse;
    }
}
