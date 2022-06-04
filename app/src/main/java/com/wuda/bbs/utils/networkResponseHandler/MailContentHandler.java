package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MailContentHandler implements ContentResponseHandler<String>{

    @Override
    public ContentResponse<String> handleNetworkResponse(@NonNull byte[] data) {
        /*
        <?xml version="1.0" encoding="utf-8"?>
<Mail num="0" sender="deliver" isnew="" title="致新注册用户的信" size="511" time="2021-10-08 18:35:53">
    prints('\n\n\r[1;34m   \r[35m尊敬的站友:\r[m\n\r[1;31m     您好！请您在实名认证审核相关页面，如实填报个人信息并上传真实、清晰、有效的个人在校（校园卡、工作证、学生证）和身份证照片。我们将在收到您的实名认证身份确认信息及资料后的三个工作日内，完成实名认证审核。感谢您对我们工作的理解和支持。\r[m\n\n\r[1;34m                                                                                    \r[35m武汉大学珞珈山水站务组\r[m\n\r[1;34m                                                                                    \r[35m2019年5月\r[m\n\n');
</Mail>
         */

        ContentResponse<String> response = new ContentResponse<>();

        String xml = new String(data);

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();
            String content = "";

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("Mail")) {
                        content = parser.nextText();
                    }
                }

                event = parser.next();
            }

            response.setContent(beautifyContent(content));

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;
    }

    private String beautifyContent(String content){
        Pattern pattern = Pattern.compile("(?<=\\\\n\\\\n)(.*?)(?=\\\\n'\\);)");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            content = matcher.group();
        }
        content = content.replaceAll("\\\\n", "");
        content = content.replaceAll("\\\\r.*?m", "\n");

        return content.trim();
    }
}


