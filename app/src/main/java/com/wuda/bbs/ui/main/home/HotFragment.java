package com.wuda.bbs.ui.main.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.bean.Board;
import com.wuda.bbs.ui.adapter.ArticleRecyclerAdapter;
import com.wuda.bbs.ui.adapter.BriefArticleRecyclerAdapter;
import com.wuda.bbs.utils.network.MobileAppService;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HotFragment extends Fragment {

    private HotViewModel mViewModel;
    private SwipeRefreshLayout article_srl;
    private RecyclerView article_rv;

    public static HotFragment newInstance() {
        return new HotFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_fragment, container, false);

        article_srl = view.findViewById(R.id.article_swipeRefresh);
        article_rv = view.findViewById(R.id.article_recyclerView);
        article_rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        article_rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HotViewModel.class);
        article_rv.setAdapter(new ArticleRecyclerAdapter(getContext(), mViewModel.hotArticleResponse.getValue().getArticleList(), true));

        eventBinding();

        requestArticleFromServer();
    }

    private void eventBinding() {
        mViewModel.hotArticleResponse.observe(getViewLifecycleOwner(), new Observer<ArticleResponse>() {
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
    }

    private void requestArticleFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        mobileService.request("hot", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = response.body().string();
                    ArticleResponse articleResponse = XMLParser.parseHot(text);
                    mViewModel.hotArticleResponse.postValue(articleResponse);
                    Log.d("Article", text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}