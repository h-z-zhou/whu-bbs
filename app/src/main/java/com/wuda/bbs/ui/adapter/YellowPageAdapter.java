package com.wuda.bbs.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.logic.bean.campus.BookItemBean;
import com.wuda.bbs.logic.bean.campus.ContactBean;

import java.util.List;


public class YellowPageAdapter extends RecyclerView.Adapter<YellowPageAdapter.ViewHolder> {

    Context mContext;
    List<ContactBean> mContactList;

    private AdapterItemListener<ContactBean> mAdapterItemListener;

    public void setAdapterItemListener(AdapterItemListener<ContactBean> mAdapterItemListener) {
        this.mAdapterItemListener = mAdapterItemListener;
    }

    public YellowPageAdapter(Context mContext, List<ContactBean> mContactList) {
        this.mContext = mContext;
        this.mContactList = mContactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView name_tv = new TextView(parent.getContext());
        name_tv.setTextSize(18);
        name_tv.setPadding(32, 32, 32, 32);
        name_tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ViewHolder holder = new ViewHolder(name_tv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapterItemListener != null) {
                    int pos = holder.getAbsoluteAdapterPosition();
                    mAdapterItemListener.onItemClick(mContactList.get(pos), pos);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactBean contact = mContactList.get(position);
        holder.name_tv.setText(contact.getName());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(mContext, SubToolActivity.class);
//////                Intent intent = new Intent(context, InfoDetailActivity.class);
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("subTool", contact);
////                intent.putExtras(bundle);
////                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv = (TextView) itemView;
        }
    }
}
