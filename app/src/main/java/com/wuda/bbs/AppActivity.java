package com.wuda.bbs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wuda.bbs.ui.login.LoginActivity;
import com.wuda.bbs.ui.main.MainActivity;

public class AppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}