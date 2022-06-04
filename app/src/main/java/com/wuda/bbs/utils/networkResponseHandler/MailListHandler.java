package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class MailListHandler implements ContentResponseHandler<List<Mail>> {
    @Override
    public ContentResponse<List<Mail>> handleNetworkResponse(@NonNull byte[] data) {
        /*
        <Mails Page="1" TotalPage="1">
    <Mail num="0" sender="deliver" isnew="  " size="511" time="2021-10-08 18:35:53">
        致新注册用户的信
    </Mail>
</Mails>
         */

        List<Mail> mailList = new ArrayList<>();
        ContentResponse<List<Mail>> response = new ContentResponse<>();

        String xml = new String(data);

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();
            String page = "";
            String totalPages = "";

            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {

                    switch (parser.getName()) {
                        case "Mails":
                            page = parser.getAttributeValue("", "Page");
                            totalPages = parser.getAttributeValue("", "TotalPage");
                            break;
                        case "Mail":
                            Mail mail = new Mail();
                            mail.setNum(parser.getAttributeValue("", "num"));
                            mail.setNew(parser.getAttributeValue("", "isnew").equals("N "));
                            mail.setSender(parser.getAttributeValue("", "sender"));
                            mail.setTime(parser.getAttributeValue("", "time"));
                            mail.setSubject(parser.nextText());
                            mailList.add(mail);
                            break;
                    }
                }

                event = parser.next();
            }

            response.setContent(mailList);
            response.setCurrentPage(Integer.parseInt(page));
            response.setTotalPage(Integer.parseInt(totalPages));

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;

    }
}
