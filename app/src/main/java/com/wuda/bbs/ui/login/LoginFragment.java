package com.wuda.bbs.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.User;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.UserDao;
import com.wuda.bbs.utils.network.LoginService;
import com.wuda.bbs.utils.network.ServerURL;
import com.wuda.bbs.utils.network.ServiceCreator;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    private EditText username_et;
    private EditText passwd_et;
    private Button login_btn;
    private Button find_passwd_btn;
    private Button register_btn;

    UserDao userDao;
    private User currentUser;
    private List<User> allUser;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userDao = AppDatabase.getDatabase(getContext()).getUserDao();
        allUser = userDao.loadAllUsers();
        for (User user: allUser) {
            if (user.flag == 'c') {
                currentUser = user;
                break;
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        username_et = view.findViewById(R.id.login_username_editText);
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

        username_et.setText(mViewModel.currentUser.getName());
        passwd_et.setText(mViewModel.currentUser.getPasswd());
    }

    private void eventBinding() {
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString();
                String passwd = passwd_et.getText().toString();

                LoginService loginCall = ServiceCreator.create(LoginService.class);

                currentUser = new User(username, passwd, 'c');

                userDao.insertUser(currentUser);

                loginCall.login("login", "zhuzi", "x57805zhz").enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        List<String> cookies = response.headers().values("Set-Cookie");
                        Log.d("Cookies", cookies.toString());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
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
                                intent.setData(Uri.parse(ServerURL.BASE));
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
        mViewModel.currentUser.setName(username_et.getText().toString());
        mViewModel.currentUser.setPasswd(username_et.getText().toString());
    }

//    private LoginUser loadCurrentUser() {
//        LoginUser currentUser = new LoginUser();
//        if (getContext() != null) {
//            SharedPreferences sp = getContext().getSharedPreferences("User")
//        }
//        getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("currentUser", "guest");
//    }

}