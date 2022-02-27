package com.wuda.bbs.ui.drawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FriendDao;
import com.wuda.bbs.ui.adapter.FriendAdapter;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.utils.networkResponseHandler.FriendResponseHandler;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    FriendViewModel mViewModel;

    RecyclerView friend_rv;
    FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("我的好友");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewModel = new ViewModelProvider(this).get(FriendViewModel.class);


        friend_rv = findViewById(R.id.recyclerView);
        friend_rv.setLayoutManager(new LinearLayoutManager(FriendActivity.this));

        adapter = new FriendAdapter(FriendActivity.this, new ArrayList<>());
        friend_rv.setAdapter(adapter);


        eventBinding();
        requestAllFriendsFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.friend_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_friend) {
            TextInputEditText id_et = new TextInputEditText(FriendActivity.this);
            new AlertDialog.Builder(FriendActivity.this)
                    .setTitle("请输入好友ID")
                    .setView(id_et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Editable id = id_et.getText();
                            if (id == null || id.length() == 0) {
                                return;
                            }
                            Intent intent = new Intent(FriendActivity.this, UserInfoActivity.class);
                            intent.putExtra("userId", id.toString());
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventBinding() {
        mViewModel.friendList.observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                adapter.setMore(false);
                adapter.setContents(friends);
                FriendDao friendDao = AppDatabase.getDatabase(FriendActivity.this).getFriendDao();
                friendDao.insertFriends(friends);
            }
        });
    }


    private void requestAllFriendsFromServer() {
        NetworkEntry.requestFriend(new FriendResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<Friend>> response) {
                mViewModel.friendList.postValue(response.getContent());
            }
        });
    }
}