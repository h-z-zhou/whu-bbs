package com.wuda.bbs.ui.campus.detial;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.AnnouncementBean;
import com.wuda.bbs.logic.bean.campus.MovieBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.CampusActivity;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.utils.campus.HttpUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.OnClickATagListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AnnouncementDetailFragment extends ToolFragment {

    private AnnouncementBean announcement;
    HtmlTextView headHtmlTextView;
    HtmlTextView bodyHtmlTextView;
    HtmlTextView signatureHtmlTextView;
    HtmlTextView annexHtmlTextView;

    public AnnouncementDetailFragment() {
    }

    public static AnnouncementDetailFragment newInstance(ToolBean announcement) {

        Bundle args = new Bundle();
        args.putSerializable("announcement", announcement);

        AnnouncementDetailFragment fragment = new AnnouncementDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            announcement = (AnnouncementBean) getArguments().getSerializable("announcement");
            tool = announcement;
            showContent();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcement_detail, container, false);

        headHtmlTextView = view.findViewById(R.id.announcementHead_htmlTextView);
        bodyHtmlTextView = view.findViewById(R.id.announcementBody_htmlTextView);
        signatureHtmlTextView = view.findViewById(R.id.announcementSignature_htmlTextView);
        annexHtmlTextView = view.findViewById(R.id.announcementAnnex_htmlTextView);
//        bodyHtmlTextView.setMovementMethod(LinkMovementMethod.getInstance());  // 无效
        bodyHtmlTextView.setOnClickATagListener(new OnClickATagListener() {
            @Override
            public boolean onClick(View widget, String spannedText, @Nullable String href) {
                return false;
            }
        });


        return view;
    }

    private void showContent() {

        String address = announcement.getUrl();

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ((SubToolActivity) getActivity()).closeProgressBar();
                        bodyHtmlTextView.setHtml("<center><b>" +
                                "<p><big> " + getResources().getText(R.string.network_error) +
                                "：</big></p>" +
                                "<p>" + e.getMessage() + "</p>" +
                                "</b></center>"
                        );
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 屏幕翻转时，是否可优化？？？
                List<String> content = parserContent(response);
                String headHtml = content.get(0);
                String bodyHtml = content.get(1);
                String signatureHtml = content.get(2);
                String annexHtml = content.get(3);

                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @SuppressLint("RtlHardcoded")
                    @Override
                    public void run() {
//                        ((SubToolActivity) getActivity()).closeProgressBar();
                        ((CampusActivity) requireActivity()).hideProgressBar();

                        headHtmlTextView.setHtml(headHtml);

                        bodyHtmlTextView.setHtml(bodyHtml, new HtmlHttpImageGetter(bodyHtmlTextView, "https://www.whu.edu.cn", true));

                        if (!signatureHtml.isEmpty()) {
                            signatureHtmlTextView.setHtml(signatureHtml);
                        } else {
                            signatureHtmlTextView.setVisibility(View.GONE);
                        }

                        if (!annexHtml.isEmpty()) {
                            annexHtmlTextView.setHtml(annexHtml);
                            annexHtmlTextView.setOnClickATagListener(new OnClickATagListener() {
                                @Override
                                public boolean onClick(View widget, String spannedText, @Nullable String href) {
                                    return false;
                                }
                            });
                            annexHtmlTextView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    private List<String> parserContent(Response response) {
        List<String> content = new ArrayList<>();
        String headHtml;
        String bodyHtml;
        String signatureHtml = "";
        String annexHtml = "";

        // 标题（本地）
        headHtml = "<div><center>" +
                "<b>" + announcement.title + "</b><br></br>" +
                "<small><i>" + announcement.department + "    |    " + announcement.time  + "</small></i>" +
                "</center></div>";

        Document doc = null;
        try {
            doc = Jsoup.parse(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 包括附件部分
        assert doc != null;
        Elements newsContents = doc.getElementsByAttributeValue("name", "_newscontent_fromname");

        if (newsContents.size() == 0) {
            // 无权访问
            if (doc.getElementsByClass("prompt").size() != 0) {
                bodyHtml = doc.getElementsByClass("prompt").html();
            } else {
                bodyHtml = doc.text();
            }
        } else {

            Element contentElement = newsContents.get(0);

            Element c_contentElement = contentElement.getElementsByClass("c_content").get(0);
            StringBuilder signatureBuilder = new StringBuilder();

            Elements pElements = c_contentElement.getElementsByTag("p");
            if (c_contentElement.attr("id").equals("vsb_content_6")) {
                int size = pElements.size();
                signatureBuilder.append(pElements.get(size - 1).text()).append("<br></br>").append(pElements.get(size - 2).text());
                pElements.get(size - 1).remove();
                pElements.get(size - 2).remove();
            } else if (c_contentElement.attr("id").equals("vsb_content_2")) {
                for (Element element : c_contentElement.getElementsByTag("p")) {
                    if (element.attr("style").equals("text-align: right;")) {
                        signatureBuilder.append(element.text()).append("<br></br>");
                        element.remove();
                    }
                }
            } else {
               headHtml = getResources().getString(R.string.unhandle_exception);
            }

            bodyHtml = c_contentElement.html();
            signatureHtml = signatureBuilder.toString();

            // 附件
            StringBuilder annexBuilder = new StringBuilder();
            Elements attachElements = contentElement.getElementsByClass("attach");
            if (attachElements.size() != 0) {
                Element attachElement = attachElements.get(0);
                for (Element href : attachElement.getElementsByTag("a")) {
                    String link = href.attr("href");
                    if (!link.startsWith("http")) {
                        href.attr("href", "https://www.whu.edu.cn" + link);
                    }
                }
                annexBuilder.append(attachElement.html());
            }
            annexHtml = annexBuilder.toString();
        }
        content.add(headHtml);
        content.add(bodyHtml);
        content.add(signatureHtml);
        content.add(annexHtml);

        return content;
    }
}