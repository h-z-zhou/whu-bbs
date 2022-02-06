package com.wuda.bbs.ui.main.article;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.response.BaseResponse;
import com.wuda.bbs.bean.DetailBoard;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.network.WebForumService;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostArticleActivity extends AppCompatActivity {

    BaseBoard board;
    List<DetailBoard> allBoards;

    Toolbar toolbar;
    TextView boardName_tv;
    TextInputEditText title_et;
    TextInputEditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_article);

        toolbar = findViewById(R.id.postArticle_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        boardName_tv = findViewById(R.id.postArticle_boardName_TextView);
        title_et = findViewById(R.id.postArticle_title_editText);
        content_et = findViewById(R.id.postArticle_content_editText);

        board = (BaseBoard) getIntent().getSerializableExtra("board");

        if (board != null) {
            boardName_tv.setText(board.getName());
        }

        AppDatabase database = AppDatabase.getDatabase(PostArticleActivity.this);
        allBoards = database.getDetailBoardDao().loadAllBoards();
        database.close();

        PopupMenu boardMenu = new PopupMenu(this, boardName_tv);
        for (int i=0; i<allBoards.size(); i++) {
            boardMenu.getMenu().add(0, Menu.NONE, i, allBoards.get(i).getName());
        }
        boardMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                board = allBoards.get(item.getOrder());
                boardName_tv.setText(board.getName());
                return false;
            }
        });
        boardName_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardMenu.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_article_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_post_article) {
            postArticle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void postArticle(){
        Editable title = title_et.getText();
        Editable content = content_et.getText();
        if (title==null || content==null) {
            return;
        }

        if (title.length() == 0) {
            new AlertDialog.Builder(PostArticleActivity.this)
                    .setTitle("标题不可为空")
                    .setMessage("好看的人都写了，你怎么可以不写！")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        if (content.length() == 0) {
            new AlertDialog.Builder(PostArticleActivity.this)
                    .setTitle("内容不可为空")
                    .setMessage("把你心里的想法告诉我吧！")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        // board=&relID=0&font=&subject=&Content=&signature=
        Map<String, String> form = new HashMap<>();
        form.put("board", board.getId());
        form.put("relID", "0");
        form.put("font", "");
        form.put("subject", title.toString());
        form.put("Content", content.toString());
        form.put("signature", "");

        WebForumService webForumService = ServiceCreator.create(WebForumService.class);
        webForumService.post("dopostarticle.php", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                    BaseResponse baseResponse = HtmlParser.parsePostArticleResponse(text);
                    if (!baseResponse.isSuccessful()) {
                        new AlertDialog.Builder(PostArticleActivity.this)
                                .setTitle("出错啦")
                                .setMessage(baseResponse.getMassage())
                                .setPositiveButton("确定", null)
                                .create()
                                .show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}