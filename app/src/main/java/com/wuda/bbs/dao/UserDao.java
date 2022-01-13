package com.wuda.bbs.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wuda.bbs.bean.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(List<User> users);
    @Update
    void updateUser(User newUser);

    @Query("select * from User")
    List<User> loadAllUsers();

    @Query(("delete from User where name = :name"))
    int deleteUserByName(String name);
}
