package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.ui.main.mail.MailContentActivity;

import java.util.ArrayList;
import java.util.List;

public class MailAdapter extends FooterAdapter<Mail> {

    String mBoxName;

    public MailAdapter(Context mContext, List<Mail> mContents, String mBoxName) {
        super(mContext, mContents);
        this.mBoxName = mBoxName;
    }

    @Override
    protected ContentViewHolder onCreateContentViewHolder(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_item, parent, false);

        MailViewHolder holder = new MailViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mail mail = mContents.get(holder.getAdapterPosition());

                Intent intent = new Intent(mContext, MailContentActivity.class);
                intent.putExtra("mail", mail);
                intent.putExtra("boxName", mBoxName);
                mContext.startActivity(intent);
            }
        });

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindContentViewHolder(ContentViewHolder contentHolder, Mail content) {
        if (contentHolder instanceof MailViewHolder) {
            MailViewHolder holder = (MailViewHolder) contentHolder;
            holder.subject_tv.setText(content.getSubject());
            holder.info_tv.setText(content.getSender() + " | " + content.getTime());
        }
    }

    public void changeBox(String boxName) {
        this.mBoxName = boxName;
        setContents(new ArrayList<>());
    }

    public void setBoxName(String boxName) {
        this.mBoxName = boxName;
    }

    static class MailViewHolder extends ContentViewHolder {

        TextView subject_tv;
        TextView info_tv;

        public MailViewHolder(@NonNull View itemView) {
            super(itemView);
            subject_tv = itemView.findViewById(R.id.mail_subject_textView);
            info_tv = itemView.findViewById(R.id.mail_info_textView);
        }
    }
}
