package com.wuda.bbs.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.wuda.bbs.bean.response.BaseResponse;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.parser.JsonParser;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPasswordFragment extends Fragment {

    protected void submit(Map<String, String> form) {

        RootService rootService = ServiceCreator.create(RootService.class);
        rootService.post("r/doreset.php", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    ResponseBody body = response.body();
                    if (body == null)
                        return;
                    BaseResponse baseResponse = JsonParser.parseFindPasswordResponse(body.string());

                    if (getActivity() != null && getContext() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getContext())
                                        .setMessage(baseResponse.getMassage())
                                        .setPositiveButton("确定", null)
                                        .create()
                                        .show();
                            }
                        });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });

    }
}
