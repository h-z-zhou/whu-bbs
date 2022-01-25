package com.wuda.bbs.ui.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.UserInfo;
import com.wuda.bbs.bean.UserInfoResponse;
import com.wuda.bbs.ui.adapter.DetailArticleRecyclerAdapter;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    private ImageView avatar_iv;
    private TextView name_tv;
    private TextView level_tv;
    private TextView experience_tv;
    private TextView postNum_tv;
    private TextView signature_tv;
    private Button add2friend_btn;
    private Button chat_btn;

    private UserInfoViewModel mViewModel;
    private String userId;
//    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        avatar_iv = findViewById(R.id.userInfo_avatar_imageView);
        name_tv = findViewById(R.id.userInfo_name_textView);
        level_tv = findViewById(R.id.userInfo_level_textView);
        experience_tv = findViewById(R.id.userInfo_exp_textView);
        postNum_tv = findViewById(R.id.userInfo_postNum_textView);
        signature_tv = findViewById(R.id.userInfo_signature_textView);
        add2friend_btn = findViewById(R.id.userInfo_add2friend_button);
        chat_btn = findViewById(R.id.userInfo_chat_button);

        mViewModel = new ViewModelProvider(this).get(UserInfoViewModel.class);
        mViewModel.userId = getIntent().getStringExtra("userId");

        eventBinding();

        requestUserInfoFromServer();

    }

    private void eventBinding() {
        mViewModel.userInfo.observe(this, new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                Glide.with(UserInfoActivity.this).load(NetConst.BASE + userInfo.getAvatar()).into(avatar_iv);
                name_tv.setText(userInfo.getId() + "(" + userInfo.getNickname() + ")");
                level_tv.append(userInfo.getLevel());
                experience_tv.append(userInfo.getExp());
                postNum_tv.append(userInfo.getPostNum());
                signature_tv.append(Html.fromHtml(userInfo.getSignature()));
            }
        });
    }


    private void requestUserInfoFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
//        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("userId", mViewModel.userId);
        mobileService.request("userInfo", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String text = response.body().string();
                        UserInfoResponse userInfoResponse = XMLParser.parseUserInfo(text);
                        if (userInfoResponse.isSuccessful()) {
                            UserInfo userInfo = userInfoResponse.getUserInfo();
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