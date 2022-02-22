package com.wuda.bbs.ui.account;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.utils.validator.TextValidator;

import java.util.HashMap;
import java.util.Map;

public class FindPasswordByAuthInfoFragment extends FindPasswordFragment {

    private FindPasswordByAuthInfoViewModel mViewModel;

    private TextInputEditText uid_et;
    private TextInputLayout uid_tl;
    private TextInputEditText idNumber_et;
    private TextInputLayout idNumber_tl;
    private TextInputEditText studentNumber_et;
    private TextInputLayout studentNumber_tl;
    private TextInputEditText email_et;
    private TextInputLayout email_tl;
    Button submit_btn;

    public static FindPasswordByAuthInfoFragment newInstance() {
        return new FindPasswordByAuthInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_password_by_auth_info_fragment, container, false);

        uid_et = view.findViewById(R.id.findPasswordByAuthInfo_uid_inputEditText);
        uid_tl = view.findViewById(R.id.findPasswordByAuthInfo_uid_textInputLayout);
        idNumber_et = view.findViewById(R.id.findPasswordByAuthInfo_idNumber_inputEditText);
        idNumber_tl = view.findViewById(R.id.findPasswordByAuthInfo_idNumber_textInputLayout);
        studentNumber_et = view.findViewById(R.id.findPasswordByAuthInfo_studentNumber_inputEditText);
        studentNumber_tl = view.findViewById(R.id.findPasswordByAuthInfo_studentNumber_textInputLayout);
        email_et = view.findViewById(R.id.findPasswordByAuthInfo_email_inputEditText);
        email_tl = view.findViewById(R.id.findPasswordByAuthInfo_email_textInputLayout);
        submit_btn = view.findViewById(R.id.findPasswdByArtificial_submit_button);

        eventBinding();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FindPasswordByAuthInfoViewModel.class);

        showActionBar("通过认证信息找回密码");
    }

    private void eventBinding() {
        uid_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (uid_et.getText()!=null && TextValidator.isUidValid(uid_et.getText().toString())) {
                    uid_tl.setError(null);
                }
                return false;
            }
        });

        idNumber_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (idNumber_et.getText()!=null && TextValidator.isIdNumberValid(idNumber_et.getText().toString())) {
                    idNumber_tl.setError(null);
                }
                return false;
            }
        });

        email_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (email_et.getText()!=null && TextValidator.isEmailValid(email_et.getText().toString())) {
                    email_tl.setError(null);
                }
                return false;
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable uid = uid_et.getText();
                if (uid==null || uid.length()<2) {
                    uid_tl.setError("请填写正确的ID");
                    return;
                }

                Editable idNumber = idNumber_et.getText();
                if (idNumber==null || !TextValidator.isIdNumberValid(idNumber.toString())) {
                    idNumber_tl.setError("请填写有效的身份证号");
                    return;
                }

                Editable studentNumber = studentNumber_et.getText();
                if (studentNumber==null || studentNumber.length()!=13) {
                    studentNumber_tl.setError("请填写有效的学号");
                    return;
                }

                Editable email = email_et.getText();
                if (email==null || !TextValidator.isEmailValid(email.toString())) {
                    email_tl.setError("请填写有效的邮箱地址");
                    return;
                }

                // tgt=authpwd&uid=abc&name=&sfzh=123456789009876543&xh=123456789012&email=abc@abc.abc
                Map<String, String> form = new HashMap<>();

                form.put("tgt", "authpwd");
                form.put("uid", uid.toString());
                form.put("name", "");
                form.put("sfzh", idNumber.toString());
                form.put("xh", studentNumber.toString());
                form.put("email", email.toString());

                submit(form);
            }
        });
    }

}