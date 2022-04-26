package com.wuda.bbs.utils.campus;

public class ServerURL {
    // CAS
    public static final String CAS_LOGIN = "https://cas.whu.edu.cn/authserver/login";
    public static final String CAS_ACCOUNT = "https://cas.whu.edu.cn/authserver/index.do";
    public static final String CAS_REDIRECTOR = "https://cas.whu.edu.cn/authserver/login?service=";

    // 研究生
    public static final String GRADUATE_SCORE = "http://yjs.whu.edu.cn/ssfw/pygl/cjgl/xscxck/cxckksq.do";
    // "http://yjs.whu.edu.cn/ssfw/pygl/xkgl/xskb.do";
    public static final String GRADUATE_SCHEDULE = "http://yjs.whu.edu.cn/ssfw/pygl/xkgl/xskb.do?sXqdm=44";

    // 通知公告
    public static final String ANNOUNCEMENT = "https://www.whu.edu.cn/tzgg.htm";

    // 梅操电影
    public static final String MOVIE = "http://gh.whu.edu.cn/pages/subMenu.jsp?code_table=BI_SCHOOL_CULTURE&code_num=4";
    public static final String MOVIE_JSON = "http://gh.whu.edu.cn/!gh/index/~query/Q_LOAD_SUB_MENU_DATA?" +
            "code_table=BI_SCHOOL_CULTURE&code_num=4&page=1&rows=6&__resultType=json&_sysCode=&t=";

    // 校历 => 变化
    public static final String CALENDAR_UC = "https://uc.whu.edu.cn";  // 本科生院
    public static final String CALENDAR = "https://uc.whu.edu.cn/xl/a2021_2022nxl.htm";

    // 黄页
    public static final String YELLOW_PAGES = "http://tele.whu.edu.cn/";

    // 今日珞珈
    public static final String LECTURE = "https://www.whu.edu.cn/xsrl.htm";
    // 需起始日期和终止日期 start_date=2022-01-01&&end_date=2022-01-07
    public static final String LECTURE_JSON = "https://www.whu.edu.cn/lectureApi.jsp";

    // 校园卡
    // get
    public static final String CAMPUS_CARD_PRE_LOGIN = "http://202.114.64.167/ias/prelogin?sysid=FWDT";
    public static final String CAMPUS_CARD_PRE_LOGIN_HOST = "202.114.64.167";
    // post
    // =>login, post: according to CAMPUS_CARD_PRE_LOGIN
    public static final String CAMPUS_CARD_LOGIN = "http://202.114.64.162/cassyno/index";
    // => info, post: json=true
    public static final String CAMPUS_CARD_INFO_JSON = "http://202.114.64.162/User/GetCardInfoByAccountNoParm";
    // 流水信息
    // ?sdate=2021-12-17&edate=2021-12-17&account=账户ID&page=1&rows=15
    public static final String CAMPUS_CARD_BILL_JSON = "http://202.114.64.162/Report/GetPersonTrjn";
    // 充值
    public static final String CAMPUS_CARD_PRE_DEPOSIT = "http://202.114.64.162/Page/Page";
    // post account=帐号&acctype=###&tranamt=3000&qpwd=MjQwODEx&paymethod=2&paytype=使用绑定的默认账号&client_type=web&json=true
    // => tranamt: 金额    qpwd：base64(密码)
    // response {"IsSucceed":true,"Msg":"{\"transfer\":{\"retcode\":\"0\", \"errmsg\":\"转账成功\",\"account\":\"帐号\",  \"tranamt\":\"3000\", \"acctype\":\"###\"} }","RMsg":null,"Obj":null,"Obj2":null,"Obj3":null,"Obj4":null,"Obj5":null,"Flag":null,"Flag7":null,"OrderID":null,"SoftAuth":null,"DownUrl":null,"Tel":null,"CopyRight":null,"AnswerUrl":null,"LogoutUrl":null,"ChangeLogonUrl":null,"GiveContent":null,"ServerUrl":null,"CardbagUrl":null,"PayFlag":null,"NO":null,"DisNet":null}
    public static final String CAMPUS_CARD_DEPOSIT = "http://202.114.64.162/User/Account_Pay";
    // 挂失 / 解挂
    // post acc=帐号&pwd=密码非加密&json=true
    public static final String CAMPUS_CARD_LOST = "http://202.114.64.162/User/LostCard";
    public static final String CAMPUS_CARD_UNLOST = "http://202.114.64.162/User/UnLostCard";

