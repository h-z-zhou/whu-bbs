package com.wuda.bbs.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.wuda.bbs.R;
import com.wuda.bbs.ui.drawer.FavArticleActivity;
import com.wuda.bbs.ui.drawer.HistoryActivity;
import com.wuda.bbs.ui.login.LoginActivity;
import com.wuda.bbs.ui.setting.SettingActivity;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView drawer_nav;
    BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.detailArticle_toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
//        }

        drawer = findViewById(R.id.drawer_layout);
        drawer_nav = findViewById(R.id.drawer_nav_view);

        bottom_nav = findViewById(R.id.bottom_nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.bottom_nav_home, R.id.bottom_nav_board, R.id.bottom_nav_mail)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.bottom_nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottom_nav, navController);

        eventBinding();
    }

    private void eventBinding() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);

            }
        });

        drawer_nav.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
//                drawer.close();
            }
        });

        drawer_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.drawer_nav_fav_article) {
                    Intent intent = new Intent(MainActivity.this, FavArticleActivity.class);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.drawer_nav_history) {
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                }
                else if (item.getItemId() == R.id.drawer_nav_setting) {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                }

                drawer.close();
                return true;
            }
        });
    }

}