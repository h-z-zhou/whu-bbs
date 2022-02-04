package com.wuda.bbs.ui.main.board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;
import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.FavorBoard;
import com.wuda.bbs.dao.AppDatabase;
import com.wuda.bbs.dao.FavorBoardDao;
import com.wuda.bbs.ui.main.post.WriteArticleActivity;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.NetConst;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

                BoardArticleFragment boardArticleFragment = new BoardArticleFragment();
                boardArticleFragment.setBoard(mViewModel.favorBoardList.getValue().get(position));
                return boardArticleFragment;
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

//        writeArticle_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BaseBoard board;
//
//                if (mViewModel.currentBoardIdx.getValue() == -1) {
//                    board = null;
//                } else {
//                    board = mViewModel.favorBoardList.getValue().get(mViewModel.currentBoardIdx.getValue());
//                }
//
//                Intent intent = new Intent(getContext(), WriteArticleActivity.class);
//                intent.putExtra("board", board);
//                startActivity(intent);
//            }
//        });
    }

    private void queryFavorBoardsFromDB() {
        if (getContext() == null)
            return;
        FavorBoardDao favorBoardDao = AppDatabase.getDatabase(getContext()).getFavorBoardDao();
        if (BBSApplication.getUsername().equals(""))
            return;

        List<BaseBoard> favorBoardList = favorBoardDao.loadFavorBoardByUsername(BBSApplication.getUsername());

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
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        mobileService.request("favor", new HashMap<>()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {

                    String text = "";
                    if (response.body() != null) {
                        text = response.body().string();
                    }
                    if (text.equals(NetConst.FAVOR_BOARD_ERROR)) {
                        if (getContext() != null) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("未登录")
                                    .setMessage("请先登录")
                                    .create()
                                    .show();
                        }
                        return;
                    }

                    hadRequest = true;

                    List<BaseBoard> favorBoardList = XMLParser.parseFavorBoard(text);
                    if (getContext() == null)
                        return;
                    FavorBoardDao favorBoardDao = AppDatabase.getDatabase(getContext()).getFavorBoardDao();
                    favorBoardDao.clearAll();
                    // cast => save to database
                    List<FavorBoard> castFavorBoardList = new ArrayList<>();
                    for (int i=0; i<favorBoardList.size(); ++i) {
                        castFavorBoardList.add((FavorBoard) favorBoardList.get(i));
                    }
                    favorBoardDao.insert(castFavorBoardList);

                    queryFavorBoardsFromDB();

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