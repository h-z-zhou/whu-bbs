package com.wuda.bbs.ui.main.mail;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wuda.bbs.R;

public class MailNavFragment extends Fragment {

    private MailNavViewModel mViewModel;

    private TabLayout mail_tl;
    private ViewPager2 mail_vp2;

    public static MailNavFragment newInstance() {
        return new MailNavFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mail_nav_fragment, container, false);

        mail_tl = view.findViewById(R.id.home_tabLayout);
        mail_vp2 = view.findViewById(R.id.home_viewPager2);

        mail_vp2.setAdapter(new FragmentStateAdapter(requireActivity().getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) {
                    return MailFragment.newInstance();
                } else if (position == 1) {
//                    return FriendFragment.newInstance();
                }
                return null;
            }

            @Override
            public int getItemCount() {
                return 1;
            }
        });

        new TabLayoutMediator(mail_tl, mail_vp2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0) {
                    tab.setText("信件");
                } else if (position == 1) {
                    tab.setText("好友");
                }
            }
        }).attach();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MailNavViewModel.class);
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.friend_menu, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
}