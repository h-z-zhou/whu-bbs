package com.wuda.bbs.ui.account;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseFragment;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.networkResponseHandler.SettingParamHandler;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetParamFragment extends BaseFragment {

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

        showActionBar("隐私参数");
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

        NetworkEntry.setSettingParam(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                String text = response.getContent().getResult();
                Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestChoicesFromServer() {
        // bug: 不能检测到登出
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("请求中");
        progressDialog.show();

        NetworkEntry.requestSettingParam(new SettingParamHandler() {
            @Override
            public void onResponseHandled(ContentResponse<List<Boolean>> response) {
                if (response.isSuccessful()) {
                    if (getActivity()!=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                List<Boolean> userParam = response.getContent();
                                for (int i=0; i<userParam.size(); i++) {
                                    switches[i].setChecked(userParam.get(i));
                                }
                                initialed = true;
                            }
                        });
                    }
                }
                progressDialog.dismiss();
            }
        });

    }
}