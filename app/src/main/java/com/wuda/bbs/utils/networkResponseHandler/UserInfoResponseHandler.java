package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public abstract class UserInfoResponseHandler implements ContentResponseHandler<UserInfo> {

    @Override
    public ContentResponse<UserInfo> handleNetworkResponse(@NonNull byte[] data) {
        /*
        <?xml version="1.0" encoding="utf-8"?>
<user>
    <userface_img width="80" height="80">
        wForum/uploadFace/Z/zhuzi.8395.jpg
    </userface_img>
    <photo_url>
        https://s1.ax1x.com/2022/04/02/qoRCPH.png
    </photo_url>
    <nickname>
        柱子
    </nickname>
    <sex>
        ??
    </sex>
    <OICQ></OICQ>
    <ICQ></ICQ>
    <MSN></MSN>
    <homepage></homepage>
    <userlevel>
        用户
    </userlevel>
    <numposts>
        1
    </numposts>
    <menpai>
        ????????
    </menpai>
    <numlogins>
        631
    </numlogins>
    <userexp>
        1076
    </userexp>
    <explevel>
        高级站友
    </explevel>
    <uservalue>
        120
    </uservalue>
    <lastlogin>
        2022-06-04 15:53:47
    </lastlogin>
    <usermode>
        目前在站上，状态如下：
&lt;span class='blue'&gt;读信中&lt;/span&gt; &lt;span class='blue'&gt;查询网友&lt;/span&gt;
    </usermode>
    <lasthostIP>
        Android?突???
    </lasthostIP>
    <sigcontent>
        歇着吧，BUG修不完的
    </sigcontent>
</user>
         */

        ContentResponse<UserInfo> response = new ContentResponse<>();

        String xml = new String(data);

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();
            UserInfo userInfo = new UserInfo();

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    switch (parser.getName()) {
                        case "userface_img":
                            userInfo.setAvatar(parser.nextText());
                            break;
                        case "photo_url":
                            userInfo.setPhoto(parser.nextText());
                            break;
                        case "nickname":
                            userInfo.setNickname(parser.nextText());
                            break;
                        case "sex":
                            userInfo.setGender(parser.nextText().equals("女")? "女": "男");
                            break;
                        case "numposts":
                            userInfo.setPostNum(parser.nextText());
                            break;
                        case "userexp":
                            userInfo.setExperience(parser.nextText());
                            break;
                        case "explevel":
                            userInfo.setLevel(parser.nextText());
                            break;
                        case "sigcontent":
                            userInfo.setSignature(parser.nextText());
                            break;
                    }
                }

                event = parser.next();
            }

            response.setContent(userInfo);

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;

    }

}
