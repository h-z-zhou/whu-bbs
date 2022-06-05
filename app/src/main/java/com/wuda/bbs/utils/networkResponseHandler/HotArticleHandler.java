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

public abstract class HotArticleHandler implements ContentResponseHandler<List<BriefArticle>> {
    @Override
    public ContentResponse<List<BriefArticle>> handleNetworkResponse(@NonNull byte[] data) {

        /*
        <?xml version="1.0" encoding="utf-8"?>
<hots>
    <hot>
        <title>
            出冰箱
        </title>
        <author>
            jiangcheng14
        </author>
        <board>
            secondhand
        </board>
        <bid>
            80
        </bid>
        <boardname>
            二手货交易市场
        </boardname>
        <groupid>
            1110708598
        </groupid>
        <lasttime>
            2022-06-03 09:54:22
        </lasttime>
        <number>
            1
        </number>
    </hot>
</hots>
         */

        String xml = new String(data);

        List<BriefArticle> briefArticleList = new ArrayList<>();
        ContentResponse<List<BriefArticle>> response = new ContentResponse<>();

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));
            int event = parser.getEventType();

            BriefArticle briefArticle = new BriefArticle();

            while (event != XmlPullParser.END_DOCUMENT) {

                if (event == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                        case "hot":
                            briefArticle = new BriefArticle();
                            break;
                        case "title":
                            briefArticle.setTitle(parser.nextText());
                            break;
                        case "author":
                            briefArticle.setAuthor(parser.nextText());
                            break;
                        case "board":
                            briefArticle.setBoardID(parser.nextText());
                            break;
                        case "boardname":
                            briefArticle.setBoardName(parser.nextText());
                            break;
                        case "groupid":
                            briefArticle.setGID(parser.nextText());
                            break;
                        case "lasttime":
                            briefArticle.setTime(parser.nextText());
                            break;
                    }
                } else if (event == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("hot")) {
                        briefArticle.setFlag(BriefArticle.FLAG_SYSTEM);
                        briefArticleList.add(briefArticle);
                    }
                }
                event = parser.next();
            }

            response.setContent(briefArticleList);
            response.setTotalPage(1);
            response.setCurrentPage(1);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;
    }
}
