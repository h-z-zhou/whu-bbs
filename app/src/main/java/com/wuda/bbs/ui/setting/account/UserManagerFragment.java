package com.wuda.bbs.ui.setting.account;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wuda.bbs.R;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.RootService;
import com.wuda.bbs.utils.network.ServiceCreator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UserManagerFragment extends Fragment {

    private UserManagerViewModel mViewModel;

    private Button logout_btn;

    public static UserManagerFragment newInstance() {
        return new UserManagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_manager_fragment, container, false);

        logout_btn = view.findViewById(R.id.userManager_logout_button);

        eventBinding();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(UserManagerViewModel.class);
    }

    private void eventBinding() {
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        RootService rootService = ServiceCreator.create(RootService.class);
        rootService.get("bbslogout.php").enqueue(new BBSCallback<ResponseBody>(getContext()) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

            }

            @Override
            public void setLogoutHandler(LogoutHandler logoutHandler) {
                super.setLogoutHandler(logoutHandler);
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });
    }
}