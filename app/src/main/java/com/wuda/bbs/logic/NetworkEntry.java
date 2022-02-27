package com.wuda.bbs.logic;

import androidx.annotation.NonNull;

import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.utils.network.AuthBBSCallback;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.NoAuthBBSCallback;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.RootServiceGBKWrapper;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.AccountResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.DetailArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.DetailBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FavArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.FavBoardHandler;
import com.wuda.bbs.utils.networkResponseHandler.FriendResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.HotArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.MailContentHandler;
import com.wuda.bbs.utils.networkResponseHandler.MailListHandler;
import com.wuda.bbs.utils.networkResponseHandler.RecommendArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.SetPasswordResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.SettingParamHandler;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.TodayNewArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.TopicArticleHandler;
import com.wuda.bbs.utils.networkResponseHandler.UserInfoResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkEntry {

    static RootService mRootService = ServiceCreator.create(RootService.class);
    static RootServiceGBKWrapper mRootServiceGBKWrapper = new RootServiceGBKWrapper(mRootService);
    static MobileService mMobileService = ServiceCreator.create(MobileService.class);


    // *******************************
    // Account
    // *******************************

    public static void login(Account account, AccountResponseHandler responseHandler) {

        requestUserInfo(account.getId(), new UserInfoResponseHandler() {
            @Override
            public void onResponseHandled(ContentResponse<UserInfo> response) {
                UserInfo info = response.getContent();
                account.setAvatar(info.getAvatar());
                _login(account, responseHandler);
            }
        });
    }

    private static void _login(Account account, AccountResponseHandler responseHandler) {

        Map<String, String> form = new HashMap<>();
        form.put("app", "login");
        form.put("id", account.getId());
        form.put("passwd", account.getPasswd());

        // results in cookies
        mMobileService.post(form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                List<String> cookies = response.headers().values("Set-Cookie");

                Map<String, String> cookiesFilter = new HashMap<>();

                for (String cookie : cookies) {
                    String[] tmp = cookie.split(";");
                    if (tmp.length > 0) {
                        String[] name_value = tmp[0].split("=");
                        if (name_value.length == 2) {
                            cookiesFilter.put(name_value[0], name_value[1]);
                        }
                    }
                }

//                AccountResponse accountResponse;
                ContentResponse<Account> accountResponse;

                // 是否成功登录
                String userId = cookiesFilter.get("UTMPUSERID");
                if (userId == null)
                    userId = "guest";
                if (userId.equals("guest") || userId.equals("deleted")) {
                    accountResponse = new ContentResponse<>(ResultCode.LOGIN_ERR, "登录失败，请检查帐号和密码！");
                    responseHandler.onResponseHandled(accountResponse);
                    return;
                }

                accountResponse = new ContentResponse<>();
                accountResponse.setContent(account);

                responseHandler.onResponseHandled(accountResponse);
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                responseHandler.onResponseHandled(new ContentResponse<>(ResultCode.CONNECT_ERR, t.getMessage()));
            }
        });
    }

    public static void logout(SimpleResponseHandler responseHandler) {
        mRootService.get("bbslogout.php").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                String cookies = response.headers().get("Set-Cookie");
                if (cookies == null) {
                    responseHandler.onResponseHandled(new ContentResponse<>(ResultCode.LOGIN_ERR, "退出成功"));
                } else {
                    responseHandler.onResponseHandled(new ContentResponse<>(ResultCode.ERROR, "未知错误"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    responseHandler.onResponseHandled(new ContentResponse<>(ResultCode.CONNECT_ERR, t.getMessage()));
            }
        });
    }

    public static void register(Map<String, String> form, SimpleResponseHandler handler) {
        mRootServiceGBKWrapper.post("bbsreg.php", form).enqueue(new NoAuthBBSCallback<>(handler));
    }

    public static void setPassword(Map<String, String> form, SetPasswordResponseHandler handler) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("do", "");

        mRootService.post("bbspwd.php", queryMap, form).enqueue(new AuthBBSCallback<>(handler));
    }

    public static void findPassword(Map<String, String> form, SimpleResponseHandler responseHandler) {

        mRootService.post("r/doreset.php", form).enqueue(new NoAuthBBSCallback<>(responseHandler));
    }

    public static void requestSettingParam(SettingParamHandler responseHandler) {
        mRootService.get("wForum/userparam.php").enqueue(new AuthBBSCallback<>(responseHandler));
    }

    public static void setSettingParam(Map<String, String> form, WebResultHandler handler) {
        mRootService.post("wForum/saveuserparam.php", form).enqueue(new AuthBBSCallback<>(handler));
    }

    // *******************************
    // User / Friend
    // *******************************

    public static void requestUserInfo(String userId, UserInfoResponseHandler responseHandler) {
        Map<String, String> form = new HashMap<>();
        form.put("userId", userId);

        mMobileService.get("userInfo", form).enqueue(new NoAuthBBSCallback<>(responseHandler));
    }

    public static void requestFriend(FriendResponseHandler responseHandler) {
        Map<String, String> form = new HashMap<>();
        form.put("list", "all");

        mMobileService.get("friend", form).enqueue(new AuthBBSCallback<>(responseHandler));
    }

    public static void operateFriend(Map<String, String> form, SimpleResponseHandler handler) {
        mMobileService.get(form).enqueue(new AuthBBSCallback<>(handler));
    }

    // *******************************
    // Article
    // *******************************
    public static void requestRecommendArticle(RecommendArticleHandler responseHandler) {
        mMobileService.get("recomm").enqueue(new NoAuthBBSCallback<>(responseHandler));
    }

    public static void requestTodayNewArticle(TodayNewArticleHandler responseHandler) {
        mRootService.get("wForum/newtopic.php").enqueue(new NoAuthBBSCallback<>(responseHandler));
    }

    public static void requestHotArticle(HotArticleHandler responseHandler) {
        mMobileService.get("hot").enqueue(new NoAuthBBSCallback<>(responseHandler));
    }

    public static void requestTopicArticle(Map<String, String> form, TopicArticleHandler responseHandler) {
        mMobileService.get("topics", form).enqueue(new NoAuthBBSCallback<>(responseHandler));
    }

    public static void requestArticleContent(Map<String, String> form, DetailArticleHandler handler) {
        mMobileService.get("read", form).enqueue(new AuthBBSCallback<>(handler));
    }

    public static void postArticle(Map<String, String> form, WebResultHandler handler) {
        mRootServiceGBKWrapper.post("wForum/dopostarticle.php", form).enqueue(new AuthBBSCallback<>(handler));
    }

    public static void requestFavArticle(FavArticleHandler handler) {
        Map<String, String> form = new HashMap<>();
        form.put("pid", "2");
        mRootService.get("bbssfav.php", form).enqueue(new AuthBBSCallback<>(handler));
    }

    public static void addArticle2Fav(Map<String, String> form, WebResultHandler handler) {
        mRootServiceGBKWrapper.get("bbssfav.php", form).enqueue(new AuthBBSCallback<>(handler));
    }

    public static void removeFavArticle(Map<String, String> form, WebResultHandler handler) {
        mRootService.get("bbssfav.php", form).enqueue(new AuthBBSCallback<>(handler));
    }

    // *******************************
    // Board
    // *******************************

    public static void requestDetailBoard(DetailBoardHandler responseHandler) {
        mMobileService.get("boards").enqueue(new NoAuthBBSCallback<>(responseHandler));
    }

    public static void requestFavBoard(FavBoardHandler responseHandler) {
        mMobileService.get("favor").enqueue(new AuthBBSCallback<>(responseHandler));
    }

    public static void operateFavBoard(Map<String, String> form, SimpleResponseHandler handler) {
        mRootService.get("bbsfav.php", form).enqueue(new AuthBBSCallback<>(handler));
    }

    // *******************************
    // Mail
    // *******************************



    public static void requestMailList(Map<String, String> form, MailListHandler responseHandler) {
        mMobileService.get("mail", form).enqueue(new AuthBBSCallback<>(responseHandler));
    }


    public static void requestMailContent(Map<String, String> form, MailContentHandler responseHandler) {
        mMobileService.get("mail", form).enqueue(new AuthBBSCallback<>(responseHandler));
    }

    public static void sendMail(Map<String, String> form, WebResultHandler responseHandler) {
        mRootServiceGBKWrapper.post("wForum/dosendmail.php", form).enqueue(new AuthBBSCallback<>(responseHandler));
    }

    // *******************************
    // Article
    // *******************************

    // *******************************
    // Article
    // *******************************

    // *******************************
    // Article
    // *******************************

}
