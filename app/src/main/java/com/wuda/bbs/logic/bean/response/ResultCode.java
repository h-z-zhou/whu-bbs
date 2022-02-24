package com.wuda.bbs.logic.bean.response;

public enum ResultCode {

    SUCCESS(0, "成功"),
    ERROR(-1, "错误"),

    CONNECT_ERR(-2, "网络连接失败"),

    LOGIN_ERR(1, "登录失败"),
    DATA_ERR(2, "数据错误"),
    ;

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
