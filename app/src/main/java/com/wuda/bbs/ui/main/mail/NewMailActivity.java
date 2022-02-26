package com.wuda.bbs.ui.main.mail;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FriendDao;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class NewMailActivity extends AppCompatActivity {

    TextInputEditText userId_et;
    TextInputEditText title_et;
    TextInputEditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mail);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("写信");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userId_et = findViewById(R.id.newMail_userId_editText);
        title_et = findViewById(R.id.newMail_title_editText);
        content_et = findViewById(R.id.newMail_content_editText);

        String userId = getIntent().getStringExtra("userId");
        String title = getIntent().getStringExtra("title");

        if (userId != null) {
            userId_et.setText(userId);
        }

        if (title != null) {
            title_et.setText(title);
        }

        TextView friend_tv= findViewById(R.id.newMail_friend_textView);
        friend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendDao friendDao = AppDatabase.getDatabase(NewMailActivity.this).getFriendDao();
                List<Friend> friendList = friendDao.loadAllFriends();
                String[] ids = new String[friendList.size()];
                for (int i=0; i<friendList.size(); i++) {
                    ids[i] = friendList.get(i).getId();
                }

                new AlertDialog.Builder(NewMailActivity.this)
                        .setTitle("请选择好友")
                        .setItems(ids, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userId_et.setText(ids[which]);
                            }
                        })
                        .create()
                        .show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_mail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_mail_send) {
            sendMail();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMail() {

        Editable userId = userId_et.getText();
        if (userId==null || userId.length()==0) {
            Toast.makeText(NewMailActivity.this, "收件人为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Editable title = title_et.getText();
        if (title==null || title.length()==0) {
            Toast.makeText(NewMailActivity.this, "标题为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Editable content = content_et.getText();
        if (content==null || content.length()==0) {
            Toast.makeText(NewMailActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> form = new HashMap<>();
        form.put("destid", userId.toString());
        form.put("title", title.toString());
        form.put("content", content.toString());
        form.put("signature", "0");
        form.put("backup", "on");

        NetworkEntry.sendMail(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(NewMailActivity.this, response.getContent().getResult(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}