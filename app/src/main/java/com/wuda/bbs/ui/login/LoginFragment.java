package com.wuda.bbs.ui.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.bean.User;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.UserDao;
import com.wuda.bbs.utils.network.LoginService;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.network.ServiceCreator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    private TextInputLayout username_tl;
    private EditText username_et;
    private TextInputLayout passwd_tl;
    private EditText passwd_et;
    private Button login_btn;
    private Button find_passwd_btn;
    private Button register_btn;

    UserDao userDao;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        username_tl = view.findViewById(R.id.login_username_textInputLayout);
        username_et = view.findViewById(R.id.login_username_editText);
        passwd_tl = view.findViewById(R.id.login_passwd_textInputLayout);
        passwd_et = view.findViewById(R.id.login_passwd_editText);
        login_btn = view.findViewById(R.id.login_login_button);
        find_passwd_btn = view.findViewById(R.id.login_find_passwd_button);
        register_btn = view.findViewById(R.id.login_register_button);

        eventBinding();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        userDao = AppDatabase.getDatabase(requireContext()).getUserDao();
        mViewModel.historyUserList = userDao.loadAllUsers();
        for (User user: mViewModel.historyUserList) {
            if (user.flag == User.FLAG_CURRENT) {
                mViewModel.currentUser = user;
                break;
            }
        }

        username_et.setText(mViewModel.currentUser.getId());
        passwd_et.setText(mViewModel.currentUser.getPasswd());
    }

    private void eventBinding() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString();
                String passwd = passwd_et.getText().toString();

                if (username.isEmpty()) {
                    username_tl.setError("用户名不可为空");
                    return;
                }

                if (passwd.isEmpty()) {
                    passwd_tl.setError("密码不可为空");
                    return;
                }

                LoginService loginCall = ServiceCreator.create(LoginService.class);

                loginCall.login("login", username, passwd).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        List<String> cookies = response.headers().values("Set-Cookie");

                        Log.d("cookies", cookies.toString());

                        Map<String, String> cookiesFilter = new HashMap<>();

                        for (String cookie: cookies) {
                            String[] tmp = cookie.split(";");
                            if (tmp.length > 0) {
                                String[] name_value = tmp[0].split("=");
                                if (name_value.length == 2){
                                    cookiesFilter.put(name_value[0], name_value[1]);
                                }
                            }
                        }

                        // 是否成功登录
                        String userID = cookiesFilter.get("UTMPUSERID");
                        if (userID == null)
                            userID = "guest";
//                        assert userID != null;
                        if (userID.equals("guest") || userID.equals("deleted")) {
                            if (getContext() == null)
                                return;
                            new AlertDialog.Builder(getContext())
                                    .setTitle("登录失败")
                                    .setMessage("请检查用户名和密码")
                                    .create()
                                    .show();
                            // 不保存帐号
                            return;
                        }

                        mViewModel.updateCurrentUser(new User(username, passwd, User.FLAG_CURRENT));
                        userDao.insertUser(mViewModel.historyUserList);
                        for (User user: mViewModel.historyUserList) {
                            userDao.updateUser(user);
                        }

                        BBSApplication.setUserInfo(username, passwd);

//                        getActivity().finish();
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("用户注册")
                        .setMessage("该功能未实现，请前往网页完成注册！")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(NetConst.BASE));
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.currentUser.setId(username_et.getText().toString());
        mViewModel.currentUser.setPasswd(username_et.getText().toString());
    }

}