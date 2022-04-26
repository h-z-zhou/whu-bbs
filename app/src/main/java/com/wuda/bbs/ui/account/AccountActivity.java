package com.wuda.bbs.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.bbs.Account;
import com.wuda.bbs.ui.base.NavigationHost;

public class AccountActivity extends AppCompatActivity implements NavigationHost {

    Toolbar toolbar;
    AccountSharedViewModel mViewModel;

    boolean isLogin;  // go to login page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        toolbar = findViewById(R.id.back_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (savedInstanceState!=null && savedInstanceState.containsKey("isLogin")) {
            isLogin = savedInstanceState.getBoolean("isLogin");
            return;
        } else {
            isLogin = getIntent().getBooleanExtra("isLogin", true);
        }

        if (isLogin) {
            navigationTo(new LoginFragment(), false);
        } else {
            navigationTo(new AccountFragment(), false);
        }

        mViewModel = new ViewModelProvider(this).get(AccountSharedViewModel.class);
        mViewModel.getCurrentAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                Intent intent = new Intent();
                intent.putExtra("accountChanged", true);
                setResult(RESULT_OK, intent);
                BBSApplication.setAccount(account);
            }
        });
    }

    @Override
    public void navigationTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.account_container, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public void showActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.show();
        }
    }

    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isLogin", isLogin);
    }

}