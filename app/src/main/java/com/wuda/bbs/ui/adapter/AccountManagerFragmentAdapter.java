package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.FragmentEntry;
import com.wuda.bbs.ui.account.AccountActivity;
import com.wuda.bbs.ui.account.MyInfoFragment;
import com.wuda.bbs.ui.account.SetParamFragment;
import com.wuda.bbs.ui.account.SetPasswordFragment;

public class AccountManagerFragmentAdapter extends RecyclerView.Adapter<AccountManagerFragmentAdapter.ViewHolder> {

    AccountActivity mAccountActivity;
    final FragmentEntry[] mFragmentEntries = new FragmentEntry[] {
            new FragmentEntry(MyInfoFragment.class, "个人主页", R.drawable.ic_home, R.color.QingHaKeZi, ""),
            new FragmentEntry(SetParamFragment.class, "用户参数", R.drawable.ic_information, R.color.DanCuiLv,""),
            new FragmentEntry(SetPasswordFragment.class, "修改密码", R.drawable.ic_lock_reset, R.color.LangHuaLv, "")
    };

    public AccountManagerFragmentAdapter(AccountActivity mAccountActivity) {
        this.mAccountActivity = mAccountActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_entrance_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentEntry fragmentEntry = mFragmentEntries[holder.getAdapterPosition()];
                try {
                    mAccountActivity.navigationTo((Fragment) fragmentEntry.getClz().newInstance(), true);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FragmentEntry fragmentEntry = mFragmentEntries[position];
        @SuppressLint("UseCompatLoadingForDrawables") Drawable icon = mAccountActivity.getDrawable(fragmentEntry.getIconId());
        holder.icon_iv.setImageDrawable(icon);
        holder.icon_iv.setColorFilter(fragmentEntry.getIconColor());
        holder.name_tv.setText(fragmentEntry.getName());
    }

    @Override
    public int getItemCount() {
        return mFragmentEntries.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon_iv;
        TextView name_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_iv = itemView.findViewById(R.id.fragment_icon_imageView);
            name_tv = itemView.findViewById(R.id.fragment_name_textView);
        }
    }
}
