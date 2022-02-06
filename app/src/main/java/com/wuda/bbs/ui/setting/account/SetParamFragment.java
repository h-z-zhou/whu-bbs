package com.wuda.bbs.ui.setting.account;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.response.UserParamResponse;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetParamFragment extends Fragment {

    private SetParamViewModel mViewModel;

    Switch[] switches;
    private boolean[] choices;
    private String[] paramNames;
    boolean initialed = false;

    public static SetParamFragment newInstance() {
        return new SetParamFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.set_param_fragment, container, false);

        int[] switchIds = new int[]{
                R.id.param_friendCall_switch,
                R.id.param_receiveAllMsg_switch,
                R.id.param_receiveFriendMsg_switch,
                R.id.param_sound_switch,
                R.id.param_detailInfo_switch,
                R.id.param_realInfo_switch,
                R.id.param_hideIP_switch,
                R.id.param_toSendBox_switch,
                R.id.param_toDustbin_switch
        };


        switches = new Switch[switchIds.length];
        choices = new boolean[switchIds.length];
        paramNames = new String[]{
                "user_define_6",
                "user_define_15",
                "user_define_16",
                "user_define_17",
                "user_define_29",
                "user_define_30",
                "user_define1_0",
                "mailbox_prop_0",
                "mailbox_prop_1",
                "Submit"
        };


        for (int i = 0; i< switchIds.length; i++) {
            switches[i] = view.findViewById(switchIds[i]);
            int finalI = i;
            switches[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    choices[finalI] = isChecked;
                    submit();
                }
            });
        }

        requestChoicesFromServer();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SetParamViewModel.class);
    }

    private void submit() {
        if (!initialed)
            return;

        // user_define_6=1&user_define_15=1&user_define_16=1&user_define_17=1&user_define_29=1&user_define_30=0&user_define1_0=1&mailbox_prop_0=1&mailbox_prop_1=0&Submit=%B8%FC+%D0%C2
        Map<String, String> form = new HashMap<>();
        for (int i=0; i<choices.length; i++) {
            form.put(paramNames[i], choices[i]? "1": "0");
        }
        form.put("Submit", "");

        RootService rootService = ServiceCreator.create(RootService.class);
        rootService.post("wForum/saveuserparam.php", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }

    private void requestChoicesFromServer() {
        // bug: 不能检测到登出
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("请求中");
        progressDialog.show();

        RootService rootService = ServiceCreator.create(RootService.class);
        rootService.get("wForum/userparam.php").enqueue(new BBSCallback<ResponseBody>(getContext()) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                    UserParamResponse userParamResponse = HtmlParser.parseUserParamResponse(text);
                    if (userParamResponse.isSuccessful()) {
                        if (getActivity()!=null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<Boolean> userParam = userParamResponse.getParamValues();
                                    for (int i=0; i<userParam.size(); i++) {
                                        switches[i].setChecked(userParam.get(i));
                                    }
                                    initialed = true;
                                }
                            });
                        }
                    }
                    progressDialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}