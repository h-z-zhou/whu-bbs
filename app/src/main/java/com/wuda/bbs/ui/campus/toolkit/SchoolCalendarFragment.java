package com.wuda.bbs.ui.campus.toolkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.SchoolCalendar;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.utils.campus.HttpUtil;
import com.wuda.bbs.utils.campus.InfoResponseParser;
import com.wuda.bbs.utils.campus.ServerURL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SchoolCalendarFragment extends ToolFragment {

    private WebView schoolCalendarWebView;

    public static SchoolCalendarFragment newInstance(ToolBean schoolCalendar) {
        SchoolCalendarFragment fragment = new SchoolCalendarFragment();
        Bundle args = new Bundle();
        args.putSerializable("schoolCalendar", schoolCalendar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_school_calendar, container, false);


        schoolCalendarWebView = view.findViewById(R.id.schoolCalendar_webView);
        // 180固定值？？？
//        schoolCalendarWebView.setInitialScale(180);
        WebSettings webSettings = schoolCalendarWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
//        webSettings.seLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        showCalendar();

        FloatingActionButton add_fab = view.findViewById(R.id.schoolCalendar_floatingActionButton);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestIndexFromServer(false);
            }
        });

        return view;
    }


    private void showCalendar() {

        if (getActivity() != null) {

            @SuppressLint("CommitPrefEdits") SharedPreferences pref = getActivity()
                    .getSharedPreferences("school_calendar", Context.MODE_PRIVATE);

            String calendarHtml = pref.getString("calendar_html", "");
            if (calendarHtml.isEmpty()) {
                requestIndexFromServer(true);
                return;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    schoolCalendarWebView.loadData(calendarHtml.toString(), "text/html", "UTF-8");
                }
            });
        }
    }

    private void requestIndexFromServer(boolean firstIdx) {
        showProgressBar();
        HttpUtil.sendOkHttpRequest(ServerURL.CALENDAR, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressBar();
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                List<SchoolCalendar> schoolCalendarList = InfoResponseParser.handleSchoolCalendarResponse(response.body().string());
                final String[] items = new String[schoolCalendarList.size()];
                for (int i=0; i<schoolCalendarList.size(); i++) {
                    items[i] = schoolCalendarList.get(i).getName();
                }

                if (firstIdx) {
                    // 首次启动，默认第一个
                    requestFromServer(schoolCalendarList.get(0));
                    return;
                }

                final Integer[] choice = {-1};

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            closeProgressBar();

                            if (getContext() == null)
                                return;
                            new AlertDialog.Builder(getContext())
                                    .setTitle("请选择校历")
                                    .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            choice[0] = i;
                                        }
                                    })
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (choice[0] != -1) {
                                                requestFromServer(schoolCalendarList.get(choice[0]));
                                            }
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    });

                }
            }
        });

    }


    private void requestFromServer(SchoolCalendar schoolCalendar) {

        showProgressBar();

        HttpUtil.sendOkHttpRequest(schoolCalendar.getUrl(), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity()!=null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressBar();
                        }
                    });
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(!isAdded())
                    return;

                StringBuilder calendarHtml = new StringBuilder();
                calendarHtml.append("<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                            "<meta name=\"viewport\" content=\"width=device-width\">" +
                        "</head>" +
                        "<body>");
                Element calendarTable = Jsoup.parse(response.body().string()).getElementById("vsb_content");
                if (calendarTable == null) {
                    calendarHtml.append(getResources().getString(R.string.no_calendar));
                } else {

                    for (Element table: calendarTable.getElementsByTag("table")) {
                        // 两个 table 宽度不一样 570 - 580
                        table.attr("width", "580");
                    }

                    for (Element imgTag : calendarTable.getElementsByTag("img")) {
                        String imgSrc = imgTag.attr("src");
                        imgTag.attr("src", "https://uc.whu.edu.cn/" + imgSrc);
                    }
                    calendarHtml.append(calendarTable.html());
                }

                calendarHtml.append("</body></html>");

                if (getActivity() != null) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressBar();
                        }
                    });

                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = getActivity()
                            .getSharedPreferences("school_calendar", Context.MODE_PRIVATE).edit();

                    editor.putString("calendar_html", calendarHtml.toString());

                    editor.apply();

                    showCalendar();
                }

            }
        });
    }
}