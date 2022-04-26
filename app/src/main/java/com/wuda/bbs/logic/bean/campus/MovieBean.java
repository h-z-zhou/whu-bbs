package com.wuda.bbs.logic.bean.campus;

import com.google.gson.annotations.SerializedName;
import com.wuda.bbs.ui.campus.detial.MovieDetailFragment;
import com.wuda.bbs.utils.campus.ServerURL;

public class MovieBean extends InfoBaseBean {
    // ID 请求详细信息必须
    @SerializedName("ID")
    String id;
    @SerializedName("MOVIE_TYPE")
    String type;
    @SerializedName("PLAY_PLACE")
    String place;
    @SerializedName("ACTORS")
    String actors;
    @SerializedName("FILE_ID")
    String poster;

    public MovieBean() {
        name = "电影详情";
        targetFragmentClz = MovieDetailFragment.class;
    }

    @Override
    public String getUrl() {
        return ServerURL.MOVIE;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPlace() {
        return place;
    }

    public String getActors() {
        return actors;
    }

    public String getPoster() {
        return "http://gh.whu.edu.cn/!service/file/~java/Downloader.get?type=thumb&id=" + poster;
    }

    @Override
    public String getUniqueId() {
        return id;
    }
}
