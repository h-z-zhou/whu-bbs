package com.wuda.bbs.ui.mail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.utils.DensityUtil;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.NetworkEntry;
import com.wuda.bbs.logic.bean.bbs.Attachment;
import com.wuda.bbs.logic.bean.bbs.BriefArticle;
import com.wuda.bbs.logic.bean.bbs.Mail;
import com.wuda.bbs.logic.bean.bbs.MailContent;
import com.wuda.bbs.logic.bean.response.ContentResponse;
import com.wuda.bbs.ui.adapter.AttachmentAdapter;
import com.wuda.bbs.ui.article.DetailArticleActivity;
import com.wuda.bbs.ui.widget.BaseCustomDialog;
import com.wuda.bbs.ui.widget.FullyGridLayoutManager;
import com.wuda.bbs.ui.widget.ResponseErrorHandlerDialog;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.networkResponseHandler.SimpleResponseHandler;

import java.util.List;
import java.util.Map;

public class MailContentActivity extends AppCompatActivity {

    MailContentViewModel mViewModel;

    Mail mail;
    String boxName;

    LinearLayout root_ll;

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
        toolbar.setTitle("信件详情");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        root_ll = findViewById(R.id.mailContent_root_linearLayout);
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

        if (mail.getSubject().contains("回文转寄")) {
            mViewModel.requestReplyArticleUrl();
        }
    }

    private void eventBinding() {
        mViewModel.getMailContentMutableLiveData().observe(this, new Observer<MailContent>() {
            @Override
            public void onChanged(MailContent mailContent) {
                content_tv.setText(mailContent.getContent());
                subject_tv.append(mail.getSubject());
                sender_tv.append(mail.getSender());
                time_tv.append(mail.getTime());

                showAttachments();
            }
        });

        mViewModel.getReplyArticleUrlMutableLivaData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                showAttachments();
            }
        });

        mViewModel.getErrorResponseMutableLiveData().observe(MailContentActivity.this, new Observer<ContentResponse<?>>() {
            @Override
            public void onChanged(ContentResponse<?> contentResponse) {
                new ResponseErrorHandlerDialog(MailContentActivity.this)
                        .addErrorResponse(contentResponse)
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
                if (mail.getSubject().contains("回文转寄")) {
                    String url = mViewModel.getReplyArticleUrlMutableLivaData().getValue();
                    if (url == null) {
                        Toast.makeText(MailContentActivity.this, "未检测到原文地址", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, String> urlParam = NetTool.extractUrlParam(url);
                    Intent intent = new Intent(MailContentActivity.this, DetailArticleActivity.class);
                    BriefArticle briefArticle = new BriefArticle();
                    briefArticle.setBoardID(urlParam.get("board"));
                    briefArticle.setGID(urlParam.get("groupID"));
                    briefArticle.setReID(urlParam.get("reID"));
                    intent.putExtra("briefArticle", briefArticle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MailContentActivity.this, SendMailActivity.class);
                    intent.putExtra("userId", mail.getSender());
                    intent.putExtra("title", "Re: " + mail.getSubject());
                    startActivity(intent);
                }
            }
        });

        delete_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.getMailContentMutableLiveData().getValue() == null) {
                    return;
                }
                Map<String, String> form = NetTool.extractUrlParam(mViewModel.getMailContentMutableLiveData().getValue().getDelUrl());
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

    private void showAttachments() {

        if (mViewModel.getMailContentMutableLiveData().getValue() == null || mViewModel.getReplyArticleUrlMutableLivaData().getValue() == null) {
            return;
        }

        List<Attachment> attachmentList = mViewModel.getMailContentMutableLiveData().getValue().getAttachmentList();
        String url = mViewModel.getReplyArticleUrlMutableLivaData().getValue();

        RecyclerView recyclerView = new RecyclerView(MailContentActivity.this);
        recyclerView.setLayoutManager(new FullyGridLayoutManager(MailContentActivity.this, 3));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3,
                DensityUtil.dip2px(MailContentActivity.this, 4), false));
        Map<String, String> urlParam = NetTool.extractUrlParam(url);
        // http://bbs.whu.edu.cn/wForum/bbsmailcon.php?boxname=inbox&num=1&ap=234
        // 附件地址不统一？ Glide缓存是否重复
        recyclerView.setAdapter(new AttachmentAdapter(MailContentActivity.this, urlParam.get("board"), urlParam.get("reID"), attachmentList));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setLayoutParams(params);
        root_ll.addView(recyclerView);
    }
}