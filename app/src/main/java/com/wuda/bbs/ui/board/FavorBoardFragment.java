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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.FavBoard;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.MainActivity;
import com.wuda.bbs.ui.article.NewArticleActivity;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

public class FavorBoardFragment extends Fragment {

    private FavorBoardViewModel mViewModel;
    private TabLayout board_tl;
    private ViewPager2 board_vp2;
    private FloatingActionButton writeArticle_fab;

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
        writeArticle_fab = view.findViewById(R.id.favorBoard_writeArticle_fab);

        if (getActivity() != null) {
            ((MainActivity) getActivity()).getToolbar().setTitle("收藏版块");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavorBoardViewModel.class);

        if (mViewModel.currentBoardIdx.getValue() == null) {
            mViewModel.queryFavorBoardsFromDB();
        }

        eventBinding();

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
        mViewModel.getFavBoardListMutableLiveData().observe(getViewLifecycleOwner(), boardList -> {
            if (boardList.isEmpty()) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("空空如也")
                        .setMessage("点开收藏，选择喜欢的板块")
                        .create()
                        .show();
                return;
            }

            board_vp2.setAdapter(new FragmentStateAdapter(requireActivity().getSupportFragmentManager(), getLifecycle()) {
                @NonNull
                @Override
                public Fragment createFragment(int position) {
                    BoardArticleFragment boardArticleFabFragment = new BoardArticleFragment();
                    boardArticleFabFragment.setBoard(boardList.get(position));
                    return boardArticleFabFragment;
                }

                @Override
                public int getItemCount() {
                    return boardList.size();
                }
            });

            new TabLayoutMediator(board_tl, board_vp2, new TabLayoutMediator.TabConfigurationStrategy() {
                @Override
                public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    tab.setText(boardList.get(position).getName());
                }
            }).attach();
        });

        mViewModel.getErrorResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(getContext())
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestFavorBoardsFromServer();
                            }
                        })
                        .show();
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

        writeArticle_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.getFavBoardListMutableLiveData().getValue() != null && mViewModel.currentBoardIdx.getValue() != null) {
                    FavBoard favBoard = mViewModel.getFavBoardListMutableLiveData().getValue().get(mViewModel.currentBoardIdx.getValue());
                    Intent intent = new Intent(getContext(), NewArticleActivity.class);
                    intent.putExtra("board", favBoard);
                    startActivity(intent);
                }
            }
        });
    }
}