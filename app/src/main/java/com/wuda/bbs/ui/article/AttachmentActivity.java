package com.wuda.bbs.ui.article;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.utils.validator.MimeValidator;

public class AttachmentActivity extends AppCompatActivity {

    String url;
    String name;
    MimeValidator.Mime mime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);

        url = getIntent().getStringExtra("url");
        name = getIntent().getStringExtra("name");
        mime = (MimeValidator.Mime) getIntent().getSerializableExtra("mime");

        ImageView face_iv = findViewById(R.id.attachment_face_imageView);
        TextView filename_tv = findViewById(R.id.attachment_filename_textView);
        Button download_btn = findViewById(R.id.attachment_download_button);


        if (mime.type == MimeValidator.Mime.Type.IMAGE) {
            Glide.with(AttachmentActivity.this).load(url).into(face_iv);
        } else {
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(500, 500);
            // 0 => parent id
            params.topToTop = 0;
            params.bottomToBottom = 0;
            params.startToStart = 0;
            params.endToEnd = 0;
            face_iv.setLayoutParams(params);
            Glide.with(AttachmentActivity.this).load(mime.icon).into(face_iv);
            filename_tv.setText(name);
        }

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }
}