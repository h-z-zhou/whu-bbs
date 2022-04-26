package com.wuda.bbs.logic.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.bean.bbs.Account;
import com.wuda.bbs.logic.bean.bbs.DetailBoard;
import com.wuda.bbs.logic.bean.bbs.FavBoard;
import com.wuda.bbs.logic.bean.bbs.Friend;
import com.wuda.bbs.logic.bean.bbs.History;

@Database(version = 1, entities = {Account.class, DetailBoard.class, FavBoard.class, History.class, Friend.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao getAccountDao();
    public abstract DetailBoardDao getDetailBoardDao();
    public abstract FavorBoardDao getFavorBoardDao();
    public abstract HistoryDao getHistoryDao();
    public abstract FriendDao getFriendDao();

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "app_database")
                .allowMainThreadQueries()
                .build();
    }

    public static AppDatabase getDatabase() {
        return Room.databaseBuilder(BBSApplication.getAppContext(), AppDatabase.class, "app_database")
                .allowMainThreadQueries()
                .build();
    }

}
