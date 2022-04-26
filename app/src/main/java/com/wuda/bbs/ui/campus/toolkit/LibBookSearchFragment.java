package com.wuda.bbs.ui.campus.toolkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.BookItemBean;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.BookAdapter;
import com.wuda.bbs.ui.campus.CampusActivity;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.ui.campus.detial.BookDetailFragment;
import com.wuda.bbs.utils.campus.HttpUtil;
import com.wuda.bbs.utils.campus.LibBookSearchResponseParser;
import com.wuda.bbs.utils.campus.ServerURL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LibBookSearchFragment extends ToolFragment {

    TextInputLayout kw_layout;
    TextInputEditText kw_et;
    RadioGroup filed_rg;
    RadioButton wti_btn;
    RadioButton tit_btn;
    RadioButton wau_btn;
    RadioButton wsu_btn;
    Button start_btn;
    RecyclerView result_rv;
    TextView amount_tv;

//    String[] filed_list = new String[]{"wti", "tit", "wau", "wsu"};
    Integer amount;

    String kw;
    String filed;
    Integer page = 0;
    Integer totalPage = 0;
    String queryOption;

    List<BookItemBean> bookList;
    BookAdapter bookAdapter;

    public LibBookSearchFragment() {
        bookList = new ArrayList<>();
    }

    public static LibBookSearchFragment newInstance(String param1, String param2) {
        LibBookSearchFragment fragment = new LibBookSearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lib_book_search, container, false);

        kw_layout = view.findViewById(R.id.lib_search_kw_textInputLayout);
        kw_et = view.findViewById(R.id.lib_search_kw_et);
        filed_rg = view.findViewById(R.id.lib_search_filed_radioGroup);
        wti_btn = view.findViewById(R.id.lib_search_filed_wti_btn);
        tit_btn = view.findViewById(R.id.lib_search_filed_tit_btn);
        wau_btn = view.findViewById(R.id.lib_search_filed_wau_btn);
        wsu_btn = view.findViewById(R.id.lib_search_filed_wsu_btn);
        start_btn = view.findViewById(R.id.lib_search_start_btn);
        amount_tv = view.findViewById(R.id.lib_search_amount_textView);
        result_rv = view.findViewById(R.id.lib_search_result_list);

        bookAdapter = new BookAdapter(requireContext(), bookList);
        bookAdapter.setAdapterItemListener(new AdapterItemListener<BookItemBean>() {
            @Override
            public void onItemClick(BookItemBean data, int position) {
                ((CampusActivity) requireActivity()).navigationTo(
                        BookDetailFragment.newInstance(data),
                        true
                );
            }

            @Override
            public void onItemLongClick(BookItemBean data, int position) {

            }
        });

        result_rv.setAdapter(bookAdapter);
        result_rv.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        eventBinding();

        return view;
    }

    private void eventBinding() {
        start_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {

                bookList.clear();
                bookAdapter.notifyDataSetChanged();
                amount_tv.setText("");

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(kw_et.getWindowToken(),0);
                }

                kw_layout.clearFocus();

                // 无关键词
                kw = kw_et.getText().toString();
                if (kw.equals("")) {
                    kw_layout.setError(getString(R.string.lib_search_no_kw));
                    return;
                } else {
                    kw_layout.setError(null);
                }

                if (wti_btn.isChecked()) {
                    filed = "wti";
                } else if (tit_btn.isChecked()) {
                    filed = "tit";
                } else if (wau_btn.isChecked()) {
                    filed = "wau";
                } else {
                    filed = "wsu";
                }
//                filed = filed_list[0];

                requestFromServer();
            }
        });

        result_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE && page < totalPage) {
                    loadMore();
                }
            }
        });
    }

    private void requestFromServer() {
        // first

        page = 1;
        queryOption = "?kw=" + kw + "&filed=" + filed + "&page=" + page.toString();

        showProgressBar();
        HttpUtil.sendOkHttpRequest(ServerURL.LIB_SEARCH + queryOption, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressBar();
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String, Object> result = LibBookSearchResponseParser.handleSearchResponse(response.body().string());
                amount = (Integer) result.get("amount");
                // 页数： 进一制
                totalPage = (amount + 9) / 10;
                List<BookItemBean> newBooks = (List<BookItemBean>) result.get("bookList");
                assert newBooks != null;
                bookList.addAll(newBooks);
                requireActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        closeProgressBar();
                        bookAdapter.notifyItemRangeInserted(bookList.size()-newBooks.size(), bookList.size());
                        amount_tv.setText(getString(R.string.lib_search_amount) + amount.toString());
                    }
                });
            }
        });

    }

    private void loadMore() {
        ++page;
        queryOption = "?kw=" + kw + "&filed=" + filed + "&page=" + page.toString();
        HttpUtil.sendOkHttpRequest(ServerURL.LIB_SEARCH + queryOption, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Map<String, Object> result = LibBookSearchResponseParser.handleSearchResponse(response.body().string());
                amount = (Integer) result.get("amount");
                List<BookItemBean> newBooks = (List<BookItemBean>) result.get("bookList");
                assert newBooks != null;
                bookList.addAll(newBooks);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bookAdapter.notifyItemRangeInserted(bookList.size()-newBooks.size(), bookList.size());
                    }
                });
            }
        });
    }
}