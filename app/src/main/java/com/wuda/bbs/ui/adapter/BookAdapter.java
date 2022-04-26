package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.BookItemBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter {

    Context context;
    List<BookItemBean> bookList;

    private AdapterItemListener<BookItemBean> mAdapterItemListener;

    public void setAdapterItemListener(AdapterItemListener<BookItemBean> mAdapterItemListener) {
        this.mAdapterItemListener = mAdapterItemListener;
    }

//    private final int TYPE_ITEM = 0;//正常的Item
//    private final int TYPE_FOOT = 1;//尾部刷新

    public BookAdapter(Context context, List<BookItemBean> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == TYPE_ITEM)
            RecyclerView.ViewHolder holder = new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mAdapterItemListener != null) {
                        int pos = holder.getAbsoluteAdapterPosition();
                        mAdapterItemListener.onItemClick(bookList.get(pos), pos);
                    }
                }
            });

            return holder;
//        else
//            return new FootHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (holder instanceof ItemHolder) {
            BookItemBean book = bookList.get(position);
            ((ItemHolder) holder).title_tv.setText(book.getName());
            ((ItemHolder) holder).info_tv.setText(book.getAuthor() + "\n" + book.getPublisher());
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, SubToolActivity.class);
////                Intent intent = new Intent(context, InfoDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("subTool", bookList.get(position));
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
//                }
//            });
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == getItemCount()) {
//            return TYPE_FOOT;
//        } else {
//            return TYPE_ITEM;
//        }
//    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView title_tv;
        TextView info_tv;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.item_book_title_textView);
            info_tv = itemView.findViewById(R.id.item_book_info_textView);
        }
    }
//
//    public static class FootHolder extends RecyclerView.ViewHolder {
//
//        public FootHolder(@NonNull View itemView) {
//            super(itemView);
//        }
//    }
}
