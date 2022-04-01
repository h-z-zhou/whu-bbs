package com.wuda.bbs.ui.account;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseFragment;
import com.wuda.bbs.ui.widget.CustomDialog;
import com.wuda.bbs.utils.GlideEngine;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyInfoSettingFragment extends BaseFragment {

    private MyInfoSettingViewModel mViewModel;
    private AccountSharedViewModel mSharedViewModel;

    // CLicked Listener
    private LinearLayout avatar_ll;
    private LinearLayout nickname_ll;
    private LinearLayout gender_ll;
    private LinearLayout birthday_ll;
    private LinearLayout signature_ll;
    // Content
    private ImageView avatar_iv;
    private TextView nickname_tv;
    private TextView gender_tv;
    private TextView birthday_tv;
    private TextView signature_tv;

    public static MyInfoSettingFragment newInstance() {
        return new MyInfoSettingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_info_setting_fragment, container, false);

        avatar_ll = view.findViewById(R.id.infoSetting_avatar_linearLayout);
        nickname_ll = view.findViewById(R.id.infoSetting_nickname_linearLayout);
        gender_ll = view.findViewById(R.id.infoSetting_gender_linearLayout);
        birthday_ll = view.findViewById(R.id.infoSetting_birthday_linearLayout);
        signature_ll = view.findViewById(R.id.infoSetting_signature_linearLayout);

        avatar_iv = view.findViewById(R.id.infoSetting_avatar_iv);
        nickname_tv = view.findViewById(R.id.infoSetting_name_textView);
        gender_tv = view.findViewById(R.id.infoSetting_gender_textView);
        birthday_tv = view.findViewById(R.id.infoSetting_birthday_textView);
        signature_tv = view.findViewById(R.id.infoSetting_signature_textView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showActionBar("个人信息");

        mViewModel = new ViewModelProvider(this).get(MyInfoSettingViewModel.class);
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(AccountSharedViewModel.class);

        UserInfo info = mSharedViewModel.getUserInfo().getValue();
        if (info != null) {
            Glide.with(requireContext()).load(NetConst.BASE + info.getAvatar()).into(avatar_iv);
            nickname_tv.setText(info.getNickname());
            gender_tv.setText(info.getGender());
            signature_tv.setText(info.getSignature());
        }

        mViewModel.requestMyUserInfo();

        eventBinding(info);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            if (data == null) return;
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                mViewModel.uploadAvatar(resultUri.getPath());
            }
        }
    }

    private void eventBinding(UserInfo info) {

        mSharedViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo info) {
                Glide.with(requireContext()).load(NetConst.BASE + info.getAvatar()).into(avatar_iv);
                nickname_tv.setText(info.getNickname());
                gender_tv.setText(info.getGender());
                signature_tv.setText(info.getSignature());
            }
        });

        mViewModel.getSetInfoResultMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mSharedViewModel.requestUserInfo();
                }
            }
        });

        avatar_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PictureSelector.create(getContext())
                        .openGallery(SelectMimeType.ofImage())
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .setSelectionMode(SelectModeConfig.SINGLE)
                        .isDisplayCamera(false)
                        .isDirectReturnSingle(true)
                        .forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {
                                if (result.isEmpty())
                                    return;
                                String path = result.get(0).getPath();

                                Uri srcUri = Uri.fromFile(new File(path));
                                String dstPath = requireContext().getCacheDir() + "/cropImage_" + result.get(0).getFileName();
                                Uri dstUri = Uri.fromFile(new File(dstPath));

                                UCrop.of(srcUri, dstUri)
                                        .withAspectRatio(1, 1)
                                        .withMaxResultSize(120, 120)
                                        .start(requireContext(), MyInfoSettingFragment.this);
                            }
                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        nickname_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_view, null, false);
                TextInputEditText content_et = view.findViewById(R.id.dialog_input_editText);

                new CustomDialog(getContext())
                        .setDialogTitle("请输入昵称")
                        .setCustomView(view)
                        .setOnPositiveButtonClickedListener("确定", new CustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                if (content_et.getText() != null) {
                                    String nickname = content_et.getText().toString();
                                    nickname_tv.setText(nickname);
                                    Map<String, String> form = new HashMap<>();
                                    form.put("nick", nickname);
                                    NetworkEntry.setNickname(form, new WebResultHandler() {
                                        @Override
                                        public void onResponseHandled(ContentResponse<WebResult> response) {

                                        }
                                    });
                                }
                            }
                        })
                        .show();
            }
        });

        gender_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup gender_rg = new RadioGroup(getContext());
                gender_rg.setOrientation(LinearLayout.HORIZONTAL);
                RadioButton boy_rb = new RadioButton(getContext());
                boy_rb.setText("男");
                boy_rb.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));
                RadioButton girl_rb = new RadioButton(getContext());
                girl_rb.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1));
                girl_rb.setText("女");
                gender_rg.addView(boy_rb);
                gender_rg.addView(girl_rb);
                if (mSharedViewModel.getUserInfo().getValue() == null) {
                    Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mSharedViewModel.getUserInfo().getValue().getGenderValue() == 1) {
                    boy_rb.setChecked(true);
                } else {
                    girl_rb.setChecked(true);
                }

                new CustomDialog(getContext())
                        .setDialogTitle("请选择性别")
                        .setCustomView(gender_rg)
                        .setOnPositiveButtonClickedListener("确定", new CustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {

                                String gender = boy_rb.isChecked()? "1": "2";
                                gender_tv.setText(boy_rb.isChecked()? "男": "女");

                                Map<String, String> form = mViewModel.form;
                                form.put("gender", gender);
                                mViewModel.setMyUserInfo(form);
                            }
                        })
                        .show();
            }
        });

        birthday_ll.setOnClickListener(new View.OnClickListener() {
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
                        birthday_tv.setText(date);
                    }
                });

                datePicker.show(getParentFragmentManager(), datePicker.toString());
            }
        });

        signature_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_view, null, false);
                TextInputEditText content_et = view.findViewById(R.id.dialog_input_editText);

                new CustomDialog(getContext())
                        .setDialogTitle("请输入个性签名")
                        .setCustomView(view)
                        .setOnPositiveButtonClickedListener("确定", new CustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                if (content_et.getText() != null) {
                                    String signature = content_et.getText().toString();
                                    signature_tv.setText(signature);

                                    Map<String, String> form = mViewModel.form;
                                    form.put("Signature", signature);
                                    mViewModel.setMyUserInfo(form);

                                }
                            }
                        })
                        .show();
            }
        });
    }
}