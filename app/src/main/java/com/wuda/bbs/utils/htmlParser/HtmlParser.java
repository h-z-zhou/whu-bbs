package com.wuda.bbs.utils.htmlParser;

import android.util.Log;

import com.wuda.bbs.bean.Article;
import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.Board;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlParser {

    public static ArticleResponse parseNewsToday(String htmlData) {
        ArticleResponse articleResponse = new ArticleResponse();
        articleResponse.setTotalPage(1);
        articleResponse.setCurrentPage(1);

        Document doc = Jsoup.parse(htmlData);
        Elements tables = doc.getElementsByTag("table");
        if (tables.size() != 2) {
            articleResponse.setSuccess(false);
        } else {
            try {
                Elements trs = tables.get(1).getElementsByTag("tr");
                for (int i = 1; i < trs.size(); ++i) {
                    Article article = new Article();
                    Elements links = trs.get(i).getElementsByTag("a");
                    article.setGID(links.get(0).attr("href").split("=")[2]);
                    article.setTitle(links.get(0).text());
                    article.setAuthor(links.get(1).attr("href").split("=")[1]);
                    article.setBoardID(links.get(2).attr("href").split("=")[1]);
                    String boardName = links.get(2).text();
                    article.setBoardName(boardName.substring(1, boardName.length() - 1));

                    Elements tds = trs.get(i).getElementsByTag("td");
                    article.setTime(tds.get(tds.size() - 1).text());

                    articleResponse.addArticle(article);
                }
            } catch (Exception e) {
                articleResponse.setSuccess(false);
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

        return articleResponse;
    }

    public static List<Board> parseFavoriteBoard(String htmlData) {
        List<Board> favBoardList = new ArrayList<>();

        Document doc = Jsoup.parse(htmlData);

        for (Element js: doc.getElementsByTag("javascript")) {
            Log.d("js", js.text());
        }

        return favBoardList;
    }

}
