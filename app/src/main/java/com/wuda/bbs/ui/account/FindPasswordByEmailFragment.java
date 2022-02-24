package com.wuda.bbs.ui.account;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.ui.base.BaseFragment;
import com.wuda.bbs.utils.validator.TextValidator;

import java.util.HashMap;
import java.util.Map;

public class FindPasswordByEmailFragment extends BaseFragment {

    private FindPasswordByEmailViewModel mViewModel;
    private TextInputLayout uid_tl;
    private TextInputEditText uid_et;
    private TextInputLayout email_tl;
    private TextInputEditText email_et;
    private Button submit_btn;

    public static FindPasswordByEmailFragment newInstance() {
        return new FindPasswordByEmailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_password_by_email_fragment, container, false);

        uid_tl = view.findViewById(R.id.findPasswdByEmail_uid_textInputLayout);
        uid_et = view.findViewById(R.id.findPasswdByEmail_uid_textInputEditText);
        email_tl = view.findViewById(R.id.findPasswdByEmail_email_textInputLayout);
        email_et = view.findViewById(R.id.findPasswdByEmail_email_textInputEditText);
        submit_btn = view.findViewById(R.id.findPasswdByArtificial_submit_button);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FindPasswordByEmailViewModel.class);

        eventBinding();

        showActionBar("通过邮箱找回密码");
    }

    private void eventBinding() {

        mViewModel.getBaseResponseLiveData().observe(getViewLifecycleOwner(), new Observer<BaseResponse>() {
            @Override
            public void onChanged(BaseResponse baseResponse) {
                new AlertDialog.Builder(getContext())
                        .setMessage(baseResponse.getMassage())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (baseResponse.isSuccessful()) {
                                    if (getActivity() != null)
                                        getActivity().onBackPressed();
                                }
                            }
                        })
                        .create()
                        .show();
            }
        });

        uid_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (uid_et.getText()!=null && TextValidator.isUidValid(uid_et.getText().toString())) {
                    uid_tl.setError(null);
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

                Editable email = email_et.getText();
                if (email==null || !TextValidator.isEmailValid(email.toString())) {
                    email_tl.setError("请填写有效的邮箱地址");
                    return;
                }

                // tgt=emailpwd&uid=abc&email=abc@abc.abc
                Map<String, String> form = new HashMap<>();

                form.put("tgt", "emailpwd");
                form.put("uid", uid_et.getText().toString());
                form.put("email", email_et.getText().toString());

                mViewModel.findPassword(form);
            }
        });
    }

}