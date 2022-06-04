package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.bbs.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class FavBoardHandler implements ContentResponseHandler<List<FavBoard>> {
    @Override
    public ContentResponse<List<FavBoard>> handleNetworkResponse(@NonNull byte[] data) {
        /*
        <?xml version="1.0" encoding="utf-8"?>
<FavDir>
    <FavBrd bid="284" name="BBSApp" desc="山水客户端" flag="544" class="[校内]" bm="" articles="372" users="1"/>
    <FavBrd bid="36" name="FreeTalk" desc="无事闲聊" flag="4104" class="[休闲]" bm="lency F2" articles="24587" users="3"/>
    <FavBrd bid="32" name="Test" desc="测试版" flag="4104" class="[本站]" bm="" articles="6281" users="0"/>
</FavDir>
         */

        List<FavBoard> favBoardList = new ArrayList<>();
        ContentResponse<List<FavBoard>> response = new ContentResponse<>();

        try {
            String xml = new String(data);
            xml = xml.replaceAll("&", "&amp;");
            xml = xml.replaceAll("true", "\"true\"");

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {

                if (event == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("FavBrd")) {
                        String namespace = parser.getNamespace();
                        String id = parser.getAttributeValue(namespace, "name");
                        String name = parser.getAttributeValue(namespace, "desc");
                        String num = parser.getAttributeValue(namespace, "bid");
                        favBoardList.add(new FavBoard(id, name, num, BBSApplication.getAccountId()));
                    }
                }

                event = parser.next();
            }
            response.setContent(favBoardList);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;
    }
}
