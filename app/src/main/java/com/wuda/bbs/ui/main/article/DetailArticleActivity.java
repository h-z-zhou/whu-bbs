package com.wuda.bbs.ui.main.article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.BriefArticle;
import com.wuda.bbs.bean.DetailArticle;
import com.wuda.bbs.bean.History;
import com.wuda.bbs.bean.response.DetailArticleResponse;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.HistoryDao;
import com.wuda.bbs.ui.adapter.DetailArticleRecyclerAdapter;
import com.wuda.bbs.ui.widget.TopicDecoration;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class DetailArticleActivity extends AppCompatActivity {

    BriefArticle mBriefArticle;
    boolean isFavor;
//    String boardId;
//    String groupId;
    DetailArticleResponse detailArticleResponse;

    Toolbar toolbar;
    RecyclerView article_rv;

    TextView reply_tv;
//    ImageView favor_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        mBriefArticle = (BriefArticle) getIntent().getSerializableExtra("briefArticle");
//        boardId = getIntent().getStringExtra("boardId");
//        groupId = getIntent().getStringExtra("groupId");

        article_rv = findViewById(R.id.detailArticle_recyclerView);
        article_rv.setLayoutManager(new LinearLayoutManager(DetailArticleActivity.this));
        article_rv.setAdapter(new DetailArticleRecyclerAdapter(DetailArticleActivity.this, mBriefArticle.getBoardID()));
        article_rv.addItemDecoration(new TopicDecoration(DetailArticleActivity.this));

        toolbar = findViewById(R.id.detailArticle_toolbar);


//        String title = getIntent().getStringExtra("title");
        String title = mBriefArticle.getTitle();
        if (title != null) {
            toolbar.setTitle(title);
        }

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reply_tv = findViewById(R.id.detailArticle_reply_textView);
//        favor_iv = findViewById(R.id.detailArticle_favor_imageView);

        eventBinding();

        requestContentFromServer();

        add2history();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_article_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_favor) {
            add2Favor();
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventBinding() {
        reply_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailArticleActivity.this, ReplyActivity.class);
                DetailArticle article = new DetailArticle();
                article.setId(mBriefArticle.getGID());
                article.setAuthor(mBriefArticle.getAuthor());
                article.setContent(mBriefArticle.getTitle());
//                article.setAuthor();
                intent.putExtra("article", article);
                intent.putExtra("boardId", mBriefArticle.getBoardID());
                startActivity(intent);
            }
        });

        // 取消收藏无法绑定
//        favor_iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isFavor = !isFavor;
//                if (isFavor) {
//                    favor_iv.setImageDrawable(getDrawable(R.drawable.ic_favor_filled));
//                    favor_iv.setColorFilter(Color.RED);
//                    addFavor();
//                } else {
//                    favor_iv.setImageDrawable(getDrawable(R.drawable.ic_favor));
//                    favor_iv.setColorFilter(null);
//                }
//            }
//        });

    }


    private void requestContentFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
//        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("GID", mBriefArticle.getGID());
        form.put("board", mBriefArticle.getBoardID());
        form.put("page", "1");
        Log.d("article", form.toString());

        mobileService.request("read", form).enqueue(new BBSCallback<ResponseBody>(DetailArticleActivity.this) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String text = response.body().string();
                        detailArticleResponse = XMLParser.parseDetailArticle(text);
                        ((DetailArticleRecyclerAdapter) article_rv.getAdapter()).updateDataSet(detailArticleResponse.getDetailArticleList());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void add2Favor() {
        RootService rootService = ServiceCreator.create(RootService.class);
        Map<String, String> form = new HashMap<>();
        // act=add&title=&type=0&pid=2&url=
        form.put("act", "add");
        form.put("title", mBriefArticle.getTitle());
        form.put("type", "0");
        form.put("pid", "2");
        // url => bbscon.php?bid=102&id=1105517542
        // url => wForum/disparticle.php?boardName=Advice&ID=1105517558&pos=1
        String url = "wForum/disparticle.php?boardName=" + mBriefArticle.getBoardID() + "&" + "ID=" + mBriefArticle.getGID() + "&pos=1";
        form.put("url", url);

        Map<String, String> encodedForm = NetTool.encodeUrlFormWithGBK(form);
        rootService.getWithEncoded("bbssfav.php", encodedForm).enqueue(new BBSCallback<ResponseBody>(DetailArticleActivity.this) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Toast.makeText(DetailArticleActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void add2history() {
        HistoryDao historyDao = AppDatabase.getDatabase(DetailArticleActivity.this).getHistoryDao();
        historyDao.insertHistory(
                new History(
                        mBriefArticle.getTitle(),
                        mBriefArticle.getBoardID(),
                        mBriefArticle.getGID(),
                        Calendar.getInstance().getTimeInMillis()
                )
        );
    }

}