package com.wuda.bbs.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.main.base.NavigationHost;

public class MyInfoActivity extends AppCompatActivity implements NavigationHost {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
    }

    @Override
    public void navigationTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.myInfo_fragment_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}