package com.wuda.bbs.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.bean.Board;

import java.util.List;

@Dao
public interface BoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<Board> boardList);

    @Query("select * from Board")
    public abstract List<Board> loadAllBoards();

    @Query("select * from Board where section = :section")
    public abstract List<Board> loadBoardBySection(String section);

    @Query("delete from Board")
    public abstract void clearAll();
}
