package com.wuda.bbs.logic.bean.response;

public class ContentResponse<T> {

    ResultCode resultCode = ResultCode.SUCCESS;
    String massage;  // 附带信息，=> Error

    private T content;
    private int currentPage;
    private int totalPage;

    public ContentResponse() {
    }

    public ContentResponse(T content) {
        this.content = content;
    }

    public ContentResponse(ResultCode resultCode, String massage) {
        this.resultCode = resultCode;
        this.massage = massage;
    }

    public ContentResponse(T content, int currentPage, int totalPage) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    public ContentResponse(ResultCode resultCode, String massage, T content, int currentPage, int totalPage) {
        this.resultCode = resultCode;
        this.massage = massage;
        this.content = content;
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    public boolean isSuccessful() {
        return resultCode == ResultCode.SUCCESS;
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

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
