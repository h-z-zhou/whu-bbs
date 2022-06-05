package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class TopicArticleHandler implements ContentResponseHandler<List<BriefArticle>> {
    @Override
    public ContentResponse<List<BriefArticle>> handleNetworkResponse(@NonNull byte[] data) {
        /*
        <?xml version="1.0" encoding="utf-8"?>
<topics board="FreeTalk" page="1" totalPages="400">
    <topic>
        <number>
            1
        </number>
        <GID>
            1040509
        </GID>
        <title>
            临别
        </title>
        <author>
            Cindy
        </author>
        <posttime>
            2013-10-26 17:04:09
        </posttime>
        <replyNum>
            11
        </replyNum>
        <lastReplyID>
            1042367
        </lastReplyID>
        <lastReplyAuthor>
            bbscrsky
        </lastReplyAuthor>
        <lastReplyTime>
            2016-04-05 22:37:04
        </lastReplyTime>
        <flag>
            TOP
        </flag>
    </topic>
</topics>
         */

        String xml = new String(data);

        List<BriefArticle> briefArticleList = new ArrayList<>();
        ContentResponse<List<BriefArticle>> response = new ContentResponse<>();

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));
            int event = parser.getEventType();

            BriefArticle briefArticle = new BriefArticle();
            String board = "", page = "", totalPages = "";
            while (event != XmlPullParser.END_DOCUMENT) {

                if (event == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                        case "topics":
                            board = parser.getAttributeValue("", "board");
                            page = parser.getAttributeValue("", "page");
                            totalPages = parser.getAttributeValue("", "totalPages");
                            break;
                        case "topic":
                            briefArticle = new BriefArticle();
                            break;
                        case "title":
                            briefArticle.setTitle(parser.nextText());
                            break;
                        case "author":
                            briefArticle.setAuthor(parser.nextText());
                            break;
                        case "GID":
                            briefArticle.setGID(parser.nextText());
                            break;
                        case "lastReplyTime":
                            briefArticle.setTime(parser.nextText());
                            break;
                        case "flag":
                            briefArticle.setFlag(parser.nextText().equals("TOP")? BriefArticle.FLAG_TOP: BriefArticle.FLAG_NORMAL);
                            break;
                    }
                } else if (event == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("topic")) {
                        briefArticle.setBoardID(board);
                        briefArticleList.add(briefArticle);
                    }
                }
                event = parser.next();
            }
            response.setContent(briefArticleList);
            response.setCurrentPage(Integer.parseInt(page));
            response.setTotalPage(Integer.parseInt(totalPages));
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;
    }
}
