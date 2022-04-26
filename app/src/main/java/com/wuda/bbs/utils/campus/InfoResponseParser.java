package com.wuda.bbs.utils.campus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wuda.bbs.logic.bean.campus.AnnouncementBean;
import com.wuda.bbs.logic.bean.campus.InfoBaseBean;
import com.wuda.bbs.logic.bean.campus.LectureBean;
import com.wuda.bbs.logic.bean.campus.MovieBean;
import com.wuda.bbs.logic.bean.campus.SchoolCalendar;
import com.wuda.bbs.logic.bean.campus.YellowPageBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InfoResponseParser {

    public static String utf8Converter(String text) {
        // 解析 utf-8 数据，非标准数据(%u -> \\u)
        // 无法使用Java内置的算法变换
        final StringBuilder buffer = new StringBuilder();
        for (int i=0; i<text.length()-1;) {
            if (text.charAt(i)=='%') {  // %  str 本身包含 % ？？
                if (text.charAt(i+1) == 'u') {  // %u  -> %u4e2d%u56fd => 中国
                    buffer.append((char)Integer.parseInt(text.substring(i+2, i+6), 16));
                    i = i + 6;
                } else {  // %  -> 20%3a10 => 20:10
                    buffer.append((char)Integer.parseInt(text.substring(i+1, i+3), 16));
                    i = i + 3;
                }
            } else {
                buffer.append(text.charAt(i));
                ++i;
            }
        }
        if (text.charAt(text.length()-1) == '}')
            buffer.append('}');  // i < str.length()-1

        return buffer.toString();
    }

    public static List<InfoBaseBean> handleMovieResponse(String response) {
        // gson
        try {
            JSONObject responseJson = new JSONObject(InfoResponseParser.utf8Converter(response));
            if (responseJson.getJSONObject("header").getString("code").equals("0")) {
                JSONArray movieJsonArray = responseJson.getJSONObject("body").getJSONArray("rows");
                List<InfoBaseBean> movieList = new Gson().fromJson(movieJsonArray.toString(),
                        new TypeToken<ArrayList<MovieBean>>(){}.getType());
                return movieList;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return null;
    }

    public static List<InfoBaseBean> handleLectureResponse(String response) {
        List<InfoBaseBean> lectureList = new ArrayList<>();
        // gson
        try {
            JSONObject responseJson = new JSONObject(response);
            if (responseJson.getString("code").equals("0")) {
                JSONObject data = responseJson.getJSONObject("data").getJSONObject("data");
                for (Iterator<String> it = data.keys(); it.hasNext(); ) {
                    String time = it.next();
                    JSONArray lectureArray = data.getJSONArray(time);
                    for (int i=0; i<lectureArray.length(); ++i) {
                        JSONObject lectureObj = (JSONObject) lectureArray.get(i);
                        LectureBean lecture = new Gson().fromJson(lectureObj.toString(), LectureBean.class);
                        lecture.time = time;
                        lectureList.add(lecture);
                    }
                }
            }
            return lectureList;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<InfoBaseBean> handleAnnouncementResponse(String response) {
        Document doc = Jsoup.parse(response);
        Elements lis = doc.getElementsByClass("article").get(0).getElementsByTag("li");

        List<InfoBaseBean> announcementList = new ArrayList<>();
        for (int i=1; i<lis.size(); ++i) {
            AnnouncementBean announcement = new AnnouncementBean();
            Elements divs = lis.get(i).getElementsByTag("div");
            announcement.title = divs.get(0).getElementsByTag("a").get(0).attr("title");
            if (announcement.title.contains("今日珞珈"))
                continue;
            announcement.url = divs.get(0).getElementsByTag("a").get(0).attr("href");
            announcement.department = divs.get(1).text();
            announcement.time = divs.get(2).text();
            announcementList.add(announcement);
        }

        return announcementList;
    }

    public static List<SchoolCalendar> handleSchoolCalendarResponse(String response) {
        Document doc = Jsoup.parse(response);
        List<SchoolCalendar> schoolCalendarList = new ArrayList<>();

        if (doc.getElementsByClass("substance_l").size() == 0) {
            return schoolCalendarList;
        }
        Element calendarItemElement = doc.getElementsByClass("substance_l").get(0);
        for (Element item: calendarItemElement.getElementsByTag("a")) {
            SchoolCalendar schoolCalendar = new SchoolCalendar();
            schoolCalendar.setName(item.text());
            schoolCalendar.setUrl(item.attr("href"));
            schoolCalendarList.add(schoolCalendar);
        }
        return schoolCalendarList;
    }

    public static List<InfoBaseBean> handleYellowPageResponse() {

        List<InfoBaseBean> yellowPageList = new ArrayList<>();

        yellowPageList.add(new YellowPageBean("校园110", "	68777110"));
        yellowPageList.add(new YellowPageBean("火警电话", "68766119"));
        yellowPageList.add(new YellowPageBean("校园医院急诊", "68766894"));
        yellowPageList.add(new YellowPageBean("中南医院急诊", "67813167"));
        yellowPageList.add(new YellowPageBean("人民医院急诊", "88041911"));
        yellowPageList.add(new YellowPageBean("校园网运行部", "68773808"));

        yellowPageList.add(new YellowPageBean("网上报修平台(1)", "68770677"));
        yellowPageList.add(new YellowPageBean("网上报修平台(2)", "68752252"));
        yellowPageList.add(new YellowPageBean("水电管理中心(运行)", "68778210"));
        yellowPageList.add(new YellowPageBean("水电管理中心(收费)", "68773203"));
        yellowPageList.add(new YellowPageBean("电话宽带报修台", "68755112"));
        yellowPageList.add(new YellowPageBean("查号咨询台", "	68755114"));

        yellowPageList.add(new YellowPageBean("校巴班车服务队(1)", "68752144"));
        yellowPageList.add(new YellowPageBean("校巴班车服务队(2)", "68752441"));
        yellowPageList.add(new YellowPageBean("医学部服务车队(固话)", "68759589"));
        yellowPageList.add(new YellowPageBean("医学部服务车队(手机)", "13971084811"));

        yellowPageList.add(new YellowPageBean("桂园餐厅", "68752376"));
        yellowPageList.add(new YellowPageBean("枫园食堂", "68752535"));
        yellowPageList.add(new YellowPageBean("梅园教工食堂", "68754427"));
        yellowPageList.add(new YellowPageBean("珞珈山庄(总台)", "68752935"));
        yellowPageList.add(new YellowPageBean("珞珈山庄(订餐)", "68752609"));
        yellowPageList.add(new YellowPageBean("星湖园餐厅", "68771521"));

        return yellowPageList;
    }
}
