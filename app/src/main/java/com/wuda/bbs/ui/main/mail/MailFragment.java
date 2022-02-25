package com.wuda.bbs.ui.main.mail;

import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.response.MailResponse;
import com.wuda.bbs.ui.adapter.MailAdapter;
import com.wuda.bbs.ui.main.MainActivity;
import com.wuda.bbs.utils.network.BBSCallback;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class MailFragment extends Fragment {

    private MailViewModel mViewModel;

    private TextView toolbar_tv;
    RecyclerView mail_rv;
    MailAdapter adapter;

    public static MailFragment newInstance() {
        return new MailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mail_fragment, container, false);

        setHasOptionsMenu(true);

        mail_rv = view.findViewById(R.id.recyclerView);


        if (getActivity() != null) {
            Toolbar toolbar = ((MainActivity) getActivity()).getToolbar();
            for (int i=0; i<toolbar.getChildCount(); i++) {
                View childView = toolbar.getChildAt(i);
                if (childView instanceof TextView) {
                    toolbar_tv = (TextView) childView;
                    Drawable arrow = getContext().getDrawable(R.drawable.ic_arrow_drop_down);
                    arrow.setBounds(0, 0, arrow.getMinimumWidth(), arrow.getMinimumHeight());
                    toolbar_tv.setCompoundDrawables(null, null, arrow, null);
                    break;
                }
            }
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MailViewModel.class);
        adapter = new MailAdapter(getContext(), new ArrayList<>(), mViewModel.box.getValue().first);
        mail_rv.setAdapter(adapter);
        mail_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        eventBinding();
//        requestMailsFromServer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toolbar_tv.setCompoundDrawables(null, null, null, null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_mail_new) {
            Intent intent = new Intent(getContext(), NewMailActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventBinding() {
        mViewModel.mailResponse.observe(getViewLifecycleOwner(), new Observer<MailResponse>() {
            @Override
            public void onChanged(MailResponse mailResponse) {
                int currentPage = mailResponse.getCurrentPage();
                int totalPage = mailResponse.getTotalPage();
                if (currentPage != 0 && currentPage==totalPage) {
                    adapter.setMore(false);
                }
//                if (briefArticleResponse.getCurrentPage() == briefArticleResponse.getTotalPage()-1) {
//                    adapter.setMore(false);
//                }
                if (currentPage == 1) {
                    adapter.setContents(mailResponse.getMailList());
                } else {
                    adapter.appendContents(mailResponse.getMailList());
                }
//                adapter.appendContents(mailResponse.getMailList());
            }
        });

        if (toolbar_tv != null) {
            toolbar_tv.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View v) {
                    if (getContext() == null) return;

                    PopupMenu popupMenu = new PopupMenu(getContext(), toolbar_tv);
                    popupMenu.getMenuInflater().inflate(R.menu.mailbox_menu, popupMenu.getMenu());
                    try {
                        Field field = popupMenu.getClass().getDeclaredField("mPopup");
                        field.setAccessible(true);
                        MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
                        mHelper.setForceShowIcon(true);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.getItemId() == R.id.menu_mailbox_inbox) {
                                mViewModel.box.setValue(new Pair<>("inbox", "收信箱"));
                            } else if (item.getItemId() == R.id.menu_mailbox_send_box) {
                                mViewModel.box.setValue(new Pair<>("sendbox", "发信箱"));
                            } else if (item.getItemId() == R.id.menu_mailbox_delete_box) {
                                mViewModel.box.setValue(new Pair<>("deleted", "废信箱"));
                            }
                            return false;
                        }
                    });

                    popupMenu.show();
                }
            });

            mViewModel.box.observe(getViewLifecycleOwner(), new Observer<Pair<String, String>>() {
                @Override
                public void onChanged(Pair<String, String> box) {
                    toolbar_tv.setText(box.second);
                    adapter.changeBox(box.first);
                    requestMailsFromServer();

                }
            });
        }
    }

    private void requestMailsFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
        form.put("list", "1");
        form.put("boxname", mViewModel.box.getValue().first);


        mobileService.get("mail", form).enqueue(new BBSCallback<ResponseBody>(getContext()) {
            @Override
            public void onResponseWithoutLogout(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
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
        });
    }
}