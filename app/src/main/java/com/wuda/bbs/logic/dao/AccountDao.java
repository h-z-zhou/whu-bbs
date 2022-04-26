package com.wuda.bbs.logic.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.wuda.bbs.logic.bean.bbs.Account;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAccount(Account account);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAccount(List<Account> accounts);
    @Update
    void updateUser(Account newAccount);

    @Query("select * from Account")
    List<Account> loadAllAccounts();

    @Query(("delete from Account where id = :id"))
    int deleteAccountById(String id);
}
