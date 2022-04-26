package com.wuda.bbs.logic.bean.campus;


import com.wuda.bbs.ui.campus.detial.AnnouncementDetailFragment;

public class AnnouncementBean extends InfoBaseBean {
    // content: 通过 detailURL 二次获取，唯一，等价于ID
    public String url;
    // 发布单位
    public String department;

    public AnnouncementBean() {
        name = "公告详情";
        targetFragmentClz = AnnouncementDetailFragment.class;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String getUrl() {
        return "https://www.whu.edu.cn/" + url;
    }

    @Override
    public String getUniqueId() {
        return url;
    }
}
