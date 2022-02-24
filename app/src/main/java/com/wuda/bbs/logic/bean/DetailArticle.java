package com.wuda.bbs.logic.bean;

import java.io.Serializable;
import java.util.List;

public class DetailArticle implements Serializable {
    String floor;
    String id;
    String author;
    String userFaceImg;
    String content;
    String time;
    String reply2username;
    String reply2content;
    List<Attachment> attachmentList;

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserFaceImg() {
        return userFaceImg;
    }

    public void setUserFaceImg(String userFaceImg) {
        this.userFaceImg = userFaceImg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReply2username() {
        return reply2username;
    }

    public void setReply2username(String reply2username) {
        this.reply2username = reply2username;
    }

    public String getReply2content() {
        return reply2content;
    }

    public void setReply2content(String reply2content) {
        this.reply2content = reply2content;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }
}
