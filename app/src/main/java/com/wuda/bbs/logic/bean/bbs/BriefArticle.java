package com.wuda.bbs.logic.bean.bbs;

import java.io.Serializable;

public class BriefArticle implements Serializable {
    String boardName;
    String boardID;
    String title;
    String author;
    String GID;
    // 回文转寄
    String reID;
    String replyNum;
    String time;
    int flag;

    public static int FLAG_NORMAL = 0, FLAG_TOP = 1, FLAG_SYSTEM = 2;

    public BriefArticle() {
    }

    public BriefArticle(String boardName, String boardID, String title, String author, String GID, String replyNum, String time, int flag) {
        this.boardName = boardName;
        this.boardID = boardID;
        this.title = title;
        this.author = author;
        this.GID = GID;
        this.replyNum = replyNum;
        this.time = time;
        this.flag = flag;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
    }

    public String getReID() {
        return reID;
    }

    public void setReID(String reID) {
        this.reID = reID;
    }

    public String getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(String replyNum) {
        this.replyNum = replyNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
