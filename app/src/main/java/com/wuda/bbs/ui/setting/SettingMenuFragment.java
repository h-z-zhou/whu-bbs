package com.wuda.bbs.ui.setting;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.setting.account.SetParamFragment;
import com.wuda.bbs.ui.setting.account.SetPasswordFragment;
import com.wuda.bbs.ui.setting.account.UserManagerFragment;

public class SettingMenuFragment extends Fragment {

    SettingActivity settingActivity;

    public SettingMenuFragment() {
    }

    public static SettingMenuFragment newInstance(String param1, String param2) {
        SettingMenuFragment fragment = new SettingMenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        settingActivity = (SettingActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_menu, container, false);

        Button personalParam_btn = view.findViewById(R.id.settingMenu_personalParam_button);
        personalParam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingActivity.navigationTo(new SetParamFragment(), true);
            }
        });

        Button setPassword_btn = view.findViewById(R.id.settingMenu_setPassword_button);
        setPassword_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingActivity.navigationTo(new SetPasswordFragment(), true);
            }
        });

        Button accountManager_btn = view.findViewById(R.id.settingMenu_accountManager_button);
        accountManager_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingActivity.navigationTo(new UserManagerFragment(), true);
            }
        });

        return view;
    }
}