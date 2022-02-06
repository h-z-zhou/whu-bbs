package com.wuda.bbs.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;

public class TopicDecoration extends RecyclerView.ItemDecoration {

    private int contentHeight;
    private int replyHeight;
    private int replyIndentWidth;
    private Paint contentPaint;
    private Paint replyPaint;

    public TopicDecoration(Context mContext) {

        contentPaint = new Paint();
        contentPaint.setColor(Color.LTGRAY);
        replyPaint = new Paint();
        replyPaint.setColor(Color.LTGRAY);

        contentHeight = mContext.getResources().getDimensionPixelSize(R.dimen.topic_decoration_height_content);
        replyHeight = mContext.getResources().getDimensionPixelSize(R.dimen.topic_decoration_height_reply);

        replyIndentWidth = mContext.getResources().getDimensionPixelSize(R.dimen.topic_decoration_replyIndentWidth);

    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int recyclerViewLeft = parent.getLeft();
        int recyclerViewRight = parent.getRight();

        int childCount = parent.getChildCount();
        for (int i = 1; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int pos = parent.getChildAdapterPosition(child);

            if (pos == 1) {
//                float left = child.getLeft();
//                float right = child.getRight();
                float top = child.getTop() + contentHeight;
                float bottom = child.getTop();
                c.drawRect(recyclerViewLeft, top, recyclerViewRight, bottom, contentPaint);
            } else {
//                float left = child.getLeft();
//                float right = child.getRight();
                float top = child.getTop() + replyHeight;
                float bottom = child.getTop();
                c.drawRect(replyIndentWidth, top, recyclerViewRight, bottom, replyPaint);
            }
        }
    }
}
