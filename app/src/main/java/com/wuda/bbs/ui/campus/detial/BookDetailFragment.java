package com.wuda.bbs.ui.campus.detial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.BookInfoBean;
import com.wuda.bbs.logic.bean.campus.BookItemBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.utils.campus.HttpUtil;
import com.wuda.bbs.utils.campus.LibBookSearchResponseParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class BookDetailFragment extends ToolFragment {

    BookItemBean bookItem;
    BookInfoBean bookInfo;

    TextView title_tv;
    TextView author_tv;
    TextView keyWord_tv;
    TextView publisher_tv;
    TextView ISBN_tv;
    TextView digest_tv;
    TextView collectionLabel_tv;
    TableLayout collectionInfo_table;

    public static BookDetailFragment newInstance(ToolBean bookItem) {
        BookDetailFragment fragment = new BookDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("bookItem", bookItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookItem = (BookItemBean) getArguments().getSerializable("bookItem");
            tool = bookItem;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_detail, container, false);

        title_tv = view.findViewById(R.id.book_title_textView);
        author_tv = view.findViewById(R.id.book_author_textView);
        keyWord_tv = view.findViewById(R.id.book_keyWord_textView);
        publisher_tv = view.findViewById(R.id.book_publisher_textView);
        ISBN_tv = view.findViewById(R.id.book_ISBN_textView);
        digest_tv = view.findViewById(R.id.book_digest_textView);
        collectionLabel_tv = view.findViewById(R.id.book_collectionLabel_textView);
        collectionInfo_table = view.findViewById(R.id.book_collectionInfo_table);

        getBookInfo();

        return view;
    }


    private void getBookInfo() {
        HttpUtil.sendOkHttpRequest(bookItem.getUrl(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                bookInfo = LibBookSearchResponseParser.handleBookInfoResponse(response.body().string());

                if (getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void run() {
//                        ((SubToolActivity) getActivity()).closeProgressBar();

                        title_tv.setText(labelSSBuilder("题名", bookInfo.getTitle()));
                        author_tv.setText(labelSSBuilder("作者", bookInfo.getAuthor()));
                        keyWord_tv.setText(labelSSBuilder("主题词", bookInfo.getKeyWord()));
                        publisher_tv.setText(labelSSBuilder("出版发行", bookInfo.getPublisher()));
                        ISBN_tv.setText(labelSSBuilder("ISBN", bookInfo.getISBN()));
                        digest_tv.setText(labelSSBuilder("摘要", bookInfo.getDigest()));
                        collectionLabel_tv.setText(labelSSBuilder("全馆馆藏", ""));

                        collectionInfo_table.setDividerDrawable(getResources().getDrawable(R.drawable.table_h_divider));
                        collectionInfo_table.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_END);

                        List<BookInfoBean.CollectionInfo> collectionInfoList = bookInfo.getCollectionInfoList();
                        for (int i=0; i<collectionInfoList.size(); ++i) {
                            for (TableRow row: initTableRowList(collectionInfoList.get(i))) {
                                collectionInfo_table.addView(row);
                            }
                            if (i != collectionInfoList.size()-1) {
                                TableRow splitRow = new TableRow(getContext());
                                splitRow.addView(new TextView(getContext()));
                                collectionInfo_table.addView(splitRow);
                            }
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private List<TableRow> initTableRowList(BookInfoBean.CollectionInfo collectionInfo) {

        List<TableRow> tableRowList = new ArrayList<>();

        Context mContext = requireContext();
        String[] header = {"单册状态", "应还日期", "分馆", "架位", "请求数", "条码"};
        String[] body = {
                collectionInfo.getStatus(), collectionInfo.getReturnDate(),
                collectionInfo.getBranch(), collectionInfo.getShelfId(),
                collectionInfo.getRequestNum(), collectionInfo.getBarCode()
        };

        TableRow.LayoutParams headerParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        headerParams.setMarginStart(8);
        TableRow.LayoutParams bodyParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        bodyParams.setMarginStart(8);

        for (int i=0; i<header.length; ++i) {
            TableRow row = new TableRow(mContext);
            row.setDividerDrawable(getResources().getDrawable(R.drawable.table_v_divider));
            row.setShowDividers(LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_MIDDLE | LinearLayout.SHOW_DIVIDER_END);

            TextView header_tv = new TextView(mContext);
            header_tv.setTextSize(16);
            header_tv.setText(header[i]);
            header_tv.setLayoutParams(headerParams);
            TextView body_tv = new TextView(mContext);
            body_tv.setTextSize(16);
            body_tv.setLayoutParams(bodyParams);
            body_tv.setText(body[i]);

            row.addView(header_tv);
            row.addView(body_tv);
            tableRowList.add(row);
        }

        return tableRowList;
    }

    private SpannableStringBuilder labelSSBuilder(String label, String content) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(label);
        builder.append(" : ");
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(content);
        return builder;
    }
}