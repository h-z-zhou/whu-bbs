package com.wuda.bbs.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.bean.BaseBoard;
import com.wuda.bbs.bean.FavorBoard;

import java.util.List;

@Dao
public interface FavorBoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<FavorBoard> boardList);

    @Query("select * from FavorBoard")
    public abstract List<FavorBoard> loadAllFavorBoards();

    @Query("select * from FavorBoard where favor_by_username = :username")
    public abstract List<BaseBoard> loadFavorBoardByUsername(@NonNull String username);

    @Query("delete from FavorBoard where favor_by_username = :username")
    public abstract void clearFavorBoardByUsername(String username);

    @Query("delete from FavorBoard")
    public abstract void clearAll();
}
