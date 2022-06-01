package com.wuda.bbs.utils.network;

public class NetConst {
    public static final String BASE = "http://bbs.whu.edu.cn/";
    public static final String BASE_HOST = "bbs.whu.edu.cn";
    public static final String MOBILE = "http://bbs.whu.edu.cn/mobile.php";
    // post
    // 忘记密码
    public static final String FIND_PASSWORD = "http://bbs.whu.edu.cn/r/doreset.php";
    // 注册
    public static final String REGISTER = "http://bbs.whu.edu.cn/bbsreg.php";
    // 修改密码 post: pw1=&pw2=&pw3=
    public static final String SET_PASSWORD = "http://bbs.whu.edu.cn/bbspwd.php?do";
    // 昵称 post: nick=
    public static final String NICKNAME = "http://bbs.whu.edu.cn/wForum/dochangepasswd.php";
    // 签名 post: text=
    public static final String SET_SIGNATURE = "http://bbs.whu.edu.cn/wForum/bbssavesig.php";
    // 用户参数
    public static final String USER_PARAM = "http://bbs.whu.edu.cn/wForum/userparam.php";
    // 发布新话题 post board=&relID=0&font=&subject=&Content=&signature=
    public static final String POST_ARTICLE = "http://bbs.whu.edu.cn/wForum/dopostarticle.php";
    // 收藏文章
    public static final String FAVOR = "http://bbs.whu.edu.cn/bbssfav.php";

    // 图片
    public static final String ATTACHMENT = "http://bbs.whu.edu.cn/wForum/bbscon.php";

    public static final String WEB_READ_MAIL = "http://bbs.whu.edu.cn/bbsmailcon.php";
}
