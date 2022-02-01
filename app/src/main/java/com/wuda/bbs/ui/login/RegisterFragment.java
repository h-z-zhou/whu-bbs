package com.wuda.bbs.ui.login;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.BaseResponse;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.parser.JsonParser;
import com.wuda.bbs.utils.validator.TextValidator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private TextInputEditText uid_et;
    private TextInputLayout uid_tl;
    private TextInputEditText password_et;
    private TextInputLayout password_tl;
    private TextInputEditText password2_et;
    private TextInputLayout password2_tl;
    private TextInputEditText nickname_et;
    private TextInputLayout nickname_tl;
    private TextInputEditText realName_et;
    private TextInputLayout realName_tl;
    private TextInputEditText campusId_et;
    private TextInputLayout campusId_tl;
    private TextInputEditText idNumber_et;
    private TextInputLayout idNumber_tl;
    private TextInputEditText birthday_et;
    private TextInputLayout birthday_tl;
    private TextInputEditText email_et;
    private TextInputLayout email_tl;
    private TextInputEditText phoneNumber_et;
    private TextInputLayout phoneNumber_tl;
    private RadioButton gender_man_btn;
    private RadioButton gender_woman_btn;
    private Button submit_btn;

    private RegisterViewModel mViewModel;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);

        uid_et = view.findViewById(R.id.register_uid_inputTextEdit);
        uid_tl = view.findViewById(R.id.register_uid_textInputLayout);
        password_et = view.findViewById(R.id.register_password_inputTextEdit);
        password_tl = view.findViewById(R.id.register_password_textInputLayout);
        password2_et = view.findViewById(R.id.register_password2_inputTextEdit);
        password2_tl = view.findViewById(R.id.register_password2_textInputLayout);
        nickname_et = view.findViewById(R.id.register_nickname_inputTextEdit);
        nickname_tl = view.findViewById(R.id.register_nickname_textInputLayout);
        realName_et =view.findViewById(R.id.register_realName_inputTextEdit);
        realName_tl = view.findViewById(R.id.register_realName_textInputLayout);
        campusId_et = view.findViewById(R.id.register_campusId_inputTextEdit);
        campusId_tl = view.findViewById(R.id.register_campusId_textInputLayout);
        idNumber_et = view.findViewById(R.id.register_idNumber_inputTextEdit);
        idNumber_tl = view.findViewById(R.id.register_idNumber_textInputLayout);
        birthday_et = view.findViewById(R.id.register_birthday_inputTextEdit);
        birthday_tl = view.findViewById(R.id.register_birthday_textInputLayout);
        email_et = view.findViewById(R.id.register_email_inputTextEdit);
        email_tl = view.findViewById(R.id.register_email_textInputLayout);
        phoneNumber_et = view.findViewById(R.id.register_phoneNumber_inputTextEdit);
        phoneNumber_tl = view.findViewById(R.id.register_phoneNumber_textInputLayout);
        gender_man_btn = view.findViewById(R.id.register_gender_man_btn);
        gender_woman_btn = view.findViewById(R.id.register_gender_woman_btn);
        submit_btn = view.findViewById(R.id.register_submit_btn);

        eventBinding();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
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

        password_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (password_et.getText()!=null && TextValidator.isPasswordValid(password_et.getText().toString())) {
                    password_tl.setError(null);
                }
                return false;
            }
        });

        password2_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Editable password = password_et.getText();
                Editable password2 = password2_et.getText();
                if (password!=null && password2!=null && password.toString().equals(password2.toString())) {
                    password2_tl.setError(null);
                }
                return false;
            }
        });

        campusId_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (campusId_et.getText()!=null && TextValidator.isCampusIdValid(campusId_et.getText().toString())) {
                    campusId_tl.setError(null);
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

        birthday_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();

                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String date = sdf.format(selection);
                        birthday_et.setText(date);
                    }
                });

                datePicker.show(getParentFragmentManager(), datePicker.toString());
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

        phoneNumber_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (phoneNumber_et.getText()!=null && TextValidator.isPhoneNumberValid(phoneNumber_et.getText().toString())) {
                    phoneNumber_tl.setError(null);
                }
                return false;
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable uid = uid_et.getText();
                if (uid==null || !TextValidator.isUidValid(uid.toString())) {
                    uid_tl.setError("请填写符合要求的ID");
                    return;
                }

                Editable password = password_et.getText();
                if (password==null || !TextValidator.isPasswordValid(password.toString())) {
                    password_tl.setError("请填写符合要求的密码");
                    return;
                }

                Editable password2 = password2_et.getText();
                if (password2==null || !password2.toString().equals(password.toString())) {
                    password2_tl.setError("两次密码不匹配");
                    return;
                }

                Editable nickname = nickname_et.getText();

                Editable realName = realName_et.getText();
                if (realName==null || realName.length()<2) {
                    realName_tl.setError("请输入真实姓名");
                    return;
                }

                Editable campusId = campusId_et.getText();
                if (campusId==null || !TextValidator.isCampusIdValid(campusId.toString())) {
                    campusId_tl.setError("请输入有效的凭证");
                    return;
                }

                Editable idNumber = idNumber_et.getText();
                if (idNumber==null || !TextValidator.isIdNumberValid(idNumber.toString())) {
                    idNumber_tl.setError("请输入正确的身份证号");
                    return;
                }

                Editable email = email_et.getText();
                if (email==null || !TextValidator.isEmailValid(email.toString())) {
                    email_tl.setError("请输入有效邮箱");
                    return;
                }

                Editable phoneNumber = phoneNumber_et.getText();
                if (phoneNumber==null || !TextValidator.isPhoneNumberValid(phoneNumber.toString())) {
                    phoneNumber_tl.setError("请输入联系电话");
                    return;
                }

                String gender;
                if (gender_man_btn.isChecked()) {
                    gender = "男";
                } else {
                    gender = "女";
                }

                Editable birthday = birthday_et.getText();
                String birthday_year = "";
                String birthday_month = "";
                String birthday_day = "";
                if (birthday!=null) {
                    String[] date = birthday.toString().split("-");
                    if (date.length == 3) {
                        birthday_year = date[0];
                        birthday_month = date[1];
                        birthday_day = date[2];
                    }
                }

                // userid=abc&pass1=x57805zhz&pass2=x57805zhz&username=&realname=%D6%DC%BA%A3%D6%F9&xh=2020202020084&sfzh=450921199802240811&gender=%C4%D0&year=&month=&day=&reg_email=1758925946%40qq.com&phone=15927210933
                Map<String, String> form = new HashMap<>();
                form.put("userid", uid.toString());
                form.put("pass1", password.toString());
                form.put("pass2", password2.toString());
                form.put("username", nickname==null? "": nickname.toString());
                form.put("realName", realName.toString());
                form.put("xh", campusId.toString());
                form.put("sfzh", idNumber.toString());
                form.put("gender", gender);
                form.put("year", birthday_year);
                form.put("month", birthday_month);
                form.put("day", birthday_day);
                form.put("reg_email", email.toString());
                form.put("phone", phoneNumber.toString());

                summit(form);

            }
        });
    }

    private void summit(Map<String, String> form) {
        RootService rootService = ServiceCreator.create(RootService.class);
        rootService.post("bbsreg.php", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    ResponseBody body = response.body();
                    if (body == null)
                        return;

                    String text = new String(body.bytes(), "GBK");

                    Log.d("response", text);

//                    BaseResponse baseResponse = JsonParser.parseFindPasswordResponse(body.string());
//
//                    if (getActivity() != null && getContext() != null) {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                new AlertDialog.Builder(getContext())
//                                        .setMessage(baseResponse.getMassage())
//                                        .setPositiveButton("确定", null)
//                                        .create()
//                                        .show();
//                            }
//                        });
//                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }
}