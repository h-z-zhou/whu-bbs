package com.wuda.bbs.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.response.AccountResponse;
import com.wuda.bbs.ui.base.BaseFragment;

import java.util.List;

public class LoginFragment extends BaseFragment {

    private LoginViewModel mViewModel;
    private AccountSharedViewModel mSharedViewModel;

    private TextInputLayout userId_tl;
    private EditText userId_et;
    private TextInputLayout passwd_tl;
    private EditText passwd_et;
    private Button login_btn;
    private Button forgot_passwd_btn;
    private Button register_btn;

    private boolean userId_tl_isDropDown = false;

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

        showActionBar("登录");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mSharedViewModel = new ViewModelProvider(getActivity()).get(AccountSharedViewModel.class);

        if (!mViewModel.id.isEmpty() || !mViewModel.pwd.isEmpty()) {
            userId_et.setText(mViewModel.id);
            userId_et.setText(mViewModel.pwd);
        } else {
            // 已经登录过的时候，进入该页面
            Account account = mSharedViewModel.getCurrentAccount().getValue();
            if (account != null) {
                userId_et.setText(account.getId());
                passwd_et.setText(account.getPasswd());
            }
        }

        eventBinding();

    }

    private void eventBinding() {

        // 未登录状态，直接进入该页面，数据加载延迟
        mSharedViewModel.getCurrentAccount().observe(getViewLifecycleOwner(), new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (userId_et.getText().length() == 0) {
                    userId_et.setText(account.getId());
                    passwd_et.setText(account.getPasswd());
                }
            }
        });

        mViewModel.getAccountResponseLiveData().observe(getViewLifecycleOwner(), new Observer<AccountResponse>() {
            @Override
            public void onChanged(AccountResponse accountResponse) {
                if (accountResponse.isSuccessful()) {
                    mSharedViewModel.updateCurrentAccount(accountResponse.getAccount());
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                } else {
                    Toast.makeText(getContext(), accountResponse.getMassage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        userId_tl.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId_tl_isDropDown = !userId_tl_isDropDown;
                if (userId_tl_isDropDown) {
                    userId_tl.setEndIconDrawable(R.drawable.ic_arrow_drop_up);
                    PopupMenu userIdMenu = new PopupMenu(requireContext(), userId_tl);

                    List<Account> accounts = mSharedViewModel.getAllAccounts().getValue();
                    if (accounts == null)
                        return;
                    for (int i=0; i<accounts.size(); i++) {
                        userIdMenu.getMenu().add(0, Menu.NONE, i, accounts.get(i).getId());
                    }

                    userIdMenu.show();

                    userIdMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Account account = accounts.get(item.getOrder());
                            userId_et.setText(account.getId());
                            passwd_et.setText(account.getPasswd());

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
                login();
            }
        });

        forgot_passwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null && getActivity() instanceof AccountActivity) {
                    ((AccountActivity) getActivity()).navigationTo(new ForgotPasswordFragment(), true);
                }
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity()!=null && getActivity() instanceof AccountActivity) {
                    ((AccountActivity) getActivity()).navigationTo(new RegisterFragment(), true);
                }
            }

        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.id = userId_et.getText().toString();
        mViewModel.pwd = passwd_et.getText().toString();
    }

    private void login() {
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

        mViewModel.login(new Account(username, passwd));

    }
}