package com.wuda.bbs.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.wuda.bbs.bean.Board;
import com.wuda.bbs.bean.User;

@Database(version = 2, entities = {User.class, Board.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract BoardDao getBoardDao();

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app_database")
                .allowMainThreadQueries()
                .build();
    }
}
