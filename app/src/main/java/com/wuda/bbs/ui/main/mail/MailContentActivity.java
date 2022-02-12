package com.wuda.bbs.ui.main.mail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wuda.bbs.R;
import com.wuda.bbs.bean.Mail;
import com.wuda.bbs.bean.response.MailContentResponse;
import com.wuda.bbs.utils.network.MobileService;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.xmlHandler.XMLParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MailContentActivity extends AppCompatActivity {

    MailContentViewModel mViewModel;

    Mail mail;

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
        subject_tv = findViewById(R.id.mailContent_subject_textView);
        sender_tv = findViewById(R.id.mailContent_sender_textView);
        time_tv = findViewById(R.id.mailContent_time_textView);
        content_tv = findViewById(R.id.mailContent_content_textView);
        reply_iv = findViewById(R.id.mailContent_reply_imageView);
        delete_iv = findViewById(R.id.mailContent_delete_imageView);

        mViewModel = new ViewModelProvider(this).get(MailContentViewModel.class);

        mail = (Mail) getIntent().getSerializableExtra("mail");

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
                Toast.makeText(MailContentActivity.this, "reply", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestMailContentFromServer() {
        MobileService mobileService = ServiceCreator.create(MobileService.class);
        Map<String, String> form = new HashMap<>();
        form.put("read", mail.getNum());
        form.put("boxname", "inbox");
        mobileService.get("mail", form).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String text = response.body().string();
                        MailContentResponse mailContentResponse = XMLParser.parseMailContent(text);
                        if (mailContentResponse.isSuccessful()) {
                            mViewModel.mailContent.postValue(mailContentResponse.getMailContent());
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