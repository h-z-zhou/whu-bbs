package com.wuda.bbs.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.wuda.bbs.bean.History;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(History history);

    @Query("select * from History order by id DESC ")
    List<History> loadAllHistories();

    @Query("delete from History where id = :id")
    void deleteHistory(long id);
}
