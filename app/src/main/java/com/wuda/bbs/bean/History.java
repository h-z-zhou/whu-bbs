package com.wuda.bbs.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {
    @PrimaryKey(autoGenerate = true)
    public long id;
    String title;
    String boardID;
    String GID;
    long readTime;

    public History() {
    }

    public History(String title, String boardID, String GID, long readTime) {
        this.title = title;
        this.boardID = boardID;
        this.GID = GID;
        this.readTime = readTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getGID() {
        return GID;
    }

    public void setGID(String GID) {
        this.GID = GID;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
