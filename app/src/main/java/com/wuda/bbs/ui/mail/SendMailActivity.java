package com.wuda.bbs.ui.mail;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.base.NavigationHost;

public class SendMailActivity extends AppCompatActivity implements NavigationHost {

    SendMailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        mViewModel = new ViewModelProvider(this).get(SendMailViewModel.class);

        String userId = getIntent().getStringExtra("userId");
        String title = getIntent().getStringExtra("title");
        if (userId != null) {
            mViewModel.userId = userId;
        }
        if (title != null) {
            mViewModel.title = title;
        }

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("写信");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        navigationTo(new NewMailFragment(), false);
    }


    @Override
    public void navigationTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.sendMail_container, fragment);
        if (addToBackstack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}