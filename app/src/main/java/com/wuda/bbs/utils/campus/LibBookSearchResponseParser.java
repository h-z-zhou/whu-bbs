package com.wuda.bbs.utils.campus;

import com.wuda.bbs.logic.bean.campus.BookInfoBean;
import com.wuda.bbs.logic.bean.campus.BookItemBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibBookSearchResponseParser {

    public static Map<String, Object> handleSearchResponse(String response) {

        Map<String, Object> result = new HashMap<>();

        Document doc = Jsoup.parse(response);

        String header = doc.getElementsByClass("header").text();
        final String prefix = "（最大显示记录 ";
        final String suffix = "条）";
        Integer amount = Integer.parseInt(header.substring(header.indexOf(prefix)+prefix.length(), header.indexOf(suffix)));

        List<BookItemBean> bookList = new ArrayList<>();

        for (Element element: doc.getElementsByTag("li")) {
            String title = element.getElementsByTag("h3").get(0).text();
            String url = element.getElementsByClass("title").get(0).attr("href");

            String[] div = element.getElementsByTag("div").toString().split("<br>");
            Element infoDiv = element.getElementsByTag("div").get(0);

            infoDiv.select("br").append("\\n");
            String[] info = infoDiv.text().split("\\\\n");

            String author = info[0].trim();
            String publisher = info[1].trim();

            bookList.add(new BookItemBean(title, author, publisher, url));

        }

        result.put("bookList", bookList);
        result.put("amount", amount);

        return result;
    }

    public static BookInfoBean handleBookInfoResponse(String response) {
        Element info = Jsoup.parse(response).getElementsByClass("basicInfo").get(0);
        Elements detailList = info.getElementsByClass("detailList");
        String title = detailList.get(0).text().substring(3);
        String author = detailList.get(1).text().substring(3);
        String keyWord = detailList.get(2).text().substring(4);
        String publisher = detailList.get(3).text().substring(5);
        String ISBN = detailList.get(4).text().substring(5);
        String digest = detailList.get(5).text().substring(3);

        BookInfoBean bookInfo = new BookInfoBean(title, author, keyWord, publisher, ISBN, digest);

        for (Element table: info.getElementsByTag("table")) {
            // tr => th + td
            Elements tds = table.getElementsByTag("td");
            String status = tds.get(0).text();
            String returnDate = tds.get(1).text();
            String branch = tds.get(2).text();
            String shelfId = tds.get(3).text();
            String requestNum = tds.get(4).text();
            String barCode = tds.get(5).text();
            bookInfo.addCollectionInfo2List(new BookInfoBean.CollectionInfo(status, returnDate,
                    branch, shelfId, requestNum, barCode));
        }

        return  bookInfo;
    }
}
