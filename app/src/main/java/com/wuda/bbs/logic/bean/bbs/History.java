package com.wuda.bbs.logic.bean.bbs;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class History {
    @NonNull
    @PrimaryKey
    String GID;
    String title;
    String author;
    String boardID;
    long readTime;

    public History(@NonNull String GID, String title, String author, String boardID, long readTime) {
        this.GID = GID;
        this.title = title;
        this.author = author;
        this.boardID = boardID;
        this.readTime = readTime;
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

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    @NonNull
    public String getGID() {
        return GID;
    }

    public void setGID(@NonNull String GID) {
        this.GID = GID;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
