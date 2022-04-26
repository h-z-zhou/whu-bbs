package com.wuda.bbs.logic.bean.campus;


import com.wuda.bbs.ui.campus.detial.YellowPageDetailFragment;
import com.wuda.bbs.utils.campus.ServerURL;

public class YellowPageBean extends InfoBaseBean{
    public String phoneNumber;

    public YellowPageBean(String title, String phoneNumber) {
        targetFragmentClz = YellowPageDetailFragment.class;
        this.title = title;
        this.phoneNumber = phoneNumber;
        this.time = "";
        this.read = true;
        name = "电话详情";
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getUrl() {
        return ServerURL.YELLOW_PAGES;
    }

    @Override
    public String getUniqueId() {
        return phoneNumber;
    }
}
