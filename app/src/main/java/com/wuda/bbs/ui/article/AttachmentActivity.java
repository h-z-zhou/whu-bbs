package com.wuda.bbs.ui.article;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.Attachment;
import com.wuda.bbs.ui.adapter.AttachmentPager2Adapter;

import java.util.List;

public class AttachmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);

        List<Attachment> attachmentList = (List<Attachment>) getIntent().getSerializableExtra("attachments");
        String board = getIntent().getStringExtra("board");
        String articleId = getIntent().getStringExtra("articleId");
        int position = getIntent().getIntExtra("position", 0);

        ViewPager2 attachment_vp2 = findViewById(R.id.attachment_viewPager2);
        attachment_vp2.setAdapter(new AttachmentPager2Adapter(AttachmentActivity.this, attachmentList, board, articleId));
        attachment_vp2.setCurrentItem(position, false);

        attachment_vp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AttachmentActivity.this, Integer.valueOf(attachment_vp2.getCurrentItem()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}