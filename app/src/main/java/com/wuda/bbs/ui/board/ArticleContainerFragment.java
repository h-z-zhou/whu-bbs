package com.wuda.bbs.ui.board;

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
import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.BriefArticleAdapter;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

import java.util.ArrayList;
import java.util.List;

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
        adapter = new BriefArticleAdapter(getContext(), new ArrayList<>());
        article_rv.setAdapter(adapter);
        eventBinding();

        article_srl.setRefreshing(true);
        requestArticleFromServer();
    }


    protected void eventBinding() {

        mViewModel.articleResponse.observe(getViewLifecycleOwner(), new Observer<ContentResponse<List<BriefArticle>>>() {
            @Override
            public void onChanged(ContentResponse<List<BriefArticle>> listContentResponse) {

                article_srl.setRefreshing(false);

                if (listContentResponse.isSuccessful()) {
                    int currentPage = listContentResponse.getCurrentPage();
                    int totalPage = listContentResponse.getTotalPage();
                    if (currentPage != 0 && currentPage==totalPage) {
                        adapter.setMore(false);
                    }

                    if (currentPage == 1) {
                        adapter.setContents(listContentResponse.getContent());
                    } else {
                        adapter.appendContents(listContentResponse.getContent());
                    }
                    mViewModel.appendArticles(listContentResponse.getContent());
                } else {
                    new ResponseErrorHandlerDialog(getContext())
                            .addErrorResponse(listContentResponse)
                            .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                                @Override
                                public void onButtonClick() {
                                    requestArticleFromServer();
                                }
                            })
                            .show();
                }
            }
        });



        article_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.setContents(new ArrayList<>());
                if (mViewModel.articleResponse.getValue() != null) {
                    mViewModel.articleResponse.getValue().setCurrentPage(-1);
                    requestArticleFromServer();
                }
            }
        });

        article_rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && manager!=null) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItem = manager.getItemCount();
                    // 最后一个
                    if (lastVisibleItem == totalItem-1) {
                        ContentResponse<List<BriefArticle>> briefArticleResponse = mViewModel.articleResponse.getValue();
                        if (briefArticleResponse != null && briefArticleResponse.getCurrentPage() < briefArticleResponse.getTotalPage()) {
                            requestArticleFromServer();
                        }
                    }
                }
            }
        });
    }

    protected abstract void requestArticleFromServer();
}