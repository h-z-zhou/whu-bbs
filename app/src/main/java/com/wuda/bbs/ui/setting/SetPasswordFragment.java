package com.wuda.bbs.ui.setting;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.PrimaryKey;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.BaseResponse;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.parser.HtmlParser;
import com.wuda.bbs.utils.validator.TextValidator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetPasswordFragment extends Fragment {

    private TextInputEditText oldPassword_et;
    private TextInputLayout oldPassword_tl;
    private TextInputEditText new1Password_et;
    private TextInputLayout new1Password_tl;
    private TextInputEditText new2Password_et;
    private TextInputLayout new2Password_tl;
    private Button submit_btn;

    public static SetPasswordFragment newInstance() {
        return new SetPasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_password_fragment, container, false);

        oldPassword_et = view.findViewById(R.id.setPassword_old_editText);
        oldPassword_tl = view.findViewById(R.id.setPassword_old_textInputLayout);
        new1Password_et = view.findViewById(R.id.setPassword_new1_editText);
        new1Password_tl = view.findViewById(R.id.setPassword_new1_textInputLayout);
        new2Password_et = view.findViewById(R.id.setPassword_new2_editText);
        new2Password_tl = view.findViewById(R.id.setPassword_new2_textInputLayout);
        submit_btn = view.findViewById(R.id.setPassword_submit_button);

        eventBinding();

        return view;
    }

    private void eventBinding() {
        oldPassword_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (oldPassword_et.getText()!=null && TextValidator.isPasswordValid(oldPassword_et.getText().toString())) {
                    oldPassword_tl.setError(null);
                }
                return false;
            }
        });

        new1Password_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (new1Password_et.getText()!=null && TextValidator.isPasswordValid(new1Password_et.getText().toString())) {
                    new1Password_tl.setError(null);
                }
                return false;
            }
        });

        new2Password_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Editable new1Password = new1Password_et.getText();
                Editable new2Password = new2Password_et.getText();
                if (new1Password!=null && new2Password!=null) {
                    if (new1Password.toString().equals(new2Password.toString())) {
                        new2Password_tl.setError(null);
                    }
                }

                return false;
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable oldPassword = oldPassword_et.getText();
                if (oldPassword==null || !TextValidator.isPasswordValid(oldPassword.toString())) {
                    oldPassword_tl.setError("无效密码");
                    return;
                }

                Editable new1Password = new1Password_et.getText();
                if (new1Password==null || !TextValidator.isPasswordValid(new1Password.toString())) {
                    new1Password_tl.setError("无效密码");
                    return;
                }

                Editable new2Password = new2Password_et.getText();
                if (new2Password==null || !TextValidator.isPasswordValid(new1Password.toString())) {
                    new2Password_tl.setError("无效密码");
                    return;
                }
                if (!new1Password.toString().equals(new2Password.toString())) {
                    new2Password_tl.setError("密码不匹配");
                    return;
                }

                Map<String, String> form = new HashMap<>();
                // pw1=&pw2=&pw3=
                form.put("pw1", oldPassword.toString());
                form.put("pw2", new1Password.toString());
                form.put("pw3", new2Password.toString());

                submit(form);
            }
        });

    }

    private void submit(Map<String, String> form) {

        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("do", "");

        RootService rootService = ServiceCreator.create(RootService.class);
        rootService.post("bbspwd.php", queryMap, form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                    BaseResponse baseResponse = HtmlParser.parseSetPasswordResponse(text);
                    if (getActivity() != null && getContext() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getContext())
                                        .setMessage(baseResponse.getMassage())
                                        .setPositiveButton("确定", null)
                                        .create()
                                        .show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SetPasswordViewModel mViewModel = new ViewModelProvider(this).get(SetPasswordViewModel.class);
    }
}