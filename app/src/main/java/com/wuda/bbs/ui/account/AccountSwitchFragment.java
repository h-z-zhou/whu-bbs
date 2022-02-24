package com.wuda.bbs.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.ui.adapter.AccountRecyclerAdapter;
import com.wuda.bbs.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class AccountSwitchFragment extends BaseFragment {

    private AccountSharedViewModel mSharedViewModel;

    RecyclerView account_rv;
    AccountRecyclerAdapter adapter;

    public static AccountSwitchFragment newInstance() {
        return new AccountSwitchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_switch_fragment, container, false);

        account_rv = view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSharedViewModel = new ViewModelProvider(getActivity()).get(AccountSharedViewModel.class);

        account_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Account> accountList;
        if (mSharedViewModel.getAllAccounts().getValue() != null) {
            accountList = mSharedViewModel.getAllAccounts().getValue();
        } else {
            accountList = new ArrayList<>();
        }
        adapter = new AccountRecyclerAdapter(getContext(), accountList);
        adapter.setAccountHelper(new AccountRecyclerAdapter.AccountHelper() {
            @Override
            public void onLogin(Account account) {
                mSharedViewModel.login(account);

            }

            @Override
            public void onRemove(Account account) {

            }
        });

        account_rv.setAdapter(adapter);

        showActionBar("切换帐号");

        mSharedViewModel.getAllAccounts().observe(getViewLifecycleOwner(), new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accountList) {
                adapter.updateAccounts(accountList);
            }
        });
    }

}