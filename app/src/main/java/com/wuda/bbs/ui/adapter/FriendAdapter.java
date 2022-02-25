package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.ui.user.UserInfoActivity;
import com.wuda.bbs.utils.network.NetConst;

import java.util.List;

public class FriendAdapter extends FooterAdapter<Friend> {

    public FriendAdapter(Context mContext, List<Friend> mContent) {
        super(mContext, mContent);
    }

    @Override
    protected ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        FriendViewHolder holder = new FriendAdapter.FriendViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Friend friend = mContents.get(position);
                Intent intent = new Intent(mContext, UserInfoActivity.class);
                intent.putExtra("userId", friend.getId());
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    protected void onBindContentViewHolder(ContentViewHolder holder, Friend content) {

        if (holder instanceof FriendViewHolder) {
            Glide.with(mContext).load(NetConst.BASE + content.getAvatar()).error(R.drawable.ic_face).into(((FriendViewHolder) holder).avatar_iv);
            ((FriendViewHolder) holder).id_tv.setText(content.getId());
        }

    }


    static class FriendViewHolder extends FooterAdapter.ContentViewHolder {

        ImageView avatar_iv;
        TextView id_tv;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_iv = itemView.findViewById(R.id.friend_avatar_imageView);
            id_tv = itemView.findViewById(R.id.friend_id_textView);
        }
    }
}
