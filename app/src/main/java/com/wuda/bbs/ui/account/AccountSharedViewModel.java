package com.wuda.bbs.ui.account;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.wuda.bbs.application.BBSApplication;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Account;
import com.wuda.bbs.logic.bean.UserInfo;
import com.wuda.bbs.logic.bean.response.AccountResponse;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.logic.bean.response.ResultCode;
import com.wuda.bbs.logic.bean.response.UserInfoResponse;
import com.wuda.bbs.logic.dao.AccountDao;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.utils.network.BBSCallback2;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.AccountResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.BaseResponseHandler;
import com.wuda.bbs.utils.networkResponseHandler.UserInfoResponseHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountSharedViewModel extends ViewModel {
    private MutableLiveData<Account> currentAccount;
    private MutableLiveData<List<Account>> allAccounts;

    public MutableLiveData<Account> getCurrentAccount() {
        if (currentAccount == null) {
            currentAccount = new MutableLiveData<>();
            if (allAccounts == null) {
                allAccounts = new MutableLiveData<>();
            }
            loadAccounts();
        }
        return currentAccount;
    }

    public MutableLiveData<List<Account>> getAllAccounts() {
        if (allAccounts == null) {
            allAccounts = new MutableLiveData<>();
            if (currentAccount == null) {
                currentAccount = new MutableLiveData<>();
            }
            loadAccounts();
        }
        return allAccounts;
    }

    private void loadAccounts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AccountDao dao = AppDatabase.getDatabase(BBSApplication.getAppContext()).getAccountDao();
                List<Account> accounts = dao.loadAllAccounts();
                allAccounts.postValue(dao.loadAllAccounts());
                for (Account account : accounts) {
                    if (account.getFlag() == Account.FLAG_CURRENT) {
                        currentAccount.postValue(account);
                        break;
                    }
                }
            }
        }).start();
    }

    public void updateCurrentAccount(Account account) {
        List<Account> accountList = allAccounts.getValue();
        if (accountList == null)
            return;
        // switch / new login
        boolean existed = false;
        for (int i = 0; i< accountList.size(); ++i) {
            // 修正历史记录
            accountList.get(i).flag = Account.FLAG_HISTORY;
            if (accountList.get(i).id.equals(account.id)){
                accountList.get(i).setFlag(Account.FLAG_CURRENT);
                existed = true;
            }
        }
        if (!existed) {
            accountList.add(account);
        }
        allAccounts.postValue(accountList);
        currentAccount.postValue(account);

        AccountDao accountDao = AppDatabase.getDatabase(BBSApplication.getAppContext()).getAccountDao();
        BBSApplication.setAccount(account);

        accountDao.insertAccount(accountList);
    }

    public void login(Account account) {
        NetworkEntry.login(account, new AccountResponseHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                if (baseResponse.isSuccessful()) {
                    if (baseResponse instanceof AccountResponse) {
                        updateCurrentAccount(((AccountResponse) baseResponse).getAccount());
                    }
                }
            }
        });
    }

//    public void login(Account account, BaseResponseHandler responseHandler) {
//        MobileService mobileService = ServiceCreator.create(MobileService.class);
//
//        Map<String, String> form = new HashMap<>();
//        form.put("userId", account.getId());
//        // the first time login: use BBSCallBack will cause ResultCode.LOGIN_ERR
//        mobileService.get("userInfo", form).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                try {
//                    ResponseBody body = response.body();
//                    if (body != null) {
//                        UserInfoResponse userInfoResponse = XMLParser.parseUserInfo(body.string());
//                        account.setAvatar(userInfoResponse.getUserInfo().getAvatar());
//                        _login(account, responseHandler);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    BaseResponse baseResponse = new BaseResponse(ResultCode.ERROR, e.getMessage());
//                    responseHandler.onResponseHandled(baseResponse);
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                BaseResponse baseResponse = new BaseResponse(ResultCode.CONNECT_ERR, t.getMessage());
//                responseHandler.onResponseHandled(baseResponse);
//            }
//        });
//    }

    private void _login(Account account, BaseResponseHandler responseHandler) {

        MobileService mobileService = ServiceCreator.create(MobileService.class);

        Map<String, String> form = new HashMap<>();
        form.put("app", "login");
        form.put("id", account.getId());
        form.put("passwd", account.getPasswd());
        mobileService.post(form).enqueue(new Callback<ResponseBody>() {
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

                BaseResponse baseResponse;

                // 是否成功登录
                String userId = cookiesFilter.get("UTMPUSERID");
                if (userId == null)
                    userId = "guest";
                if (userId.equals("guest") || userId.equals("deleted")) {
                    baseResponse = new BaseResponse();
                    baseResponse.setMassage("登录失败，请检查帐号和密码！");
                    responseHandler.onResponseHandled(baseResponse);
                    return;
                }

                updateCurrentAccount(account);
                BBSApplication.setAccount(account);

                requestUserInfoFromServer(account.getId(), account.getPasswd());

                responseHandler.onResponseHandled(new BaseResponse());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                BaseResponse baseResponse = new BaseResponse();
                baseResponse.setResultCode(ResultCode.CONNECT_ERR);
                responseHandler.onResponseHandled(baseResponse);
            }
        });
    }


    private void requestUserInfoFromServer(String userId, String passwd) {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
        form.put("userId", userId);

        mobileService.get("userInfo", form).enqueue(new BBSCallback2(new UserInfoResponseHandler() {
            @Override
            public void onResponseHandled(BaseResponse baseResponse) {
                if (baseResponse instanceof UserInfoResponse) {
                    UserInfo userInfo = ((UserInfoResponse) baseResponse).getUserInfo();
                    updateCurrentAccount(new Account(userId, passwd, userInfo.getAvatar() , Account.FLAG_CURRENT));
                    BBSApplication.setAccount(new Account(userId, passwd, userInfo.getAvatar(), Account.FLAG_CURRENT));
                }
            }
        }));
    }

    public void logout(BaseResponseHandler responseHandler) {
        RootService rootService = ServiceCreator.create(RootService.class);
        rootService.get("bbslogout.php").enqueue(new BBSCallback2(responseHandler));
    }
}
