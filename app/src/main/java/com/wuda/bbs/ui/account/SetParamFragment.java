package com.wuda.bbs.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.base.BaseFragment;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetParamFragment extends BaseFragment {

    private SetParamViewModel mViewModel;

    Switch[] switches;
    private boolean[] choices;
    private String[] paramNames;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SetParamViewModel.class);

        mViewModel.getUserParamMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Boolean>>() {
            @Override
            public void onChanged(List<Boolean> userParam) {
                for (int i=0; i<userParam.size(); i++) {
                    switches[i].setChecked(userParam.get(i));
                }
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(getContext())
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestChoicesFromServer();
                            }
                        })
                        .show();
            }
        });

        showActionBar("隐私参数");

        mViewModel.requestChoicesFromServer();
    }

    private void submit() {
        if (mViewModel.getUserParamMutableLiveData().getValue() == null)
            return;
        // user_define_6=1&user_define_15=1&user_define_16=1&user_define_17=1&user_define_29=1&user_define_30=0&user_define1_0=1&mailbox_prop_0=1&mailbox_prop_1=0&Submit=%B8%FC+%D0%C2
        Map<String, String> form = new HashMap<>();
        for (int i=0; i<choices.length; i++) {
            form.put(paramNames[i], choices[i]? "1": "0");
        }
        form.put("Submit", "");

        mViewModel.setParam(form);
    }
}