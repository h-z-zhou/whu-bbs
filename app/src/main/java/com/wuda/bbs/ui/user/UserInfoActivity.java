package com.wuda.bbs.ui.user;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FriendDao;
import com.wuda.bbs.ui.mail.SendMailActivity;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.UserInfoResponseHandler;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    private View root;

    private ImageView photo_iv;
    private ImageView avatar_iv;
    private TextView name_tv;
    private TextView level_tv;
    private TextView experience_tv;
    private TextView postNum_tv;
    private TextView signature_tv;
    private Button friend_btn;
    private Button chat_btn;

    private UserInfoViewModel mViewModel;
    private boolean isFriend;
    FriendDao friendDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        root = findViewById(R.id.userInfo_root);
        photo_iv = findViewById(R.id.userInfo_photo_imageView);
        avatar_iv = findViewById(R.id.userInfo_avatar_imageView);
        name_tv = findViewById(R.id.userInfo_name_textView);
        level_tv = findViewById(R.id.userInfo_level_textView);
        experience_tv = findViewById(R.id.userInfo_exp_textView);
        postNum_tv = findViewById(R.id.userInfo_postNum_textView);
        signature_tv = findViewById(R.id.userInfo_signature_textView);
        friend_btn = findViewById(R.id.userInfo_friend_button);
        chat_btn = findViewById(R.id.userInfo_chat_button);

        root.setVisibility(View.INVISIBLE);

        mViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        mViewModel.userId = getIntent().getStringExtra("userId");

        eventBinding();

        friendDao = AppDatabase.getDatabase(UserInfoActivity.this).getFriendDao();
        isFriend = friendDao.loadFriend(mViewModel.userId) != null;
        setFriendBtnText();

        requestUserInfoFromServer();

    }

    private void eventBinding() {
        mViewModel.userInfo.observe(this, new Observer<UserInfo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(UserInfo userInfo) {

                if (!userInfo.getPhoto().equals("")) {
                    Glide.with(UserInfoActivity.this)
                            .load(NetConst.BASE + userInfo.getPhoto())
                            .error(R.mipmap.default_user_photo)
                            .into(photo_iv);
                } else {
                    Glide.with(UserInfoActivity.this)
                            .load(R.mipmap.default_user_photo)
                            .into(photo_iv);
                }

                Glide.with(UserInfoActivity.this).load(NetConst.BASE + userInfo.getAvatar()).into(avatar_iv);
                name_tv.setText(userInfo.getId() + "(" + userInfo.getNickname() + ")" + "\n" + userInfo.getGender());
                level_tv.append(userInfo.getLevel());
                experience_tv.append(userInfo.getExp());
                postNum_tv.append(userInfo.getPostNum());
                signature_tv.append(Html.fromHtml(userInfo.getSignature()));
                root.setVisibility(View.VISIBLE);
            }
        });

        friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateFriend();
            }
        });

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, SendMailActivity.class);
                intent.putExtra("userId", mViewModel.userId);
                startActivity(intent);
            }
        });
    }


    private void requestUserInfoFromServer() {

        NetworkEntry.requestUserInfo(mViewModel.userId, new UserInfoResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<UserInfo> response) {

                if (response.isSuccessful()) {
                    UserInfo userInfo = response.getContent();
                    userInfo.setId(mViewModel.userId);
                    mViewModel.userInfo.postValue(userInfo);
                } else {
                    new AlertDialog.Builder(UserInfoActivity.this)
                            .setMessage("该用户不存在")
                            .setCancelable(false)
                            .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UserInfoActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        });

    }

    private void operateFriend() {
        Map<String, String> form = new HashMap<>();
        form.put("app", "friend");
        if (isFriend) {
            form.put("delete", mViewModel.userId);
        } else {
            form.put("add", mViewModel.userId);
        }

        NetworkEntry.operateFriend(form, new SimpleResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<Object> response) {
                isFriend = !isFriend;
                Friend friend = new Friend(mViewModel.userInfo.getValue().getId(), "", mViewModel.userInfo.getValue().getAvatar());
                if (isFriend) {
                    friendDao.insertFriend(friend);
                } else {
                    friendDao.deleteFriend(friend);
                }
                setFriendBtnText();
                Toast.makeText(UserInfoActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFriendBtnText() {
        if (isFriend) {
            friend_btn.setText("删除好友");
        } else {
            friend_btn.setText("添加好友");
        }
    }

}