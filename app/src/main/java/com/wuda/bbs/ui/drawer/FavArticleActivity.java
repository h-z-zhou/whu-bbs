package com.wuda.bbs.ui.drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.bean.DetailBoard;
import com.wuda.bbs.bean.FavArticle;
import com.wuda.bbs.bean.Treasure;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.DetailBoardDao;
import com.wuda.bbs.ui.adapter.FavArticleAdapter;
import com.wuda.bbs.utils.htmlParser.HtmlParser;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavArticleActivity extends AppCompatActivity {

    RecyclerView favArticle_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_article);

        Toolbar toolbar = findViewById(R.id.fav_article_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        favArticle_rv = findViewById(R.id.recyclerView);
        favArticle_rv.setLayoutManager(new LinearLayoutManager(FavArticleActivity.this));
        favArticle_rv.setAdapter(new FavArticleAdapter(FavArticleActivity.this, new ArrayList<>()));

        requestFavArticleFromServer();

    }

    private void requestFavArticleFromServer() {
        RootService rootService = ServiceCreator.create(RootService.class);
        Map<String, String> form = new HashMap<>();
        form.put("pid", "2");
        rootService.request("bbssfav.php", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                    List<Treasure> treasureList = HtmlParser.parseTreasures(FavArticle.class, text);
                    List<FavArticle> favArticleList = parseSrcUrl(treasureList);
                    ((FavArticleAdapter) favArticle_rv.getAdapter()).updateFavArticles(favArticleList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    private List<FavArticle> parseSrcUrl(List<Treasure> treasureList) {

        List<FavArticle> favArticleList = new ArrayList<>();

        DetailBoardDao boardDao = AppDatabase.getDatabase(FavArticleActivity.this).getDetailBoardDao();
        List<DetailBoard> allBoards = boardDao.loadAllBoards();

//        Map<String, String> id2num = allBoards.stream().collect(Collectors.toMap(DetailBoard::getId))
        Map<String, String> num2id = new HashMap<>();
        for (DetailBoard board: allBoards) {
            num2id.put(board.getNumber(), board.getId());
        }

        for (int i=0; i<treasureList.size(); i++) {
            FavArticle favArticle = ((FavArticle) treasureList.get(i));
            String params = favArticle.getSrcUrl().split("\\?")[1];
            String[] param_arr = params.split("&");
            if (param_arr.length != 2)
                continue;
            String bid = param_arr[0].split("=")[1];
            String gid = param_arr[1].split("=")[1];

            favArticle.setBoardId(num2id.get(bid));
            favArticle.setGroupId(gid);

            favArticleList.add(favArticle);
        }

        return favArticleList;
    }
}