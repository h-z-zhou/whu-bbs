package com.wuda.bbs.utils.networkResponseHandler;

import androidx.annotation.NonNull;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.bean.FavArticle;
import com.wuda.bbs.logic.bean.Treasure;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.DetailBoardDao;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class FavArticleHandler implements ContentResponseHandler<List<FavArticle>> {
    @Override
    public ContentResponse<List<FavArticle>> handleNetworkResponse(@NonNull byte[] data) {

        ContentResponse<List<FavArticle>> response;

        try {
            List<Treasure> treasureList = HtmlParser.parseTreasures(new String(data, "GBK"));
            response = new ContentResponse<>();
            response.setContent(parseSrcUrl(treasureList));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = new ContentResponse<>(ResultCode.HANDLE_DATA_ERR, e.getMessage());
        }
        return response;
    }


    private List<FavArticle> parseSrcUrl(List<Treasure> treasureList) {

        List<FavArticle> favArticleList = new ArrayList<>();

        DetailBoardDao boardDao = AppDatabase.getDatabase(BBSApplication.getAppContext()).getDetailBoardDao();
        List<DetailBoard> allBoards = boardDao.loadAllBoards();

        Map<String, String> num2id = new HashMap<>();
        for (DetailBoard board: allBoards) {
            num2id.put(board.getNumber(), board.getId());
        }

        // url => bbscon.php?bid=102&id=1105517542
        // url => wForum/disparticle.php?boardName=Advice&ID=1105517558&pos=1

        for (Treasure treasure: treasureList) {

            FavArticle favArticle = new FavArticle();
            favArticle.setName(treasure.getName());
            favArticle.setDelUrl(treasure.getDelUrl());
            favArticle.setSrcUrl(treasure.getSrcUrl());

            String params = favArticle.getSrcUrl().split("\\?")[1];
            String[] param_arr = params.split("&");
            String bid, gid;
            if (param_arr.length == 2) {
                bid = param_arr[0].split("=")[1];
                bid = num2id.get(bid);
                gid = param_arr[1].split("=")[1];
            } else if (param_arr.length == 3) {
                bid = param_arr[0].split("=")[1];
                gid = param_arr[1].split("=")[1];
            } else {
                continue;
            }

            favArticle.setBoardId(bid);
            favArticle.setGroupId(gid);

            favArticleList.add(favArticle);
        }

        return favArticleList;
    }
}
