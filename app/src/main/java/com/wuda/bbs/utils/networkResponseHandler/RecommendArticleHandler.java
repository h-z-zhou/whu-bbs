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

public abstract class RecommendArticleHandler implements ContentResponseHandler<List<BriefArticle>> {
    @Override
    public ContentResponse<List<BriefArticle>> handleNetworkResponse(@NonNull byte[] data) {

        /*
        <?xml version="1.0" encoding="utf-8"?>
<recomms>
    <recomm>
        <title>
            《神探蒲松龄》：到最后竟成了倩女幽魂！！！
        </title>
        <recommby>
            xiaoshitou01
        </recommby>
        <recommTime>
            2019-03-20 09:28:03
        </recommTime>
        <board>
            Recommend
        </board>
        <recommGID>
            1105509861
        </recommGID>
        <Author>
            zhu
        </Author>
        <originBoard>
            Movie
        </originBoard>
        <originBoardName>
            电影
        </originBoardName>
        <originArticleID>
            948903
        </originArticleID>
        <originGID>
            948903
        </originGID>
        <brief>
            http://blog.sina.com.cn/s/blog_4b104b190102yicv.html
《神探蒲松龄》：到最后竟成了倩女幽魂
2019年春节档众多大片中，最惨的当属成龙大哥主演的《神探蒲松龄》，不到2亿的票
房和豆瓣上3.9的评分让这部“东方魔幻巨制”落得个票房口碑双输的结果，金马影
        </brief>
    </recomm>
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
                        case "recomm":
                            briefArticle = new BriefArticle();
                            break;
                        case "title":
                            briefArticle.setTitle(parser.nextText());
                            break;
                        case "Author":
                            briefArticle.setAuthor(parser.nextText());
                            break;
                        case "originBoard":
                            briefArticle.setBoardID(parser.nextText());
                            break;
                        case "originBoardName":
                            briefArticle.setBoardName(parser.nextText());
                            break;
                        case "originGID":
                            briefArticle.setGID(parser.nextText());
                            break;
                        case "recommTime":
                            briefArticle.setTime(parser.nextText());
                            break;
                    }
                } else if (event == XmlPullParser.END_TAG) {
                    if (parser.getName().equals("recomm")) {
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
