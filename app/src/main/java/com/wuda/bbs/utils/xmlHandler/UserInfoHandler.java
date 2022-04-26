package com.wuda.bbs.utils.xmlHandler;

import com.wuda.bbs.logic.bean.bbs.UserInfo;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class UserInfoHandler extends DefaultHandler {
    UserInfo userInfo;

//    StringBuilder id;
    StringBuilder nickname;
    StringBuilder gender;
    StringBuilder avatar;
    StringBuilder photo;
    StringBuilder level;
    StringBuilder postNum;
    StringBuilder exp;  // experience
    StringBuilder signature;

    String nodeName;

    public UserInfoHandler() {
        userInfo = new UserInfo();

//        id = new StringBuilder();
        nickname = new StringBuilder();
        gender = new StringBuilder();
        avatar = new StringBuilder();
        photo = new StringBuilder();
        level = new StringBuilder();
        postNum = new StringBuilder();
        exp = new StringBuilder();
        signature = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        nodeName = localName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        switch (nodeName) {
            case "userface_img":
                avatar.append(ch, start, length);
                break;
            case "photo_url":
                photo.append(ch, start, length);
                break;
            case "nickname":
                nickname.append(ch, start, length);
                break;
            case "sex":
                gender.append(ch, start, length);
                break;
            case "numposts":
                postNum.append(ch, start, length);
                break;
            case "userexp":
                exp.append(ch, start, length);
                break;
            case "explevel":
                level.append(ch, start, length);
                break;
            case "sigcontent":
                signature.append(ch, start, length);
                break;
        }
        /*
        <?xml version="1.0" encoding="utf-8"?>
<user>
    <userface_img>
        wForum/userface/image0.gif
    </userface_img>
    <photo_url></photo_url>
    <nickname>
        海猪
    </nickname>
    <sex>
        ??
    </sex>
    <OICQ></OICQ>
    <ICQ></ICQ>
    <MSN></MSN>
    <homepage></homepage>
    <userlevel>
        新人
    </userlevel>
    <numposts>
        0
    </numposts>
    <menpai>
        ????????
    </menpai>
    <numlogins>
        66
    </numlogins>
    <userexp>
        65
    </userexp>
    <explevel>
        新手上路
    </explevel>
    <uservalue>
        59
    </uservalue>
    <lastlogin>
        2022-01-20 17:36:19
    </lastlogin>
    <usermode>
        目前????站??
    </usermode>
    <lasthostIP>
        Android?突???
    </lasthostIP>
    <sigcontent>
        ??位同学?艿偷鳎从?签????
    </sigcontent>
</user>
         */

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        switch (localName) {
            case "userface_img":
                userInfo.setAvatar(avatar.toString());
                avatar.setLength(0);
                break;
            case "photo_url":
                userInfo.setPhoto(photo.toString());
                photo.setLength(0);
                break;
            case "nickname":
                userInfo.setNickname(nickname.toString());
                nickname.setLength(0);
                break;
            case "sex":
                // 男 => "??"，不使用.equals("??")，万一哪天就好了
                String _gender = gender.toString().equals("女") ? "女": "男";
                userInfo.setGender(_gender);
                gender.setLength(0);
                break;
            case "numposts":
                userInfo.setPostNum(postNum.toString());
                postNum.setLength(0);
                break;
            case "userexp":
                userInfo.setExp(exp.toString());
                exp.setLength(0);
                break;
            case "explevel":
                userInfo.setLevel(level.toString());
                level.setLength(0);
                break;
            case "sigcontent":
                userInfo.setSignature(signature.toString());
                signature.setLength(0);
                break;
        }
    }

    UserInfo getUserInfo() {
        return userInfo;
    }
}
