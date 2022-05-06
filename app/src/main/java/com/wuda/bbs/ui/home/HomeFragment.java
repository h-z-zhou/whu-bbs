package com.wuda.bbs.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;
import com.wuda.bbs.ui.MainActivity;

public class HomeFragment extends Fragment {

    TabLayout home_tl;
    ViewPager2 home_vp2;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        if (getActivity() != null) {
            Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
            if (toolbar != null) {
                toolbar.setTitle("主页");
            }
        }

        home_tl = view.findViewById(R.id.home_tabLayout);
        home_vp2 = view.findViewById(R.id.home_viewPager2);

        home_vp2.setAdapter(new FragmentStateAdapter(getChildFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return new RecommendArticleFragment();
                } else if (position == 1) {
                    return new HotArticleFragment();
                } else if (position == 2) {
                    return new TodayNewArticleFragment();
                } else if (position == 3) {
                    return new BoardEntranceFragment();
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 4;
            }
        });

        new TabLayoutMediator(home_tl, home_vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("推荐文章");
                } else if (position == 1) {
                    tab.setText("十大热点");
                } else if (position == 2) {
                    tab.setText("今日新帖");
                } else if (position == 3) {
                    tab.setText("版块列表");
                }
            }
        }).attach();

        return view;
    }

}