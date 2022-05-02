package com.wuda.bbs.ui.article;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.Attachment;
import com.wuda.bbs.ui.adapter.AttachmentPager2Adapter;
import com.wuda.bbs.ui.base.CustomizedThemeActivity;
import com.wuda.bbs.utils.network.NetConst;

import java.util.List;

public class AttachmentActivity extends CustomizedThemeActivity {

    List<Attachment> attachmentList;
    ViewPager2 attachment_vp2;

    String board;
    String articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        attachmentList = (List<Attachment>) getIntent().getSerializableExtra("attachments");
        board = getIntent().getStringExtra("board");
        articleId = getIntent().getStringExtra("articleId");
        int position = getIntent().getIntExtra("position", 0);


        setContentView(R.layout.activity_attachment);

        Toolbar toolbar = findViewById(R.id.back_toolbar);
        toolbar.setTitle(position+1 + "/" + attachmentList.size());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        attachment_vp2 = findViewById(R.id.attachment_viewPager2);
        attachment_vp2.setAdapter(new AttachmentPager2Adapter(AttachmentActivity.this, attachmentList, board, articleId));
        attachment_vp2.setCurrentItem(position, false);

        attachment_vp2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                toolbar.setTitle(position+1 + "/" + attachmentList.size());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.preview_attachment_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_preview_attachment_download) {
            Attachment attachment = attachmentList.get(attachment_vp2.getCurrentItem());
            String url = NetConst.ATTACHMENT + "?board=" + board + "&id=" + articleId + "&ap=" + attachment.getId();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}