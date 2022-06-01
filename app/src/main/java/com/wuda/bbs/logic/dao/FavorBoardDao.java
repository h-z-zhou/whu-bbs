package com.wuda.bbs.logic.dao;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.logic.bean.bbs.FavBoard;

import java.util.List;

@Dao
public interface FavorBoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<FavBoard> boardList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavBoard favBoard);

    @Query("select * from FavBoard")
    List<FavBoard> loadAllFavorBoards();

    @Query("select * from FavBoard where favor_by_username = :username")
    List<FavBoard> loadFavorBoardByUsername(@NonNull String username);

    @Query("delete from FavBoard where favor_by_username = :username")
    void clearFavorBoardsByUsername(String username);

    @Query("delete from FavBoard")
    void clearAll();

    @Query("delete from FavBoard where id = :id")
    void delete(String id);
}
