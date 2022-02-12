package com.wuda.bbs.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wuda.bbs.bean.DetailBoard;
import com.wuda.bbs.bean.FavorBoard;
import com.wuda.bbs.bean.Friend;
import com.wuda.bbs.bean.History;
import com.wuda.bbs.bean.User;

@Database(version = 2, entities = {User.class, DetailBoard.class, FavorBoard.class, History.class, Friend.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract DetailBoardDao getDetailBoardDao();
    public abstract FavorBoardDao getFavorBoardDao();
    public abstract HistoryDao getHistoryDao();
    public abstract FriendDao getFriendDao();

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
                .allowMainThreadQueries()
                .build();
    }
}
