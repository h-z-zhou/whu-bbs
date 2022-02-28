package com.wuda.bbs.ui.board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FavorBoardDao;
import com.wuda.bbs.ui.MainActivity;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;

import java.util.ArrayList;
import java.util.List;

public class FavorBoardFragment extends Fragment {

    private FavorBoardViewModel mViewModel;
    private TabLayout board_tl;
    private ViewPager2 board_vp2;
//    private FloatingActionButton writeArticle_fab;

    // 收藏可能为空，请求一次后不再请求
    private boolean hadRequest = false;

    public static FavorBoardFragment newInstance() {
        return new FavorBoardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favor_board_fragment, container, false);
        board_tl = view.findViewById(R.id.favorBoard_tabLayout);
        board_vp2 = view.findViewById(R.id.favorBoard_viewPager2);
//        writeArticle_fab = view.findViewById(R.id.favorBoard_writeArticle_fab);
        requestFavorBoardsFromServer();

        if (getActivity() != null) {
            ((MainActivity) getActivity()).getToolbar().setTitle("收藏版块");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavorBoardViewModel.class);

//        board_vp2.setAdapter(new BoardViewPager2Adapter(getContext(), mViewModel.favoriteBoardList.getValue()));
        board_vp2.setAdapter(new FragmentStateAdapter(requireActivity().getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {

                BoardArticleFragment boardArticleFabFragment = new BoardArticleFragment();
                boardArticleFabFragment.setBoard(mViewModel.favorBoardList.getValue().get(position));
                return boardArticleFabFragment;
            }

            @Override
            public int getItemCount() {
                return mViewModel.favorBoardList.getValue().size();
            }
        });

        new TabLayoutMediator(board_tl, board_vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mViewModel.favorBoardList.getValue().get(position).getName());
            }
        }).attach();

        eventBinding();

//        queryBoardsFromDB();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.board_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.favorBoardManager) {
            Intent intent = new Intent(requireContext(), FavorBoardManagerActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void eventBinding() {
        mViewModel.favorBoardList.observe(getViewLifecycleOwner(), boardList -> {
            if (board_vp2.getAdapter() != null) {
                board_vp2.getAdapter().notifyDataSetChanged();
            }
        });

        board_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewModel.currentBoardIdx.setValue(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void queryFavorBoardsFromDB() {
        if (getContext() == null)
            return;
        FavorBoardDao favorBoardDao = AppDatabase.getDatabase(getContext()).getFavorBoardDao();
        if (BBSApplication.getAccountId().equals(""))
            return;

        List<FavBoard> favorBoardList = favorBoardDao.loadFavorBoardByUsername(BBSApplication.getAccountId());

        if (favorBoardList.isEmpty()) {
            if (!hadRequest) {
                requestFavorBoardsFromServer();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle("空空如也")
                        .setMessage("点开收藏，选择喜欢的板块")
                        .create()
                        .show();
            }
        } else {
            mViewModel.favorBoardList.setValue(favorBoardList);
        }

    }

    private void requestFavorBoardsFromServer() {

        NetworkEntry.requestFavBoard(new FavBoardHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<FavBoard>> response) {

                if (response.isSuccessful()) {
                    hadRequest = true;
                    List<FavBoard> favorBoardList = response.getContent();
                    if (getContext() == null)
                        return;
                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase(getContext()).getFavorBoardDao();
                    favorBoardDao.clearAll();
                    // cast => save to database
                    List<FavBoard> castFavBoardList = new ArrayList<>();
                    for (int i=0; i<favorBoardList.size(); ++i) {
                        castFavBoardList.add((FavBoard) favorBoardList.get(i));
                    }
                    favorBoardDao.insert(castFavBoardList);

                    queryFavorBoardsFromDB();
                } else {
                    switch (response.getResultCode()) {
                        case LOGIN_ERR:
                            Toast.makeText(getContext(), response.getResultCode().getMsg(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}