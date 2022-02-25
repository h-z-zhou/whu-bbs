package com.wuda.bbs.ui.main.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.response.BriefArticleResponse;
import com.wuda.bbs.ui.adapter.BriefArticleAdapter;

import java.util.ArrayList;

public abstract class ArticleContainerFragment extends Fragment {

    protected ArticleContainerViewModel mViewModel;
    protected FrameLayout article_root_fl;
    protected SwipeRefreshLayout article_srl;
    protected RecyclerView article_rv;
    protected BriefArticleAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_container_fragment, container, false);

        article_root_fl = view.findViewById(R.id.article_container_root_frameLayout);
        article_srl = view.findViewById(R.id.article_swipeRefresh);
        article_rv = view.findViewById(R.id.recyclerView);
//        article_rv.setVerticalScrollBarEnabled(true);
//        article_rv.setScrollBarSize(20);
//        article_rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));\

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ArticleContainerViewModel.class);
        article_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BriefArticleAdapter(getContext(), mViewModel.articleResponse.getValue().getBriefArticleList());
        article_rv.setAdapter(adapter);
        eventBinding();

        article_srl.setRefreshing(true);
        requestArticleFromServer();
    }


    protected void eventBinding() {
        mViewModel.articleResponse.observe(getViewLifecycleOwner(), new Observer<BriefArticleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(BriefArticleResponse briefArticleResponse) {
                int currentPage = briefArticleResponse.getCurrentPage();
                int totalPage = briefArticleResponse.getTotalPage();
                if (currentPage != 0 && currentPage==totalPage) {
                    adapter.setMore(false);
                }
//                if (briefArticleResponse.getCurrentPage() == briefArticleResponse.getTotalPage()-1) {
//                    adapter.setMore(false);
//                }
                if (currentPage == 1) {
                    adapter.setContents(briefArticleResponse.getBriefArticleList());
                } else {
                    adapter.appendContents(briefArticleResponse.getBriefArticleList());
                }
            }
        });

        article_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setContents(new ArrayList<>());
                mViewModel.articleResponse.getValue().setCurrentPage(-1);
                requestArticleFromServer();
            }
        });

        article_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItem = manager.getItemCount();
                    // 最后一个
                    if (lastVisibleItem == totalItem-1) {
                        BriefArticleResponse briefArticleResponse = mViewModel.articleResponse.getValue();
                        if (briefArticleResponse.getCurrentPage() < briefArticleResponse.getTotalPage()) {
                            requestArticleFromServer();
                        }
                    }
                }
            }
        });
    }

    protected abstract void requestArticleFromServer(); //{
//        WebService webService = ServiceCreator.create(WebService.class);
//        webService.request("newtopic.php", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                ResponseBody responseBody = response.body();
//                if (responseBody != null) {
//                    ArticleResponse articleResponse = new ArticleResponse();
//                    try {
//                        articleResponse = HtmlParser.parseNewsToday(new String(responseBody.bytes(), "GBK"));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    mViewModel.articleResponse.postValue(articleResponse);
//                }
//                article_srl.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                article_srl.setRefreshing(false);
//            }
//        });
//    }
}