package com.wuda.bbs.logic.bean.response;

import com.wuda.bbs.logic.bean.DetailArticle;

import java.util.List;

public class DetailArticleResponse extends ContentResponse<List<DetailArticle>> {
    String GID;
    Integer replyNum;
    String flag;

    public DetailArticleResponse() {
    }

    public DetailArticleResponse(ResultCode resultCode, String massage) {
        super(resultCode, massage);
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
