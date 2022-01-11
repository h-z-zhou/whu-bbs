package com.wuda.bbs.ui.main.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;
import com.wuda.bbs.bean.ArticleResponse;
import com.wuda.bbs.ui.adapter.ArticleRecyclerAdapter;
import com.wuda.bbs.ui.main.mail.ContactFragment;
import com.wuda.bbs.ui.main.mail.LetterFragment;
import com.wuda.bbs.utils.htmlParser.HtmlParser;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.network.WebService;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsTodayFragment extends Fragment {

    private NewsTodayViewModel mViewModel;
    private SwipeRefreshLayout article_srl;
    private RecyclerView article_rv;

    public static NewsTodayFragment newInstance() {
        return new NewsTodayFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_today_fragment, container, false);

        article_srl = view.findViewById(R.id.article_swipeRefresh);
        article_rv = view.findViewById(R.id.article_recyclerView);
        article_rv.setLayoutManager(new LinearLayoutManager(getContext()));
//        article_rv.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewsTodayViewModel.class);
        article_rv.setAdapter(new ArticleRecyclerAdapter(getContext(), mViewModel.newsTodayArticleResponse.getValue().getArticleList(), true));
        eventBinding();

        requestArticleFromServer();
    }


    private void eventBinding() {
        mViewModel.newsTodayArticleResponse.observe(getViewLifecycleOwner(), new Observer<ArticleResponse>() {
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
        WebService webService = ServiceCreator.create(WebService.class);
        webService.request("newtopic.php", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                ResponseBody responseBody = response.body();
                if (responseBody != null) {
                    ArticleResponse articleResponse = new ArticleResponse();
                    try {
                        articleResponse = HtmlParser.parseNewsToday(new String(responseBody.bytes(), "GBK"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mViewModel.newsTodayArticleResponse.postValue(articleResponse);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}