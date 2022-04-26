package com.wuda.bbs.logic.bean.campus;


import com.wuda.bbs.ui.campus.detial.YellowPageDetailFragment;

import java.io.Serializable;

public class ContactBean extends ToolBean {
    // 黄页
    String name;
    String phoneNumber;

    public ContactBean(String name, String phoneNumber) {
        targetFragmentClz = YellowPageDetailFragment.class;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getUrl() {
        return null;
    }
}
