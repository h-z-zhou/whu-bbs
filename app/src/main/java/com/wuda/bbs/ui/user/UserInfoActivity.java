package com.wuda.bbs.ui.user;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.mail.SendMailActivity;
import com.wuda.bbs.utils.network.NetConst;

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
    boolean isStartup = true;

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

        mViewModel.queryFriendFromDB();
        mViewModel.requestUserInfoFromServer();

    }

    private void eventBinding() {

        mViewModel.getFriendStateMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isFriend) {
                if (isFriend) {
                    friend_btn.setText("删除好友");
                } else {
                    friend_btn.setText("添加好友");
                }
                if (isStartup) {
                    isStartup = false;
                } else {
                    Toast.makeText(UserInfoActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
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
        });

        mViewModel.getUserInfoMutableLiveData().observe(this, new Observer<UserInfo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(UserInfo userInfo) {

                if (!userInfo.getPhoto().equals("")) {
                    Glide.with(UserInfoActivity.this)
                            .load(userInfo.getPhoto())
                            .error(R.drawable.ic_login_bg)
                            .into(photo_iv);
                } else {
                    photo_iv.setColorFilter(Color.LTGRAY);
                    Glide.with(UserInfoActivity.this)
                            .load(R.drawable.ic_login_bg)
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
                mViewModel.operateFriend();
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

}