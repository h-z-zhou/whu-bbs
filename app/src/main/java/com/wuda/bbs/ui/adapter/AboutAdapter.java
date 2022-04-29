package com.wuda.bbs.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.BuildConfig;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.bbs.OpenSourceProject;
import com.wuda.bbs.utils.OpenSourceProvider;

import java.util.List;

public class AboutAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;

    int TYPE_HEADER = 0, TYPE_SIMPLE_TEXT = 1, TYPE_OPEN_SOURCE = 2;
    List<OpenSourceProject> openSourceProjectList;

    public AboutAdapter(Context context) {
        mContext = context;
        openSourceProjectList = OpenSourceProvider.getUsedOpenSourceProjects();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1) {
            return TYPE_SIMPLE_TEXT;
        } else {
            return TYPE_OPEN_SOURCE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        View view;

        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.about_header_item, parent, false);
            holder = new BBSInfoHeader(view);
        } else if (viewType == TYPE_SIMPLE_TEXT) {
            view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setPadding(0, 32, 0, 32);
            ((TextView) view).setTextSize(18);
            holder = new SimpleTextInfo(view);
        }
        else {
            view = new TextView(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setPadding(0, 32, 0, 32);
            ((TextView) view).setTextSize(18);
            holder = new OpenSourceInfo(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenSourceProject project = openSourceProjectList.get(holder.getAbsoluteAdapterPosition()-2);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(project.getUrl()));
                    mContext.startActivity(intent);
                }
            });
        }

        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BBSInfoHeader) {
            ((BBSInfoHeader) holder).version_tv.setText("当前版本：" + BuildConfig.VERSION_NAME);
        } else if (holder instanceof SimpleTextInfo) {
            ((SimpleTextInfo) holder).simpleText_tv.setText("部分图标来源于：iconfont.cn");
        } else if (holder instanceof OpenSourceInfo) {
            ((OpenSourceInfo) holder).openSource_tv.setText(buildOpenSourceProjectItem(openSourceProjectList.get(position-2)));
        }
    }

    @Override
    public int getItemCount() {
        return 2 + openSourceProjectList.size();
    }

    private SpannableStringBuilder buildOpenSourceProjectItem(OpenSourceProject project) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        SpannableString str;

        str = new SpannableString(project.getName() + " - " + project.getContributor());
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str);

        builder.append("\n");
        str = new SpannableString(project.getUrl());
        builder.append(str);

        builder.append("\n");
        str = new SpannableString(project.getLicence());
        builder.append(str);

        return builder;
    }

    static class BBSInfoHeader extends RecyclerView.ViewHolder {
        TextView version_tv;
        public BBSInfoHeader(@NonNull View itemView) {
            super(itemView);
            version_tv = itemView.findViewById(R.id.about_version_textView);
        }
    }

    static class SimpleTextInfo extends RecyclerView.ViewHolder {
        TextView simpleText_tv;
        public SimpleTextInfo(@NonNull View itemView) {
            super(itemView);
            simpleText_tv = (TextView) itemView;
        }
    }

    static class OpenSourceInfo extends RecyclerView.ViewHolder {
        TextView openSource_tv;
        public OpenSourceInfo(@NonNull View itemView) {
            super(itemView);
            openSource_tv = (TextView) itemView;
        }
    }
}
