package com.wuda.bbs.logic.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.logic.bean.BaseBoard;
import com.wuda.bbs.logic.bean.FavBoard;

import java.util.List;

@Dao
public interface FavorBoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<FavBoard> boardList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(FavBoard favBoard);

    @Query("select * from FavBoard")
    public abstract List<FavBoard> loadAllFavorBoards();

    @Query("select * from FavBoard where favor_by_username = :username")
    public abstract List<BaseBoard> loadFavorBoardByUsername(@NonNull String username);

    @Query("delete from FavBoard where favor_by_username = :username")
    public abstract void clearFavorBoardsByUsername(String username);

    @Query("delete from FavBoard")
    public abstract void clearAll();

    @Query("delete from FavBoard where id = :id")
    public abstract void delete(String id);
}
