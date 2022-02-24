package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.ui.account.AccountActivity;
import com.wuda.bbs.ui.account.LoginFragment;
import com.wuda.bbs.utils.network.NetConst;

import java.util.List;

public class AccountRecyclerAdapter extends RecyclerView.Adapter<AccountRecyclerAdapter.ViewHolder> {

    Context mContext;
    List<Account> mAccountList;
    AccountHelper mAccountHelper;

    public AccountRecyclerAdapter(Context mContext, List<Account> mAccountList) {
        this.mContext = mContext;
        this.mAccountList = mAccountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == mAccountList.size()) {
            // last one => add account
            holder.avatar_iv.setImageDrawable(mContext.getDrawable(R.drawable.ic_add));
            holder.id_tv.setText("添加新帐号");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof AccountActivity) {
                        ((AccountActivity) mContext).navigationTo(new LoginFragment(), true);
                    }
                }
            });
            return;
        }

        Account account = mAccountList.get(position);
        Glide.with(mContext).load(NetConst.BASE + account.getAvatar()).into(holder.avatar_iv);
        holder.id_tv.setText(account.getId());
        if(account.getFlag() == Account.FLAG_CURRENT) {
            holder.id_tv.append("(当前帐号)");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getFlag() != Account.FLAG_CURRENT) {
                    if (mAccountHelper != null) {
                        mAccountHelper.onLogin(account);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAccountList.size() + 1;
    }

    public void updateAccounts(List<Account> accountList) {
        this.mAccountList = accountList;
        this.notifyDataSetChanged();
    }

    public void setAccountHelper(AccountHelper mAccountHelper) {
        this.mAccountHelper = mAccountHelper;
    }

    public interface AccountHelper {
        public void onLogin(Account account);
        public void onRemove(Account account);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar_iv;
        TextView id_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_iv = itemView.findViewById(R.id.account_avatar_imageView);
            id_tv = itemView.findViewById(R.id.account_id_textView);
        }
    }
}
