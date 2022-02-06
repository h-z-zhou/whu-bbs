package com.wuda.bbs.ui.main.mail;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.Friend;
import com.wuda.bbs.bean.response.FriendResponse;
import com.wuda.bbs.ui.adapter.FriendAdapter;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {

    private FriendViewModel mViewModel;
    private RecyclerView friend_rv;

    public static FriendFragment newInstance() {
        return new FriendFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.friend_fragment, container, false);
        friend_rv = view.findViewById(R.id.recyclerView);
        friend_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        friend_rv.setAdapter(new FriendAdapter(getContext(), new ArrayList<>()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        eventBinding();
        requestAllFriendsFromServer();
    }

    private void eventBinding() {
        mViewModel.friendList.observe(getViewLifecycleOwner(), new Observer<List<Friend>>() {
            @Override
            public void onChanged(List<Friend> friends) {
                ((FriendAdapter) friend_rv.getAdapter()).updateFriendList(friends);
            }
        });
    }

    private void requestAllFriendsFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
//        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("list", "all");

        mobileService.request("friend", form).enqueue(new BBSCallback<ResponseBody>(getContext()) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String text = response.body().string();
                        FriendResponse friendResponse = XMLParser.parseFriends(text);
                        if (friendResponse.isSuccessful()) {
                            mViewModel.friendList.postValue(friendResponse.getFriendList());
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}