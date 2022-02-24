package com.wuda.bbs.logic.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.FavorBoard;

import java.util.List;

@Dao
public interface FavorBoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<FavorBoard> boardList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(FavorBoard favorBoard);

    @Query("select * from FavorBoard")
    public abstract List<FavorBoard> loadAllFavorBoards();

    @Query("select * from FavorBoard where favor_by_username = :username")
    public abstract List<BaseBoard> loadFavorBoardByUsername(@NonNull String username);

    @Query("delete from FavorBoard where favor_by_username = :username")
    public abstract void clearFavorBoardsByUsername(String username);

    @Query("delete from FavorBoard")
    public abstract void clearAll();

    @Query("delete from FavorBoard where id = :id")
    public abstract void delete(String id);
}
