package com.wuda.bbs.ui.campus;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.toolkit.AnnouncementFragment;
import com.wuda.bbs.ui.campus.toolkit.FreeRoomFragment;
import com.wuda.bbs.ui.campus.toolkit.LectureFragment;
import com.wuda.bbs.ui.campus.toolkit.LibBookSearchFragment;
import com.wuda.bbs.ui.campus.toolkit.MovieFragment;
import com.wuda.bbs.ui.campus.toolkit.SchoolCalendarFragment;
import com.wuda.bbs.ui.campus.toolkit.YellowPageFragment;
import com.wuda.bbs.utils.campus.ServerURL;

import java.util.ArrayList;
import java.util.List;

public class ToolProvider {
    public static List<ToolBean> getAllTools() {
        List<ToolBean> tools = new ArrayList<>();

        tools.add(new ToolBean(R.drawable.ic_announcement, "#234534", "通知公告", ServerURL.ANNOUNCEMENT, AnnouncementFragment.class));
        tools.add(new ToolBean(R.drawable.ic_movie, "#986764", "梅操电影", ServerURL.MOVIE, MovieFragment.class));
        tools.add(new ToolBean(R.drawable.ic_calendar, "#abc344", "校历", ServerURL.CALENDAR, SchoolCalendarFragment.class));
        tools.add(new ToolBean(R.drawable.ic_yellow_page, "#dacd5e", "电话本", ServerURL.YELLOW_PAGES, YellowPageFragment.class));
        tools.add(new ToolBean(R.drawable.ic_lecture, "#324654", "今日珞珈", ServerURL.LECTURE, LectureFragment.class));
        tools.add(new ToolBean(R.drawable.ic_search, "#ac3242", "查找图书", ServerURL.LIB_SEARCH, LibBookSearchFragment.class));
        tools.add(new ToolBean(R.drawable.ic_room, "#723d23", "空教室", ServerURL.FREE_ROOM, FreeRoomFragment.class));

        return tools;
    }
}
