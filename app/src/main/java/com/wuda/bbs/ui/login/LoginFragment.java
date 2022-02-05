package com.wuda.bbs.ui.login;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.bean.User;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.UserDao;
import com.wuda.bbs.utils.network.MobileService;
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

    private TextInputLayout userId_tl;
    private EditText userId_et;
    private TextInputLayout passwd_tl;
    private EditText passwd_et;
    private Button login_btn;
    private Button forgot_passwd_btn;
    private Button register_btn;

    private boolean userId_tl_isDropDown = false;

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

        userId_tl = view.findViewById(R.id.login_userId_textInputLayout);
        userId_et = view.findViewById(R.id.login_userId_editText);
        passwd_tl = view.findViewById(R.id.login_passwd_textInputLayout);
        passwd_et = view.findViewById(R.id.login_passwd_editText);
        login_btn = view.findViewById(R.id.login_login_button);
        forgot_passwd_btn = view.findViewById(R.id.login_forgot_passwd_button);
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

        userId_et.setText(mViewModel.currentUser.getId());
        passwd_et.setText(mViewModel.currentUser.getPasswd());
    }

    private void eventBinding() {

        userId_tl.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId_tl_isDropDown = !userId_tl_isDropDown;
                if (userId_tl_isDropDown) {
                    userId_tl.setEndIconDrawable(R.drawable.ic_arrow_drop_up);
                    PopupMenu userIdMenu = new PopupMenu(requireContext(), userId_tl);
                    for (int i=0; i<mViewModel.historyUserList.size(); i++) {
                        User user = mViewModel.historyUserList.get(i);
                        userIdMenu.getMenu().add(0, Menu.NONE, i, user.getId());
                    }
                    userIdMenu.show();

                    userIdMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            User user = mViewModel.historyUserList.get(item.getOrder());
                            userId_et.setText(user.getId());
                            passwd_et.setText(user.getPasswd());

                            userId_tl_isDropDown = false;
                            userId_tl.setEndIconDrawable(R.drawable.ic_arrow_drop_down);

                            return true;
                        }
                    });

                    userIdMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                        @Override
                        public void onDismiss(PopupMenu menu) {
                            userId_tl_isDropDown = false;
                            userId_tl.setEndIconDrawable(R.drawable.ic_arrow_drop_down);
                        }
                    });

                } else {
                    userId_tl.setEndIconDrawable(R.drawable.ic_arrow_drop_down);
                }
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userId_et.getText().toString();
                String passwd = passwd_et.getText().toString();

                if (username.isEmpty()) {
                    userId_tl.setError("用户名不可为空");
                    return;
                }

                if (passwd.isEmpty()) {
                    passwd_tl.setError("密码不可为空");
                    return;
                }

                Map<String, String> form = new HashMap<>();
                form.put("app", "login");
                form.put("id", username);
                form.put("passwd", passwd);

                MobileService mobileService = ServiceCreator.create(MobileService.class);
                mobileService.post(form).enqueue(new Callback<ResponseBody>() {
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

                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    }
                });
            }
        });

        forgot_passwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                if (loginActivity != null) {
                    loginActivity.navigationTo(new ForgotPasswordFragment(), true);
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                if (loginActivity != null) {
                    loginActivity.navigationTo(new RegisterFragment(), true);
                }
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.currentUser.setId(userId_et.getText().toString());
        mViewModel.currentUser.setPasswd(userId_et.getText().toString());
    }

}