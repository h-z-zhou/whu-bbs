package com.wuda.bbs.ui.drawer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.logic.bean.bbs.History;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.HistoryDao;

import java.util.List;

public class HistoryViewModel extends ViewModel {
    MutableLiveData<List<History>> historyListMutableLiveData;

    public MutableLiveData<List<History>> getHistoryListMutableLiveData() {
        if (historyListMutableLiveData == null) {
            historyListMutableLiveData = new MutableLiveData<>();
        }
        return historyListMutableLiveData;
    }

    public void loadHistory() {
        HistoryDao historyDao = AppDatabase.getDatabase().getHistoryDao();
        List<History> historyList = historyDao.loadAllHistories();
        historyListMutableLiveData.postValue(historyList);
    }

    public void clearHistory() {
        HistoryDao historyDao = AppDatabase.getDatabase().getHistoryDao();
        historyDao.clearHistory();
    }
}
