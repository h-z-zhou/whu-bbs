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
import com.wuda.bbs.logic.bean.MailContent;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

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

        mail = (Mail) getIntent().getSerializableExtra("mail");
        boxName = getIntent().getStringExtra("boxName");

        mViewModel = new ViewModelProvider(this).get(MailContentViewModel.class);
        mViewModel.mail = mail;
        mViewModel.boxName = boxName;

        eventBinding();
        mViewModel.requestMailContentFromWeb();
    }

    private void eventBinding() {
        mViewModel.getMailContent().observe(this, new Observer<MailContent>() {
            @Override
            public void onChanged(MailContent mailContent) {
                content_tv.setText(mailContent.getContent());
                subject_tv.append(mail.getSubject());
                sender_tv.append(mail.getSender());
                time_tv.append(mail.getTime());
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(MailContentActivity.this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(MailContentActivity.this)
                        .addErrorMsg(contentResponse.getResultCode(), contentResponse.getMassage())
                        .setOnRetryButtonClickedListener(new BaseCustomDialog.OnButtonClickListener() {
                            @Override
                            public void onButtonClick() {
                                mViewModel.requestMailContentFromWeb();
                            }
                        })
                        .show();
            }
        });

        reply_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MailContentActivity.this, SendMailActivity.class);
                intent.putExtra("userId", mail.getSender());
                intent.putExtra("title", "Re: " + mail.getSubject());
                startActivity(intent);
            }
        });

        delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.getMailContent().getValue() == null) {
                    return;
                }
                Map<String, String> form = NetTool.extractUrlParam(mViewModel.getMailContent().getValue().getDelUrl());
                NetworkEntry.deleteMail(form, new SimpleResponseHandler() {
                    @Override
                    public void onResponseHandled(ContentResponse<Object> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent();
                            intent.putExtra("deleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}