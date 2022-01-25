package com.wuda.bbs.ui.main.mail;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.FriendResponse;
import com.wuda.bbs.bean.MailResponse;
import com.wuda.bbs.ui.adapter.MailAdapter;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MailFragment extends Fragment {

    private MailViewModel mViewModel;
    RecyclerView mail_rv;

    public static MailFragment newInstance() {
        return new MailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mail_fragment, container, false);

        mail_rv = view.findViewById(R.id.recyclerView);
        mail_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mail_rv.setAdapter(new MailAdapter(getContext(), new ArrayList<>()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MailViewModel.class);

        eventBinding();
        requestMailsFromServer();
    }

    private void eventBinding() {
        mViewModel.mailResponse.observe(getViewLifecycleOwner(), new Observer<MailResponse>() {
            @Override
            public void onChanged(MailResponse mailResponse) {
                ((MailAdapter)mail_rv.getAdapter()).appendMails(mailResponse.getMailList());
            }
        });
    }

    private void requestMailsFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
//        int requestPage = mViewModel.articleResponse.getValue().getCurrentPage() + 1;
        form.put("list", "1");
        form.put("boxname", "inbox");
        mobileService.request("mail", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String text = response.body().string();
                        MailResponse mailResponse = XMLParser.parseMails(text);
                        if (mailResponse.isSuccessful()) {
                            mViewModel.mailResponse.postValue(mailResponse);
                        }
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