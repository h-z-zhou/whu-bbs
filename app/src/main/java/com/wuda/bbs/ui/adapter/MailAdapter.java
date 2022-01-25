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
import com.wuda.bbs.bean.Mail;
import com.wuda.bbs.ui.main.mail.MailContentActivity;

import java.util.List;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.ViewHolder> {

    Context mContext;
    List<Mail> mMailList;
    Intent mailContentIntent;

    public MailAdapter(Context mContext, List<Mail> mMailList) {
        this.mContext = mContext;
        this.mMailList = mMailList;
        mailContentIntent = new Intent(this.mContext, MailContentActivity.class);
        mailContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mail_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mail mail = mMailList.get(position);
        holder.subject_tv.setText(mail.getSubject());
        holder.info_tv.setText(mail.getSender() + " | " + mail.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mailContentIntent.putExtra("mail", mail);
                mContext.startActivity(mailContentIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMailList.size();
    }

    public void appendMails(List<Mail> mails) {
        mMailList.addAll(mails);
        this.notifyItemRangeInserted(mMailList.size()-mails.size()-1, mMailList.size());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView subject_tv;
        TextView info_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subject_tv = itemView.findViewById(R.id.mail_subject_textView);
            info_tv = itemView.findViewById(R.id.mail_info_textView);
        }
    }
}
