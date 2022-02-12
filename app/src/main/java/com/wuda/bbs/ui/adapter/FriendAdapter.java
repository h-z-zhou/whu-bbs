package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.Friend;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.utils.network.NetConst;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    Context mContext;
    List<Friend> mFriendList;

    public FriendAdapter(Context mContext, List<Friend> mFriendList) {
        this.mContext = mContext;
        this.mFriendList = mFriendList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Friend friend = mFriendList.get(position);

        Glide.with(mContext).load(NetConst.BASE + friend.getAvatar()).error(R.drawable.ic_face).into(holder.avatar_iv);
        holder.id_tv.setText(friend.getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("userId", friend.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFriendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView avatar_iv;
        TextView id_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_iv = itemView.findViewById(R.id.friend_avatar_imageView);
            id_tv = itemView.findViewById(R.id.friend_id_textView);
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateFriendList(List<Friend> friends) {
        mFriendList = friends;
        this.notifyDataSetChanged();
    }
}
