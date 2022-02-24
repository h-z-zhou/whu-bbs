package com.wuda.bbs.logic.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.DetailBoard;
import com.wuda.bbs.logic.bean.FavorBoard;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.History;

@Database(version = 2, entities = {Account.class, DetailBoard.class, FavorBoard.class, History.class, Friend.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao getAccountDao();
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
