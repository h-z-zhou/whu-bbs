package com.wuda.bbs.logic.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.logic.bean.bbs.DetailBoard;

import java.util.List;

@Dao
public interface DetailBoardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<DetailBoard> detailBoardList);

    @Query("select * from DetailBoard")
    List<DetailBoard> loadAllBoards();

    @Query("select * from DetailBoard where section = :section")
    List<DetailBoard> loadBoardsBySection(String section);

    @Query("select board.* from DetailBoard board group by board.section")
    List<DetailBoard> loadBoardsGroupBySection();

    @Query("delete from DetailBoard")
    void clearAll();
}
