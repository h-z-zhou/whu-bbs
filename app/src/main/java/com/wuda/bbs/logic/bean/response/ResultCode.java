package com.wuda.bbs.logic.bean.response;

public enum ResultCode {

    SUCCESS(0, "成功"),
    UNKNOWN_ERR(-1, "未定义错误"),
    UNMATCHED_CONTENT_ERR(1, "数据匹配错误"), // 返回内容与定义的处理方式不一致
    CONNECT_ERR(2, "网络连接错误"),
    LOGIN_ERR(3, "登录失败"),
    USER_ERR(4, "该用户不存在"),
    PASSWORD(5, "密码错误"),
    EMPTY_DATA_ERR(6, "返回空数据"),
    HANDLE_DATA_ERR(7, "数据处理错误"),
    DATA_IO_ERR(8, "数据IO错误"),
    SERVER_HANDLE_ERR(9, "服务器处理错误"),  // 修改密码不通过等
    PERMISSION_DENIED(10, "没有权限");

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
