package com.wuda.bbs.ui.setting;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.wuda.bbs.R;

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

        return view;
    }
}