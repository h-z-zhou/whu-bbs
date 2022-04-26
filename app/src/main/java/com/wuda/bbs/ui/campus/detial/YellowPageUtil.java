package com.wuda.bbs.ui.campus.detial;


import com.wuda.bbs.logic.bean.campus.ContactBean;

import java.util.ArrayList;
import java.util.List;

public class YellowPageUtil {
    public static List<ContactBean> getAllContacts() {

        List<ContactBean> contactList = new ArrayList<>();

        contactList.add(new ContactBean("校园110", "	68777110"));
        contactList.add(new ContactBean("火警电话", "68766119"));
        contactList.add(new ContactBean("校园医院急诊", "68766894"));
        contactList.add(new ContactBean("中南医院急诊", "67813167"));
        contactList.add(new ContactBean("人民医院急诊", "88041911"));
        contactList.add(new ContactBean("校园网运行部", "68773808"));

        contactList.add(new ContactBean("网上报修平台(1)", "68770677"));
        contactList.add(new ContactBean("网上报修平台(2)", "68752252"));
        contactList.add(new ContactBean("水电管理中心(运行)", "68778210"));
        contactList.add(new ContactBean("水电管理中心(收费)", "68773203"));
        contactList.add(new ContactBean("电话宽带报修台", "68755112"));
        contactList.add(new ContactBean("查号咨询台", "	68755114"));

        contactList.add(new ContactBean("校巴班车服务队(1)", "68752144"));
        contactList.add(new ContactBean("校巴班车服务队(2)", "68752441"));
        contactList.add(new ContactBean("医学部服务车队(固话)", "68759589"));
        contactList.add(new ContactBean("医学部服务车队(手机)", "13971084811"));

        contactList.add(new ContactBean("桂园餐厅", "68752376"));
        contactList.add(new ContactBean("枫园食堂", "68752535"));
        contactList.add(new ContactBean("梅园教工食堂", "68754427"));
        contactList.add(new ContactBean("珞珈山庄(总台)", "68752935"));
        contactList.add(new ContactBean("珞珈山庄(订餐)", "68752609"));
        contactList.add(new ContactBean("星湖园餐厅", "68771521"));

        return contactList;
    }
}