    // 网络自助
    public static final String NETWORK_SELF_SERVICE = "http://user-serv.whu.edu.cn:8080/selfservice/login_self2.jsp";

    // 图书馆
    public static final String LIB = "http://cxm.lib.whu.edu.cn";
    // 查找书刊  ?kw=***&filed=[wti | tit | wau | wsu]&page=
    public static final String LIB_SEARCH = "http://cxm.lib.whu.edu.cn/search";

    // 座位预约
    public static final String LIB_SEAT_LOGIN = "https://seat.lib.whu.edu.cn/login";
    public static final String LIB_SEAT_CAS = "https://seat.lib.whu.edu.cn/cas";
    public static final String LIB_SEAT = "https://seat.lib.whu.edu.cn";
    // TOKEN
    public static final String LIB_SEAT_SELF = "https://seat.lib.whu.edu.cn/self";
    public static final String LIB_SEAT_MAP = "https://seat.lib.whu.edu.cn/map";
    // 馆内房间：id： 1:信息馆  2:工学分馆  3:医学分馆  4:总馆
    public static final String LIB_SEAT_GET_ROOM = "https://seat.lib.whu.edu.cn/freeBook/ajaxGetRooms";  // ?id=1
    // 查找座位 ?onDate=2021-12-25&building=1&room=116&hour=null&startMin=null&endMin=null&power=null&window=null
    public static final String LIB_SEAT_QUERY_SEAT = "https://seat.lib.whu.edu.cn/freeBook/ajaxSearch";
    // 馆内楼层
    // https://seat.lib.whu.edu.cn/mapBook/ajaxGetFloor?id=1\
    // 房间布局
    // ?room=94&date=2022-01-28
    public static final String LIB_SEAT_QUERY_BY_ROOM = "https://seat.lib.whu.edu.cn/mapBook/getSeatsByRoom";
    // 座位可用时间
    // 开始时间 ?id=15808&date=2022-01-29"
    public static final String LIB_SEAT_START_TIME = "https://seat.lib.whu.edu.cn/freeBook/ajaxGetTime";
    // 结束时间 ?start=1020&seat=9180&date=2022-01-29
    public static final String LIB_SEAT_END_TIME = "https://seat.lib.whu.edu.cn/freeBook/ajaxGetEndTime";
    // 预约 post SYNCHRONIZER_TOKEN=6a549401-28f2-4b99-bc00-fc9bf1b82044&SYNCHRONIZER_URI=%2Fmap&date=2022-01-29&seat=15803&start=1200&end=1290&authid=-1
    public static final String LIB_SEAT_ORDER = "https://seat.lib.whu.edu.cn/selfRes";
    // 历史记录
    public static final String LIB_SEAT_HISTORY = "https://seat.lib.whu.edu.cn/history?type=SEAT";
    // 取消预约
    // https://seat.lib.whu.edu.cn/reservation/cancel/14469761
    public static final String LIB_SEAT_CANCEL = "https://seat.lib.whu.edu.cn/reservation/cancel/";

    // 空教室
    public static final String FREE_ROOM = "http://tsh.whu.edu.cn/#/tsh/service/classRoom?conCode=CON0000000012";
    public static final String FREE_ROOM_BUILDING = "http://tsh.whu.edu.cn/trp/getCampusBuildingTree";
    // building=&date=?
    public static final String FREE_ROOM_DATA = "http://tsh.whu.edu.cn/trp/getFreeRoomData";
}
