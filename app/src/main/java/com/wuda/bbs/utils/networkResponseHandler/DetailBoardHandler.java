package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public abstract class DetailBoardHandler implements ContentResponseHandler<List<DetailBoard>> {
    @Override
    public ContentResponse<List<DetailBoard>> handleNetworkResponse(@NonNull byte[] data) {
        /*
        <?xml version="1.0" encoding="utf-8"?>
<Sections>
    <Section name="本站系统">
        <board num="102" name="共建山水" id="Advice"/>
        <board num="66" name="站务公告" id="Announce"/>
        <board num="226" name="BBS活动" id="BBSActivity" hasChildren="true">
            <board num="101" name="山水站庆" id="Anniversary"/>
            <board num="227" name="山水站衫" id="TShirt"/>
        </board>
        <board num="284" name="山水客户端" id="BBSApp"/>
        <board num="223" name="BBS统计数据" id="BBSData" hasChildren="true">
            <board num="4" name="本站的各类数据统计列表" id="BBSLists"/>
            <board num="12" name="酸甜苦辣" id="notepad"/>
            <board num="7" name="各项投票与结果" id="vote"/>
        </board>
    </Section>

    <Section name="武汉大学">
    </Section>

</Sections>
         */

        String xml = new String(data);
        xml = xml.replaceAll("&", "&amp;");
        xml = xml.replaceAll("true", "\"true\"");

        List<DetailBoard> boardList = new ArrayList<>();
        ContentResponse<List<DetailBoard>> response = new ContentResponse<>();

        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(xml));

            int event = parser.getEventType();
            String node;
            String section = "";
            while (event != XmlPullParser.END_DOCUMENT) {
                node = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    String namespace = parser.getNamespace();
                    switch (node) {
                        case "Section":
                            section = parser.getAttributeValue(namespace, "name");
                            break;
                        case "board":
                            if (parser.getAttributeCount() == 4) break;
                            String num = parser.getAttributeValue(namespace, "num");
                            String name = parser.getAttributeValue(namespace, "name");
                            String id = parser.getAttributeValue(namespace, "id");
                            boardList.add(new DetailBoard(id, name, num, section));
                            break;
                    }
                }
                event = parser.next();
            }
            response.setContent(boardList);
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            response.setResultCode(ResultCode.HANDLE_DATA_ERR);
        }

        return response;
    }
}
