package com.wuda.bbs.utils.networkResponseHandler;

import android.util.Log;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.bbs.ArticleTreeNode;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.network.NetTool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ArticleTreeHandler implements ContentResponseHandler<ArticleTreeNode> {
    @Override
    public ContentResponse<ArticleTreeNode> handleNetworkResponse(@NonNull byte[] data) {
        ContentResponse<ArticleTreeNode> response;

        try {
            String text = new String(data, "GBK");
            Matcher matcher = Pattern.compile("<TABLE(.*?)</TABLE>").matcher(text);
            if (matcher.find()) {
                Document table = Jsoup.parse(matcher.group());

                Queue<ArticleTreeNode> nodeQueue = new LinkedList<>();
                int parentIndents = 0;

                Elements nodes = table.getElementsByTag("tr");
                for (Element node: nodes) {
                    int indents = node.getElementsByTag("img").size();
                    String href = node.getElementsByTag("a").attr("href");
                    if (href.isEmpty()) continue;
                    String id = NetTool.extractUrlParam(href).get("ID");
                }
            }

            response = new ContentResponse<>();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.DATA_IO_ERR);
        }
        return response;
    }
}
