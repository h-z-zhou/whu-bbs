package com.wuda.bbs.ui.main.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.Board;
import com.wuda.bbs.ui.adapter.ArticleRecyclerAdapter;
import com.wuda.bbs.utils.htmlParser.HtmlParser;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.network.WebService;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ArticleContainerFragment extends Fragment {

    protected ArticleContainerViewModel mViewModel;
    protected SwipeRefreshLayout article_srl;
    protected RecyclerView article_rv;

//    public ArticleContainerFragment(){};

//    public ArticleContainerFragment(Board board){
//        mViewModel = new ViewModelProvider(this, new ArticleContainerViewModelFactory(board))
//                .get(ArticleContainerViewModel.class);
//        mViewModel.board.setValue(board);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_container_fragment, container, false);


        article_srl = view.findViewById(R.id.article_swipeRefresh);
        article_rv = view.findViewById(R.id.article_recyclerView);
        article_rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        article_rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ArticleContainerViewModel.class);
        article_rv.setAdapter(new ArticleRecyclerAdapter(getContext(), mViewModel.articleResponse.getValue().getArticleList()));
        eventBinding();

        article_srl.setRefreshing(true);
        requestArticleFromServer();
    }


    protected void eventBinding() {
        mViewModel.articleResponse.observe(getViewLifecycleOwner(), new Observer<ArticleResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(ArticleResponse articleResponse) {
                if (article_rv.getAdapter() != null) {
                    ArticleRecyclerAdapter adapter = (ArticleRecyclerAdapter) article_rv.getAdapter();
                    adapter.updateArticleList(articleResponse.getArticleList());
                    adapter.notifyDataSetChanged();
                }

            }
        });

        article_srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestArticleFromServer();
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