package com.wuda.bbs.logic.bean.campus;


import com.wuda.bbs.ui.campus.detial.BookDetailFragment;
import com.wuda.bbs.utils.campus.ServerURL;

public class BookItemBean extends ToolBean {

    String author;
    String publisher;

    public BookItemBean(String name, String author, String publisher, String url) {
        targetFragmentClz = BookDetailFragment.class;
        this.name = name;
        this.author = author;
        this.publisher = publisher;
        this.url = url;
    }

    @Override
    public String getUrl() {
        return ServerURL.LIB + "/" + url;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

}
