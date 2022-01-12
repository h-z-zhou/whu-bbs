package com.wuda.bbs.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.main.base.NavigationHost;

public class LoginActivity extends AppCompatActivity implements NavigationHost {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.login_fragment_container, new LoginFragment())
                .commit();
    }

    @Override
    public void navigationTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_fragment_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}