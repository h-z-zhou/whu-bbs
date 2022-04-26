package com.wuda.bbs.logic.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.logic.bean.bbs.History;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(History history);

    @Query("select * from History order by readTime DESC ")
    List<History> loadAllHistories();

    @Query("delete from History where GID = :GID")
    void deleteHistory(long GID);

    @Query("delete from History")
    void clearHistory();
}
