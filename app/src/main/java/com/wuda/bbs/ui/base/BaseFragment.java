package com.wuda.bbs.ui.base;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    public void showActionBar(String title) {
        if (getActivity()!=null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
                if (!actionBar.isShowing()) {
                    actionBar.show();
                }
            }
        }
    }

    public void hideActionBar() {
        if (getActivity()!=null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                if (actionBar.isShowing()) {
                    actionBar.hide();
                }
            }
        }
    }
}
