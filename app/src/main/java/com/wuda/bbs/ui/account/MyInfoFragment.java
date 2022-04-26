package com.wuda.bbs.ui.account;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseFragment;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.network.NetConst;

public class MyInfoFragment extends BaseFragment {

//    private MyInfoViewModel mViewModel;
    private AccountSharedViewModel mSharedViewModel;

    private ImageView photo_iv;
    private ImageView avatar_iv;
    private TextView name_tv;
    private TextView level_tv;
    private TextView experience_tv;
    private TextView postNum_tv;
    private TextView signature_tv;

    public static MyInfoFragment newInstance() {
        return new MyInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_info_fragment, container, false);

        photo_iv = view.findViewById(R.id.userInfo_photo_imageView);
        avatar_iv = view.findViewById(R.id.userInfo_avatar_imageView);
        name_tv = view.findViewById(R.id.userInfo_name_textView);
        level_tv = view.findViewById(R.id.userInfo_level_textView);
        experience_tv = view.findViewById(R.id.userInfo_exp_textView);
        postNum_tv = view.findViewById(R.id.userInfo_postNum_textView);
        signature_tv = view.findViewById(R.id.userInfo_signature_textView);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSharedViewModel = new ViewModelProvider(requireActivity()).get(AccountSharedViewModel.class);

        mSharedViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserInfo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(UserInfo userInfo) {
                if (getContext() != null) {
                    if (TextUtils.isEmpty(userInfo.getPhoto())) {
                        Glide.with(getContext()).load(R.drawable.ic_login_bg).into(photo_iv);
                        photo_iv.setColorFilter(Color.LTGRAY);
                    } else {
                        Glide.with(getContext()).load(userInfo.getPhoto()).into(photo_iv);
                    }

                    Glide.with(getContext()).load(NetConst.BASE + userInfo.getAvatar()).into(avatar_iv);
                    name_tv.setText(userInfo.getId() + "(" + userInfo.getNickname() + ")" + "\n" + userInfo.getGender());
                    level_tv.append(userInfo.getLevel());
                    experience_tv.append(userInfo.getExp());
                    postNum_tv.append(userInfo.getPostNum());
                    signature_tv.append(Html.fromHtml(userInfo.getSignature()));
                }
            }
        });

        mSharedViewModel.getErrorResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(getContext())
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mSharedViewModel.requestUserInfo();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_info) {
            ((AccountActivity) requireActivity()).navigationTo(new MyInfoSettingFragment(), true);
        }
        return super.onOptionsItemSelected(item);
    }
}