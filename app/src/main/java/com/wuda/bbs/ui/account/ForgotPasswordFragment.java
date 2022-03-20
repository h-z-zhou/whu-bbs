package com.wuda.bbs.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wuda.bbs.R;
import com.wuda.bbs.ui.base.BaseFragment;

public class ForgotPasswordFragment extends BaseFragment {

    Button findPasswdByEmail_btn;
    Button findPasswdByAuthInfo_btn;
    Button findPasswdByArtificial_btn;

    public ForgotPasswordFragment() {
    }

    public static ForgotPasswordFragment newInstance(String param1, String param2) {
        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        findPasswdByEmail_btn = view.findViewById(R.id.forgotPassword_findByEmail_button);
        findPasswdByAuthInfo_btn = view.findViewById(R.id.forgotPassword_findByAuthInfo_button);
        findPasswdByArtificial_btn = view.findViewById(R.id.forgotPassword_findByArtificial_button);

        AccountActivity accountActivity = (AccountActivity) getActivity();

        if (accountActivity != null) {

            findPasswdByEmail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountActivity.navigationTo(new FindPasswordByEmailFragment(), true);
                }
            });

            findPasswdByAuthInfo_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountActivity.navigationTo(new FindPasswordByAuthInfoFragment(), true);
                }
            });

            findPasswdByArtificial_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    accountActivity.navigationTo(new FindPasswordByArtificialFragment(), true);
                }
            });
        }

        showActionBar("找回密码");

        return view;
    }

}