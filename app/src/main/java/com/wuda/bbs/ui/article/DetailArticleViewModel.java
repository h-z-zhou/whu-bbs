package com.wuda.bbs.ui.article;

import androidx.lifecycle.MutableLiveData;

import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.bbs.DetailArticle;
import com.wuda.bbs.logic.bean.bbs.History;
import com.wuda.bbs.logic.bean.bbs.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.HistoryDao;
import com.wuda.bbs.ui.base.BaseResponseViewModel;
import com.wuda.bbs.utils.networkResponseHandler.DetailArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailArticleViewModel extends BaseResponseViewModel {

    private BriefArticle mBriefArticle;
    private MutableLiveData<List<DetailArticle>> detailArticlesMutableLiveData;
    private MutableLiveData<Boolean> favoriteMutableLiveData;

    public BriefArticle getBriefArticle() {
        return mBriefArticle;
    }

    public void setBriefArticle(BriefArticle mBriefArticle) {
        this.mBriefArticle = mBriefArticle;
    }

    public MutableLiveData<List<DetailArticle>> getDetailArticlesMutableLiveData() {
        if (detailArticlesMutableLiveData == null) {
            detailArticlesMutableLiveData = new MutableLiveData<>();
        }
        return detailArticlesMutableLiveData;
    }

    public MutableLiveData<Boolean> getFavoriteMutableLiveData() {
        if (favoriteMutableLiveData == null) {
            favoriteMutableLiveData = new MutableLiveData<>();
        }
        return favoriteMutableLiveData;
    }

    public void requestContentFromServer() {
        Map<String, String> form = new HashMap<>();
        form.put("GID", mBriefArticle.getGID());
        form.put("board", mBriefArticle.getBoardID());
        form.put("page", "1");

        NetworkEntry.requestArticleContent(form, new DetailArticleHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<DetailArticle>> response) {
                if (response.isSuccessful()) {
                    detailArticlesMutableLiveData.postValue(response.getContent());
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }

        });
    }

    public void add2history() {
        HistoryDao historyDao = AppDatabase.getDatabase().getHistoryDao();
        historyDao.insertHistory(
                new History(
                        mBriefArticle.getGID(),
                        mBriefArticle.getTitle(),
                        mBriefArticle.getAuthor(),
                        mBriefArticle.getBoardID(),
                        Calendar.getInstance().getTimeInMillis()
                )
        );
    }

    public void add2Favor() {
        Map<String, String> form = new HashMap<>();
        // act=add&title=&type=0&pid=2&url=
        form.put("act", "add");
        form.put("title", mBriefArticle.getTitle());
        form.put("type", "0");
        form.put("pid", "2");
        // url => bbscon.php?bid=102&id=1105517542
        // url => wForum/disparticle.php?boardName=Advice&ID=1105517558&pos=1
        String url = "wForum/disparticle.php?boardName=" + mBriefArticle.getBoardID() + "&" + "ID=" + mBriefArticle.getGID() + "&pos=1";
        form.put("url", url);

        NetworkEntry.addArticle2Fav(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (response.isSuccessful()) {
                    favoriteMutableLiveData.postValue(true);
                } else {
                    errorResponseMutableLiveData.postValue(response);
                }
            }
        });
    }
}
