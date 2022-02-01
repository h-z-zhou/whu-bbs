package com.wuda.bbs.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.wuda.bbs.R;

public class ForgotPasswordFragment extends Fragment {

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

        findPasswdByEmail_btn = view.findViewById(R.id.forgotPasswd_findByEmail_button);
        findPasswdByAuthInfo_btn = view.findViewById(R.id.forgotPasswd_findByAuthInfo_button);
        findPasswdByArtificial_btn = view.findViewById(R.id.forgotPasswd_findByArtificial_button);

        LoginActivity loginActivity = (LoginActivity) getActivity();

        if (loginActivity != null) {

            findPasswdByEmail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginActivity.navigationTo(new FindPasswordByEmailFragment(), true);
                }
            });

            findPasswdByAuthInfo_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginActivity.navigationTo(new FindPasswordByAuthInfoFragment(), true);
                }
            });

            findPasswdByArtificial_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginActivity.navigationTo(new FindPasswordByArtificialFragment(), true);
                }
            });
        }

        return view;
    }

}