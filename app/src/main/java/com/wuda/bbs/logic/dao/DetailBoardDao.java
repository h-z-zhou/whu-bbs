package com.wuda.bbs.logic.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.logic.bean.DetailBoard;

import java.util.List;

@Dao
public interface DetailBoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<DetailBoard> detailBoardList);

    @Query("select * from DetailBoard")
    public abstract List<DetailBoard> loadAllBoards();

    @Query("select * from DetailBoard where section = :section")
    public abstract List<DetailBoard> loadBoardsBySection(String section);

    @Query("select board.* from DetailBoard board group by board.section")
    public abstract List<DetailBoard> loadBoardsGroupBySection();

    @Query("delete from DetailBoard")
    public abstract void clearAll();
}
