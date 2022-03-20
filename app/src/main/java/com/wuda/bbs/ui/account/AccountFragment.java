package com.wuda.bbs.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.ui.adapter.AccountManagerFragmentAdapter;
import com.wuda.bbs.ui.base.BaseFragment;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

public class AccountFragment extends BaseFragment {

    private AccountSharedViewModel mSharedViewModel;

    AccountActivity accountActivity;

    ImageView currentAvatar_iv;
    RecyclerView manager_rv;
    Button switch_btn;
    Button logout_btn;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment, container, false);

        currentAvatar_iv = view.findViewById(R.id.account_currentAvatar_imageView);
        manager_rv = view.findViewById(R.id.account_manager_recyclerView);
        switch_btn = view.findViewById(R.id.account_switch_button);
        logout_btn = view.findViewById(R.id.account_logout_button);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSharedViewModel = new ViewModelProvider(requireActivity()).get(AccountSharedViewModel.class);

        manager_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        manager_rv.setAdapter(new AccountManagerFragmentAdapter(accountActivity));

        showActionBar("帐号管理");

        eventBinding();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        accountActivity = (AccountActivity) getActivity();
    }

    private void eventBinding() {

        mSharedViewModel.getCurrentAccount().observe(getViewLifecycleOwner(), new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if (account.getId().equals("guest")) {
                    accountActivity.onBackPressed();
                } else if (getContext() != null) {
                    Glide.with(getContext()).load(NetConst.BASE+account.getAvatar()).into(currentAvatar_iv);
                }
            }
        });

        switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountActivity.navigationTo(new AccountSwitchFragment(), true);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSharedViewModel.logout(new SimpleResponseHandler() {
                    @Override
                    public void onResponseHandled(ContentResponse<Object> response) {
                        if (response.getResultCode() == ResultCode.LOGIN_ERR) {
                            BBSApplication.setAccount(Account.GUEST);
                            // 不改变登录页面数据
//                            mSharedViewModel.updateCurrentAccount(Account.GUEST);
                            if (getActivity() !=null) {
                                getActivity().onBackPressed();
                            }
                        }
                    }
                });
            }
        });
    }
}