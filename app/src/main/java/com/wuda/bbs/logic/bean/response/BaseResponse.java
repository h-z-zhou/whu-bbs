package com.wuda.bbs.logic.bean.response;

public class BaseResponse {
    ResultCode resultCode = ResultCode.SUCCESS;
    String massage;

    public BaseResponse() {
    }

    public BaseResponse(ResultCode resultCode, String massage) {
        this.resultCode = resultCode;
        this.massage = massage;
    }

    public boolean isSuccessful() {
        return resultCode == ResultCode.SUCCESS;
    }

    public void setSuccessful(boolean successful) {
        if (successful) {
            resultCode = ResultCode.SUCCESS;
        } else {
            resultCode = ResultCode.ERROR;
        }
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }
}
