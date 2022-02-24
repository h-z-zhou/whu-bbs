package com.wuda.bbs.ui.article;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.DetailArticle;
import com.wuda.bbs.logic.bean.response.BaseResponse;
import com.wuda.bbs.utils.network.NetTool;
import com.wuda.bbs.utils.network.ServiceCreator;
import com.wuda.bbs.utils.network.WebForumService;
import com.wuda.bbs.utils.parser.HtmlParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReplyActivity extends AppCompatActivity {

    private DetailArticle repliedArticle;
    private String boardId;

    ImageView close_btn;
    ImageView send_btn;
    TextInputEditText content_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repliedArticle = (DetailArticle) getIntent().getSerializableExtra("article");
        boardId = getIntent().getStringExtra("boardId");
        if (repliedArticle == null)
            finish();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reply);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        setFinishOnTouchOutside(true);

        close_btn = findViewById(R.id.reply_close_imageButton);
        send_btn = findViewById(R.id.reply_send_imageButton);

        content_et = findViewById(R.id.reply_content_inputEditText);

        // 需要加延时，不然无法弹出
        new Handler().postDelayed(new Runnable(){
            public void run() {
                content_et.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.showSoftInput(content_et, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 100);

        eventBinding();
    }

    private void eventBinding() {
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply();
            }
        });
    }

    private void reply(){
        Editable content = content_et.getText();
        if (content==null) {
            return;
        }

        if (content.length() == 0) {
            new AlertDialog.Builder(ReplyActivity.this)
                    .setTitle("内容不可为空")
                    .setMessage("把你心里的想法告诉我吧！")
                    .setPositiveButton("确定", null)
                    .create()
                    .show();
            return;
        }

        String title = "@" + repliedArticle.getAuthor();

        // board=&relID=0&font=&subject=&Content=&signature=
        Map<String, String> form = new HashMap<>();
        form.put("board", boardId);
        form.put("relID", repliedArticle.getId());
        form.put("font", "");
        form.put("subject", title);
        form.put("Content", content.toString());
        form.put("signature", "");

        Map<String, String> encodedForm = NetTool.encodeUrlFormWithGBK(form);

        WebForumService webForumService = ServiceCreator.create(WebForumService.class);
        webForumService.postWithEncoded("dopostarticle.php", encodedForm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String text = new String(response.body().bytes(), "GBK");
                    BaseResponse baseResponse = HtmlParser.parsePostArticleResponse(text);
                    if (!baseResponse.isSuccessful()) {
                        new AlertDialog.Builder(ReplyActivity.this)
                                .setTitle("出错啦")
                                .setMessage(baseResponse.getMassage())
                                .setPositiveButton("确定", null)
                                .create()
                                .show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}