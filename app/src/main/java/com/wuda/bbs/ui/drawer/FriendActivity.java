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
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.Friend;
import com.wuda.bbs.bean.response.FriendResponse;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.FriendDao;
import com.wuda.bbs.ui.adapter.FriendAdapter;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class FriendActivity extends AppCompatActivity {

    FriendViewModel mViewModel;

    RecyclerView friend_rv;

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
        friend_rv.setAdapter(new FriendAdapter(FriendActivity.this, new ArrayList<>()));

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
                ((FriendAdapter) friend_rv.getAdapter()).updateFriendList(friends);
                FriendDao friendDao = AppDatabase.getDatabase(FriendActivity.this).getFriendDao();
                friendDao.insertFriends(friends);
            }
        });
    }


    private void requestAllFriendsFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
//        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("list", "all");

        mobileService.get("friend", form).enqueue(new BBSCallback<ResponseBody>(FriendActivity.this) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String text = response.body().string();
                        FriendResponse friendResponse = XMLParser.parseFriends(text);
                        if (friendResponse.isSuccessful()) {
                            mViewModel.friendList.postValue(friendResponse.getFriendList());
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}