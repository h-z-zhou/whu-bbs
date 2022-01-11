package com.wuda.bbs.bean;

public class Article {
    String boardName;
    String boardID;
    String title;
    String author;
    String GID;
    String replyNum;
    String time;

    public Article() {
    }

    public Article(String boardName, String boardID, String title, String author, String GID, String replyNum, String time) {
        this.boardName = boardName;
        this.boardID = boardID;
        this.title = title;
        this.author = author;
        this.GID = GID;
        this.replyNum = replyNum;
        this.time = time;
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
}
