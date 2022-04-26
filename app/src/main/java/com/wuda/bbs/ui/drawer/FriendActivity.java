package com.wuda.bbs.ui.drawer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.Friend;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.FriendAdapter;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.CustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

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
        adapter.setOnFriendSelectedListener(new FriendAdapter.OnFriendSelectedListener() {
            @Override
            public void oFriendSelected(Friend friend) {
                Intent intent = new Intent(FriendActivity.this, UserInfoActivity.class);
                intent.putExtra("userId", friend.getId());
                startActivity(intent);
            }
        });
        friend_rv.setAdapter(adapter);

        eventBinding();
        mViewModel.requestAllFriendsFromServer();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.friend_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_add_friend) {
            TextInputEditText id_et = new TextInputEditText(FriendActivity.this);

            View view = LayoutInflater.from(FriendActivity.this).inflate(R.layout.dialog_input_view, null, false);
            TextInputEditText content_et = view.findViewById(R.id.dialog_input_editText);

            new CustomDialog(FriendActivity.this)
                    .setDialogTitle("请输入用户ID")
                    .setCustomView(view)
                    .setOnPositiveButtonClickedListener("确定", new CustomDialog.OnButtonClickListener() {
                        @Override
                        public void onButtonClick() {
                            if (content_et.getText() != null) {
                                String id = content_et.getText().toString();
                                if(!TextUtils.isEmpty(id)) {
                                    Intent intent = new Intent(FriendActivity.this, UserInfoActivity.class);
                                    intent.putExtra("userId", id);
                                    startActivity(intent);
                                }
                            }
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventBinding() {
        mViewModel.getFriendListMutableLiveData().observe(this, new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                adapter.setMore(false);
                adapter.setContents(friends);
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(FriendActivity.this)
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestAllFriendsFromServer();
                            }
                        })
                        .setOnNegativeButtonClickedLister(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                finish();
                            }
                        })
                        .show();
            }
        });
    }
}