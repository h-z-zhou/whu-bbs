package com.wuda.bbs.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.ui.account.AccountActivity;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;
import com.wuda.bbs.ui.campus.CampusActivity;
import com.wuda.bbs.ui.drawer.AboutActivity;
import com.wuda.bbs.ui.drawer.ColorThemeActivity;
import com.wuda.bbs.ui.drawer.FavArticleActivity;
import com.wuda.bbs.ui.drawer.FriendActivity;
import com.wuda.bbs.ui.drawer.HistoryActivity;
import com.wuda.bbs.utils.network.NetConst;

public class MainActivity extends CustomizedThemeActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView drawer_nav;
    BottomNavigationView bottom_nav;

    SwitchCompat nightMode_switch;

    ActivityResultLauncher<Intent> mAccountActivityLauncher;
    ActivityResultLauncher<Intent> mThemeActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data == null)
                                return;
                            boolean accountChanged = data.getBooleanExtra("accountChanged", false);
                            if (accountChanged) {
                                initDrawerHeader();
                            }
                        }
                    }
                });
        mThemeActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == RESULT_OK) {
//                            recreate();
//                        }
                        recreate();
                    }
                }
        );

        toolbar = findViewById(R.id.detailArticle_toolbar);
        toolbar.setTitle("主页");
//        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
//        if(getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
//        }

        drawer = findViewById(R.id.drawer_layout);
        drawer_nav = findViewById(R.id.drawer_nav_view);

        nightMode_switch = drawer_nav.getMenu().findItem(R.id.drawer_nav_color)
                .getActionView().findViewById(R.id.drawer_dark_theme_switch);


        bottom_nav = findViewById(R.id.bottom_nav_view);
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.bottom_nav_home, R.id.bottom_nav_board, R.id.bottom_nav_mail)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.bottom_nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottom_nav, navController);

        eventBinding();

        SharedPreferences sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        int nightMode = sharedPreferences.getInt("nightMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    private void eventBinding() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                    nightMode_switch.setChecked(true);
                }
                drawer.openDrawer(GravityCompat.START);

                initDrawerHeader();
            }
        });

        drawer_nav.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAccountActivity(false);

                drawer.close();
            }
        });

        drawer_nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;

                if (item.getItemId() == R.id.drawer_nav_fav_article) {
                    intent = new Intent(MainActivity.this, FavArticleActivity.class);
                } else if (item.getItemId() == R.id.drawer_nav_history) {
                    intent = new Intent(MainActivity.this, HistoryActivity.class);
                } else if (item.getItemId() == R.id.drawer_nav_friend) {
                    intent = new Intent(MainActivity.this, FriendActivity.class);
                } else if (item.getItemId() == R.id.drawer_nav_friend) {
                    intent = new Intent(MainActivity.this, FriendActivity.class);
                } else if (item.getItemId() == R.id.drawer_nav_campus) {
                    intent = new Intent(MainActivity.this, CampusActivity.class);
                } else if (item.getItemId() == R.id.drawer_nav_about) {
                    intent = new Intent(MainActivity.this, AboutActivity.class);
                } else if(item.getItemId() == R.id.drawer_nav_color) {
                    intent = new Intent(MainActivity.this, ColorThemeActivity.class);
                    mThemeActivityLauncher.launch(intent);
                    drawer.close();
                    return true;
                } else {
                    intent = new Intent();
                }

                startActivity(intent);

                drawer_nav.setCheckedItem(-1);

                drawer.close();
                return true;
            }
        });

        nightMode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int nightMode = isChecked? AppCompatDelegate.MODE_NIGHT_YES: AppCompatDelegate.MODE_NIGHT_NO;
                setNightMode(nightMode);
            }
        });
    }

    private void initDrawerHeader() {
        View header = drawer_nav.getHeaderView(0);

        ImageView drawer_userAvatar_iv;
        TextView drawer_userId_tv;
        drawer_userAvatar_iv = header.findViewById(R.id.drawerUserAvatar_imageView);
        drawer_userId_tv = header.findViewById(R.id.drawerUserId_textView);

        if (BBSApplication.isLogin()) {
            Glide.with(MainActivity.this).load(NetConst.BASE + BBSApplication.getAccountAvatar()).into(drawer_userAvatar_iv);
            drawer_userId_tv.setText(BBSApplication.getAccountId());
        } else {
            Glide.with(MainActivity.this).load(R.drawable.ic_face).into(drawer_userAvatar_iv);
            drawer_userId_tv.setText("guest");
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void openAccountActivity(boolean isLogin) {
        Intent intent = new Intent(MainActivity.this, AccountActivity.class);
        intent.putExtra("isLogin", !BBSApplication.isLogin() || isLogin);
        mAccountActivityLauncher.launch(intent);
    }

    private void setNightMode(int nightMode) {

        AppCompatDelegate.setDefaultNightMode(nightMode);

        SharedPreferences.Editor editor = getSharedPreferences("theme", MODE_PRIVATE).edit();
        editor.putInt("nightMode", nightMode);
        editor.apply();
    }
}