package com.wuda.bbs.utils.htmlParser;

import android.util.Log;

import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.bean.BriefArticleResponse;
import com.wuda.bbs.bean.DetailBoard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public static BriefArticleResponse parseNewsToday(String htmlData) {
        BriefArticleResponse briefArticleResponse = new BriefArticleResponse();
        briefArticleResponse.setTotalPage(1);
        briefArticleResponse.setCurrentPage(1);

        Document doc = Jsoup.parse(htmlData);
        Elements tables = doc.getElementsByTag("table");
        if (tables.size() != 2) {
            briefArticleResponse.setSuccess(false);
        } else {
            try {
                Elements trs = tables.get(1).getElementsByTag("tr");
                for (int i = 1; i < trs.size(); ++i) {

                    BriefArticle briefArticle = new BriefArticle();
                    briefArticle.setFlag(BriefArticle.FLAG_SYSTEM);

                    Elements links = trs.get(i).getElementsByTag("a");
                    briefArticle.setGID(links.get(0).attr("href").split("=")[2]);
                    briefArticle.setTitle(links.get(0).text());
                    briefArticle.setAuthor(links.get(1).attr("href").split("=")[1]);
                    briefArticle.setBoardID(links.get(2).attr("href").split("=")[1]);
                    String boardName = links.get(2).text();
                    briefArticle.setBoardName(boardName.substring(1, boardName.length() - 1));

                    Elements tds = trs.get(i).getElementsByTag("td");
                    briefArticle.setTime(tds.get(tds.size() - 1).text());

                    briefArticleResponse.addArticle(briefArticle);
                }
            } catch (Exception e) {
                briefArticleResponse.setSuccess(false);
            }

            /*
                <tr>
        <td>
            <a href="disparticle.php?boardName=Physics&ID=1103392310" style='text-decoration:none;'>电子的理论</a>
        </td>
        <td>
            <a title="点击访问用户中心" href="dispuser.php?id=stoneblock" style='text-decoration:none;'>
                <font color='#666666'>stoneblock</font>
            </a>
        </td>
        <td>
            <a href="board.php?name=Physics" style='text-decoration:none;'>
                <font color='#666666'>[物理]</font>
            </a>
        </td>
        <td>
            <!--1970-01-01-->
            <font color=gray>2022-01-11 10:36:20</font>
        </td>
    </tr>
             */
        }

        return briefArticleResponse;
    }

    public static List<DetailBoard> parseFavouriteBoard(String htmlData) {

        List<DetailBoard> favDetailBoardList = new ArrayList<>();

        Document doc = Jsoup.parse(htmlData);

        for (Element js: doc.getElementsByTag("javascript")) {
            Log.d("js", js.text());
        }

        return favDetailBoardList;
    }

}
