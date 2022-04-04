package com.wuda.bbs.ui.mail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.MainActivity;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.MailAdapter;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MailFragment extends Fragment {

    private MailViewModel mViewModel;

    private TextView toolbar_tv;
    RecyclerView mail_rv;
    MailAdapter adapter;

    ActivityResultLauncher<Intent> mailContentActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            boolean deleted = data.getBooleanExtra("deleted", false);
                            if (deleted) {
                                adapter.deleteMail(mViewModel.selectedPosition);
                            }
                        }
                    }
                }
            });


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
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable arrow = getActivity().getDrawable(R.drawable.ic_arrow_drop_down);
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
        adapter.setAdapterListener(new AdapterItemListener<Mail>() {
            @Override
            public void onItemClick(Mail data, int position) {
                Intent intent = new Intent(getContext(), MailContentActivity.class);
                intent.putExtra("mail", data);
                intent.putExtra("boxName", mViewModel.box.getValue().first);
                mViewModel.selectedPosition = position;
                mailContentActivityLauncher.launch(intent);
            }

            @Override
            public void onItemLongClick(Mail data, int position) {

            }
        });
        mail_rv.setAdapter(adapter);
        mail_rv.setLayoutManager(new LinearLayoutManager(getContext()));

        eventBinding();
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
            Intent intent = new Intent(getContext(), SendMailActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventBinding() {

        mViewModel.getErrorResponseMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(getContext())
                        .addErrorResponse(contentResponse)
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestMailsFromServer();
                            }
                        })
                        .show();
            }
        });

        mViewModel.getMailListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Mail>>() {
            @Override
            public void onChanged(List<Mail> mailList) {
                if (mViewModel.currentPage!=0 && mViewModel.totalPage>= mViewModel.currentPage) {
                    adapter.setMore(false);
                    Collections.reverse(mailList);
                    if (mViewModel.currentPage == 1) {
                        adapter.setContents(mailList);
                    } else {
                        adapter.appendContents(mailList);
                    }
                }
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
                        if (mHelper != null) {
                            mHelper.setForceShowIcon(true);
                        }
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
                    mViewModel.requestMailsFromServer();
                }
            });
        }
    }

}