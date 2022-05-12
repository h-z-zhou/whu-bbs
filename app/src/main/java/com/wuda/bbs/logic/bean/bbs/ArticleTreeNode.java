package com.wuda.bbs.logic.bean.bbs;

import java.util.ArrayList;
import java.util.List;

public class ArticleTreeNode {
    String id;
    List<ArticleTreeNode> children;

    public ArticleTreeNode(String id) {
        children = new ArrayList<>();
    }

    public void addChild(String id) {
        children.add(new ArticleTreeNode(id));
    }
}
