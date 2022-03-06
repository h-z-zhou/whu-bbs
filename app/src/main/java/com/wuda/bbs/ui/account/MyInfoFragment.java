package com.wuda.bbs.ui.account;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.text.Html;
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
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.ui.base.BaseFragment;
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
        mSharedViewModel = new ViewModelProvider(getActivity()).get(AccountSharedViewModel.class);

        mSharedViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserInfo>() {
            @Override
            public void onChanged(UserInfo userInfo) {
                if (getContext() != null) {
                    if (!userInfo.getPhoto().equals("")) {
                        Glide.with(getContext()).load(NetConst.BASE + userInfo.getPhoto()).into(photo_iv);
                    } else {
                        Glide.with(getContext()).load(R.drawable.ic_login_bg).into(photo_iv);
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

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my_info_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_info) {
            ((AccountActivity) getActivity()).navigationTo(new MyInfoSettingFragment(), true);
        }
        return super.onOptionsItemSelected(item);
    }
}