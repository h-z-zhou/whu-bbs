package com.wuda.bbs.ui.mail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.Mail;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.utils.networkResponseHandler.MailContentHandler;

import java.util.HashMap;
import java.util.Map;

public class MailContentActivity extends AppCompatActivity {

    MailContentViewModel mViewModel;

    Mail mail;
    String boxName;

    TextView subject_tv;
    TextView sender_tv;
    TextView time_tv;
    TextView content_tv;
    ImageView reply_iv;
    ImageView delete_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_content);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subject_tv = findViewById(R.id.mailContent_subject_textView);
        sender_tv = findViewById(R.id.mailContent_sender_textView);
        time_tv = findViewById(R.id.mailContent_time_textView);
        content_tv = findViewById(R.id.mailContent_content_textView);
        reply_iv = findViewById(R.id.mailContent_reply_imageView);
        delete_iv = findViewById(R.id.mailContent_delete_imageView);

        mViewModel = new ViewModelProvider(this).get(MailContentViewModel.class);

        mail = (Mail) getIntent().getSerializableExtra("mail");
        boxName = getIntent().getStringExtra("boxName");

        eventBinding();

        requestMailContentFromServer();
    }

    private void eventBinding() {
        mViewModel.mailContent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                content_tv.setText(s);
                subject_tv.append(mail.getSubject());
                sender_tv.append(mail.getSender());
                time_tv.append(mail.getTime());
            }
        });

        reply_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MailContentActivity.this, NewMailActivity.class);
                intent.putExtra("userId", mail.getSender());
                intent.putExtra("title", "Re: " + mail.getSubject());
                startActivity(intent);
            }
        });
    }

    private void requestMailContentFromServer() {
        Map<String, String> form = new HashMap<>();
        form.put("read", mail.getNum());
        form.put("boxname", boxName);

        NetworkEntry.requestMailContent(form, new MailContentHandler() {
            @Override
            public void onResponseHandled(ContentResponse<String> response) {
                mViewModel.mailContent.postValue(response.getContent());
            }
        });
    }
}