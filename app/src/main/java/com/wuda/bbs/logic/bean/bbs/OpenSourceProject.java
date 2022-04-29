package com.wuda.bbs.logic.bean.bbs;

public class OpenSourceProject {
    String name;
    String contributor;
    String url;
    String licence;
    String introduction;

    public OpenSourceProject(String name, String contributor, String url, String licence, String introduction) {
        this.name = name;
        this.contributor = contributor;
        this.url = url;
        this.licence = licence;
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public String getContributor() {
        return contributor;
    }

    public String getUrl() {
        return url;
    }

    public String getLicence() {
        return licence;
    }

    public String getIntroduction() {
        return introduction;
    }
}
