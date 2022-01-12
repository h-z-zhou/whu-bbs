package com.wuda.bbs.ui.main.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;
import com.wuda.bbs.ui.main.mail.ContactFragment;
import com.wuda.bbs.ui.main.mail.LetterFragment;

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

        home_tl = view.findViewById(R.id.home_tabLayout);
        home_vp2 = view.findViewById(R.id.home_viewPager2);

        home_vp2.setAdapter(new FragmentStateAdapter(requireActivity().getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return new RecommendFragment();
                } else if (position == 1) {
                    return new HotFragment();
                } else if (position == 2) {
                    return new NewsTodayFragment();
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 3;
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
                }
            }
        }).attach();


        return view;
    }

}