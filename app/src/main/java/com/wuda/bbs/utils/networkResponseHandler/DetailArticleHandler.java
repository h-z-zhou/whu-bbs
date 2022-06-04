package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.bbs.DetailArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.DetailArticleResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.ArticleContentRegex;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class DetailArticleHandler implements ContentResponseHandler<List<DetailArticle>> {
    @Override
    public ContentResponse<List<DetailArticle>> handleNetworkResponse(@NonNull byte[] data) {
        /*
        <?xml version="1.0" encoding="utf-8"?>
<page GID="400" num="1" total="1" replynum="0" flag="NORMAL">
    <article>
        <floor>
            楼??
        </floor>
        <id>
            400
        </id>
        <author>
            zhuzi
        </author>
        <userface_img>
            wForum/uploadFace/Z/zhuzi.8395.jpg
        </userface_img>
        <content>
            prints('发信人: zhuzi (柱子), 信区: BBSApp\n标  题: 新版Android客户端\n发信站: 珞珈山水 (Fri Apr  1 19:23:46 2022), 转信\n\n## 0.0.5\n新增帖子链接分享\n修复重复请求、登录验证失效、未登录用户无对一年内帖子阅读权限等问题\n\n## 0.0.4\n新增主题切换\n\n## 0.0.3\n新增“回文转寄”跳转至原帖\n新增部分校园信息页面资源\n新增“关于”页面\nBug修复\n\n## 0.0.2\n回复中增加表情和图片上传\n\n## 0.0.1\n这是个人学习安卓附带的成果，基本把能接入的功能都接入了，但是遗留下无数\nBUG。\n\n目前空闲时间不多，随缘维护随缘修，随时跑路随时溜。\n\n[URL=https:\/\/wwm.lanzoub.com\/b036ugl9g]蓝奏云https:\/\/wwm.lanzoub.com\/b036ugl9g[\/URL]，密码:luojia\n\n\n\n--\n\n\r[36m※ 修改:・zhuzi 于 Jun  1 14:19:08 2022 修改本文・[FROM: 202.114.78.109]\r[m\n\r[m\r[34m※ 来源:・珞珈山水 http:\/\/bbs.whu.edu.cn・[FROM: 202.114.78.100]\r[m\n');attach('home.png', 185462, 760);attach('board.png', 202574, 186243);attach('mailbox.png', 40522, 388839);attach('drawer.png', 131572, 429385);attach('info.png', 98269, 560980);attach('download.png', 23317, 659270);
        </content>
    </article>
    <article>
        ...
    </article>
</page>
         */
        List<DetailArticle> articleList = new ArrayList<>();
        DetailArticleResponse response;
        try {

            String xml = new String(data);
            xml = xml.replaceAll("&", "&amp;");

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();

            DetailArticle article = new DetailArticle();
            while (event != XmlPullParser.END_DOCUMENT) {
                String node = parser.getName();
                if (event == XmlPullParser.START_TAG) {
                    switch (node) {
                        case "article":
                            article = new DetailArticle();
                            break;
                        case "id":
                            article.setId(parser.nextText());
                            break;
                        case "author":
                            article.setAuthor(parser.nextText());
                            break;
                        case "userface_img":
                            article.setUserFaceImg(parser.nextText());
                            break;
                        case "content":
                            String rawContent = parser.nextText();
                            article.setTime(ArticleContentRegex.getTime(rawContent));
                            article.setContent(ArticleContentRegex.getContent(rawContent));
                            article.setReply2username(ArticleContentRegex.getReply2username(rawContent));
                            article.setReply2content(ArticleContentRegex.getReply2content(rawContent));
                            article.setAttachmentList(ArticleContentRegex.getAttachments(rawContent));
                            break;
                    }
                } else if (event == XmlPullParser.END_TAG) {
                    if (node.equals("article")) {
                        articleList.add(article);
                    }
                }

                event = parser.next();
            }

            response = new DetailArticleResponse();
            response.setContent(articleList);

        } catch (XmlPullParserException | IOException e) {
            if (BBSApplication.isLogin())
                response = new DetailArticleResponse(ResultCode.HANDLE_DATA_ERR, e.getMessage());
            else {
                response = new DetailArticleResponse(ResultCode.NO_LOGIN_ERR, "未登陆用户无法查看一年前的帖子");
            }
        }

        return response;
    }


}
