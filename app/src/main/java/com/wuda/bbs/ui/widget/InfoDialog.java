package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class InfoDialog extends BaseCustomDialog{
    private final TextView info_tv;

    public InfoDialog(Context context) {
        super(context);
        info_tv = new TextView(context);
        this.content_view = info_tv;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.negative_btn.setVisibility(View.GONE);
        this.positive_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        refreshView();
    }

    public InfoDialog setInfo(String info){
        info_tv.setText(info);
        return this;
    }
}
