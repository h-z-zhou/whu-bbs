package com.wuda.bbs.ui.drawer;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FriendDao;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.FriendResponseHandler;

import java.util.List;

public class FriendViewModel extends BaseResponseViewModel {
    private MutableLiveData<List<Friend>> friendListMutableLiveData;

    public MutableLiveData<List<Friend>> getFriendListMutableLiveData() {
        if (friendListMutableLiveData == null) {
            friendListMutableLiveData = new MutableLiveData<>();
        }
        return friendListMutableLiveData;
    }

    public void requestAllFriendsFromServer() {
        NetworkEntry.requestFriend(new FriendResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<Friend>> response) {
                if (response.isSuccessful()) {
                    List<Friend> friends = response.getContent();
                    friendListMutableLiveData.postValue(friends);
                    FriendDao friendDao = AppDatabase.getDatabase().getFriendDao();
                    friendDao.insertFriends(friends);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }

}