package com.wuda.bbs.ui.mail;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Friend;
import com.wuda.bbs.logic.bean.WebResult;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.logic.dao.AppDatabase;
import com.wuda.bbs.logic.dao.FriendDao;
import com.wuda.bbs.utils.networkResponseHandler.WebResultHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewMailFragment extends Fragment {

    private SendMailViewModel mSendMailViewModel;

    TextInputEditText userId_et;
    TextInputEditText title_et;
    TextInputEditText content_et;

    TextView friend_tv;


    public static NewMailFragment newInstance() {
        return new NewMailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_mail_fragment, container, false);



        userId_et = view.findViewById(R.id.newMail_userId_editText);
        title_et = view.findViewById(R.id.newMail_title_editText);
        content_et = view.findViewById(R.id.newMail_content_editText);


        setHasOptionsMenu(true);

//        String userId = getIntent().getStringExtra("userId");
//        String title = getIntent().getStringExtra("title");

//        if (userId != null) {
//            userId_et.setText(userId);
//        }
//
//        if (title != null) {
//            title_et.setText(title);
//        }

        friend_tv= view.findViewById(R.id.newMail_friend_textView);


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSendMailViewModel = new ViewModelProvider(getActivity()).get(SendMailViewModel.class);

        if (mSendMailViewModel.userId != null) {
            userId_et.setText(mSendMailViewModel.userId);
        }

        if (mSendMailViewModel.title != null) {
            title_et.setText(mSendMailViewModel.title);
        }

        eventBinding();

    }

    private void eventBinding() {
        mSendMailViewModel.userIdmLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                userId_et.setText(s);
            }
        });

        friend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FriendDao friendDao = AppDatabase.getDatabase(getContext()).getFriendDao();
//                List<Friend> friendList = friendDao.loadAllFriends();
//                String[] ids = new String[friendList.size()];
//                for (int i=0; i<friendList.size(); i++) {
//                    ids[i] = friendList.get(i).getId();
//                }
//
//                new AlertDialog.Builder(getContext())
//                        .setTitle("请选择好友")
//                        .setItems(ids, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                userId_et.setText(ids[which]);
//                            }
//                        })
//                        .create()
//                        .show();
                if (getActivity() instanceof SendMailActivity) {
                    ((SendMailActivity) getActivity()).navigationTo(new SelectFriendFragment(), true);
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.new_mail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_mail_send) {
            sendMail();
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMail() {

        Editable userId = userId_et.getText();
        if (userId==null || userId.length()==0) {
            Toast.makeText(getContext(), "收件人为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Editable title = title_et.getText();
        if (title==null || title.length()==0) {
            Toast.makeText(getContext(), "标题为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Editable content = content_et.getText();
        if (content==null || content.length()==0) {
            Toast.makeText(getContext(), "内容为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> form = new HashMap<>();
        form.put("destid", userId.toString());
        form.put("title", title.toString());
        form.put("content", content.toString());
        form.put("signature", "0");
        form.put("backup", "on");

        NetworkEntry.sendMail(form, new WebResultHandler() {
            @Override
            public void onResponseHandled(ContentResponse<WebResult> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), response.getContent().getResult(), Toast.LENGTH_SHORT).show();
                    if (getActivity() != null)
                        getActivity().onBackPressed();
                }
            }
        });

    }
}