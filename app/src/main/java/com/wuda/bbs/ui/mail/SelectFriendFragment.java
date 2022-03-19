package com.wuda.bbs.ui.mail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FriendDao;
import com.wuda.bbs.ui.adapter.FriendAdapter;

import java.util.List;

public class SelectFriendFragment extends Fragment {

    SendMailViewModel mSendMailViewModel;
    RecyclerView friend_rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_friend, container, false);

        friend_rv = view.findViewById(R.id.recyclerView);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSendMailViewModel = new ViewModelProvider(requireActivity()).get(SendMailViewModel.class);

        FriendDao friendDao = AppDatabase.getDatabase(getContext()).getFriendDao();
        List<Friend> friendList = friendDao.loadAllFriends();

        friend_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        FriendAdapter adapter = new FriendAdapter(getContext(), friendList);
        adapter.setMore(false);
        adapter.setOnFriendSelectedListener(new FriendAdapter.OnFriendSelectedListener() {
            @Override
            public void oFriendSelected(Friend friend) {
                mSendMailViewModel.userIdMutableLiveData.postValue(friend.getId());
                requireActivity().onBackPressed();
            }
        });
        friend_rv.setAdapter(adapter);

    }
}