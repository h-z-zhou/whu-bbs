package com.wuda.bbs.logic.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.logic.bean.bbs.Friend;

import java.util.List;

@Dao
public interface FriendDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertFriends(List<Friend> friends);

    @Insert
    void insertFriend(Friend friend);

    @Query("select * from Friend where id = :id limit 1")
    Friend loadFriend(String id);

    @Query("select * from Friend")
    List<Friend> loadAllFriends();

    @Delete
    void deleteFriend(Friend friend);
}
