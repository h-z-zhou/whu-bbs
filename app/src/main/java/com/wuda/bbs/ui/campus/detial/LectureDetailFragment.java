package com.wuda.bbs.ui.campus.detial;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.LectureBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.ToolFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LectureDetailFragment extends ToolFragment {

    private LectureBean lecture;

    private TextView title_textView;
    private TextView reporter_textView;
    private TextView time_textView;
    private TextView position_textView;
    private TextView organizer_textView;
    private TextView reporterIntroduction_textView;
    private TextView content_textView;
    private ImageView qrCode_imageView;
    private ImageView poster_imageView;

    public static LectureDetailFragment newInstance(ToolBean lecture) {
        LectureDetailFragment fragment = new LectureDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("lecture", lecture);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lecture = (LectureBean) getArguments().getSerializable("lecture");
            tool = lecture;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lecture_detail, container, false);

        title_textView = view.findViewById(R.id.lecture_title_textView);
        reporter_textView = view.findViewById(R.id.lecture_reporter_textView);
        time_textView = view.findViewById(R.id.lecture_time_textView);
        position_textView = view.findViewById(R.id.lecture_position_textView);
        organizer_textView = view.findViewById(R.id.lecture_organizer_textView);
        reporterIntroduction_textView = view.findViewById(R.id.lecture_repoter_introduction_textView);
        content_textView = view.findViewById(R.id.lecutre_content_textView);
        qrCode_imageView = view.findViewById(R.id.lecture_qrCode_imageView);
        poster_imageView = view.findViewById(R.id.lecture_poster_imageView);

        show();

//        if(getActivity()!=null)
//            ((SubToolActivity) getActivity()).closeProgressBar();

        return view;
    }

    private void show() {
        if (lecture == null)
            return;

        title_textView.setText(lecture.getTitle());
        reporter_textView.setText(lecture.getReporter());
        time_textView.setText(this.getLectureTime());
        position_textView.setText(lecture.getPosition());
        organizer_textView.setText(lecture.getOrganizer());
        reporterIntroduction_textView.setText(lecture.getIntroduction());
        content_textView.setText(lecture.getContent());

        String qrCodeUrl = lecture.getLive_qrcode();
        if(!qrCodeUrl.isEmpty())
            Glide.with(this).load(qrCodeUrl).into(qrCode_imageView);

        String posterUrl = lecture.getPoster();
        if(!posterUrl.isEmpty())
            Glide.with(this).load(posterUrl).into(poster_imageView);

    }

    private String getLectureTime() {
        StringBuilder time = new StringBuilder();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date date = new Date(lecture.getStart_time());
        time.append(sdf.format(date));

        if (lecture.getEnd_time() != 0) {
            time.append(" - ");
            date.setTime(lecture.getEnd_time());
            time.append(sdf.format(lecture.getEnd_time()));
        }

        return time.toString();
    }
}