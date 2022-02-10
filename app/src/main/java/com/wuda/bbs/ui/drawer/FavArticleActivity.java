package com.wuda.bbs.ui.drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.DetailBoard;
import com.wuda.bbs.bean.FavArticle;
import com.wuda.bbs.bean.Treasure;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.DetailBoardDao;
import com.wuda.bbs.ui.adapter.FavArticleAdapter;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.parser.HtmlParser;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        favArticle_rv.addItemDecoration(new DividerItemDecoration(FavArticleActivity.this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//                return 0;
                final int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FavArticleAdapter adapter = (FavArticleAdapter) favArticle_rv.getAdapter();
                if (adapter == null)
                    return;
                FavArticle favArticle = adapter.removeItem(position);
                removeFavArticle(favArticle);
            }
        });
        mItemTouchHelper.attachToRecyclerView(favArticle_rv);

        requestFavArticleFromServer();

    }

    private void requestFavArticleFromServer() {

        ProgressDialog progressDialog = new ProgressDialog(FavArticleActivity.this);
        progressDialog.setMessage("请求中~");
        progressDialog.show();

        RootService rootService = ServiceCreator.create(RootService.class);
        Map<String, String> form = new HashMap<>();
        form.put("pid", "2");
        rootService.get("bbssfav.php", form).enqueue(new BBSCallback<ResponseBody>(FavArticleActivity.this) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                    List<Treasure> treasureList = HtmlParser.parseTreasures(FavArticle.class, text);
                    List<FavArticle> favArticleList = parseSrcUrl(treasureList);
                    ((FavArticleAdapter) favArticle_rv.getAdapter()).updateFavArticles(favArticleList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                progressDialog.dismiss();
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

        // url => bbscon.php?bid=102&id=1105517542
        // url => wForum/disparticle.php?boardName=Advice&ID=1105517558&pos=1

        for (int i=0; i<treasureList.size(); i++) {
            FavArticle favArticle = ((FavArticle) treasureList.get(i));
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

    public void removeFavArticle(FavArticle favArticle) {
        RootService rootService = ServiceCreator.create(RootService.class);
        Map<String, String> form = NetTool.extractUrlParam(favArticle.getDelUrl());
        rootService.get("bbssfav.php", form).enqueue(new BBSCallback<ResponseBody>(FavArticleActivity.this) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
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
}