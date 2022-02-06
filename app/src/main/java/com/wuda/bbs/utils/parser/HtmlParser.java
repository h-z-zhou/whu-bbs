package com.wuda.bbs.utils.parser;

import com.wuda.bbs.bean.response.BaseResponse;
import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.bean.response.BriefArticleResponse;
import com.wuda.bbs.bean.Treasure;
import com.wuda.bbs.bean.response.UserParamResponse;

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
            briefArticleResponse.setSuccessful(false);
            briefArticleResponse.setMassage("未定义错误");
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
                briefArticleResponse.setSuccessful(false);
                briefArticleResponse.setMassage(e.getMessage());
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

    public static <T> List<Treasure> parseTreasures(Class<T> treasureClz,String htmlData) {
        List<Treasure> treasureList = new ArrayList<>();

        Document doc = Jsoup.parse(htmlData);
        Elements trs = doc.getElementsByTag("tr");

        int idx_tr = 3;

        for (; idx_tr<trs.size(); idx_tr++) {
            Elements links = trs.get(idx_tr).getElementsByTag("a");
            if (links.size() != 3)
                continue;

            Treasure treasure = null;
            try {
                treasure = (Treasure) treasureClz.newInstance();
                treasure.setName(links.get(0).text());
                treasure.setSrcUrl(links.get(0).attr("href"));
                treasure.setDelUrl(links.get(2).attr("href"));

                treasureList.add(treasure);

            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        return treasureList;
    }

    public static BaseResponse parseRegisterResponse(String htmlData) {

        BaseResponse response = new BaseResponse();

        Document doc = Jsoup.parse(htmlData);
        Elements infoTable = doc.getElementsByTag("table");
        if (infoTable.size() != 1) {
            response.setSuccessful(false);
            response.setMassage("未知错误");
        } else {
            response.setMassage(infoTable.get(0).text());
        }

        return response;

        /*
<table cellspacing="0" cellpadding="10" border="0" class="t1">
<tr><td class="t3">
<font style="color: #FF0000"><b>发生错误</b></font>
</td></tr>
<tr><td class="t4">
该用户名已经被使用!</td></tr>
</table><br /><br />
         */
    }

    public static BaseResponse parseSetPasswordResponse(String htmlData) {
        BaseResponse response = new BaseResponse();

        Document doc = Jsoup.parse(htmlData);
        Elements infoTable = doc.getElementsByTag("table");
        if (infoTable.size() != 1) {
            response.setMassage(doc.text());
        } else {
            response.setMassage(infoTable.get(0).text());
        }

        return response;
/*
<table class="error">
<tr><th>发生错误</th></tr>
<tr><td>密码不正确</td></tr>
</table>
 */
    }

    public static UserParamResponse parseUserParamResponse(String htmlData) {
        UserParamResponse response = new UserParamResponse();

        Document doc = Jsoup.parse(htmlData);
        Elements paramTable = doc.getElementsByTag("form");
        if (paramTable.isEmpty()) {
            response.setSuccessful(false);
            response.setMassage("未定义错误");
        } else {
            Elements trs = paramTable.get(0).getElementsByTag("tr");
            if (trs.size() != 11) {
                response.setSuccessful(false);
                response.setMassage("未定义错误");
            } else {
                List<Boolean> paramValues = new ArrayList<>();
                for (int i=1; i<10; i++) {
                    Elements tds = trs.get(i).getElementsByTag("td");
                    if (tds.size()!=2) {
                        response.setSuccessful(false);
                        response.setMassage("未定义错误");
                    }
                    Elements radios = tds.get(1).getElementsByTag("input");
                    for (Element radio: radios) {
                        if (radio.attr("value").equals("1")) {
                            paramValues.add(radio.hasAttr("checked"));
                        }
                    }
                }
                if (paramValues.size() != 9) {
                    response.setSuccessful(false);
                    response.setMassage("未定义错误");
                } else {
                    response.setParamValues(paramValues);
                }
            }
        }

        return response;
    }

    public static BaseResponse parsePostArticleResponse(String htmlData) {
        BaseResponse response = new BaseResponse();

        Document doc = Jsoup.parse(htmlData);
        Elements tables = doc.getElementsByClass("TableBody1");
        if (tables.isEmpty()) {
            response.setSuccessful(false);
            response.setMassage("未定义错误");
        } else {
            String text = tables.get(0).text();
            if (text.contains("错误")) {
                response.setSuccessful(false);
                response.setMassage(text);
            }
        }

        return response;
    }
}
