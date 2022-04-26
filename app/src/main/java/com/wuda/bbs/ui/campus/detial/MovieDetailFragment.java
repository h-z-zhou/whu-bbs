package com.wuda.bbs.ui.campus.detial;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.MovieBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.utils.campus.HttpUtil;
import com.wuda.bbs.utils.campus.InfoResponseParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MovieDetailFragment extends ToolFragment {

    private MovieBean movie;
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView timeTextView;
    private TextView placeTextView;
    private TextView typeTextView;
    private TextView actorsTextView;
    private TextView storylineTextView;


    public static MovieDetailFragment newInstance(ToolBean info) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("movie", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie = (MovieBean) getArguments().getSerializable("movie");
            tool = movie;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        posterImageView = view.findViewById(R.id.movie_poster_imageView);
        titleTextView = view.findViewById(R.id.movie_title_textView);
        timeTextView = view.findViewById(R.id.movie_time_textView);
        placeTextView = view.findViewById(R.id.movie_place_textView);
        typeTextView = view.findViewById(R.id.movie_type_textView);
        actorsTextView = view.findViewById(R.id.movie_actors_textView);
        storylineTextView = view.findViewById(R.id.movie_storyline_textView);

        showContent();

//        if (getActivity() != null) {
//            ((SubToolActivity) getActivity()).closeProgressBar();
//        }

        showStoryline();

        return view;
    }

    private void showContent() {
        Glide.with(this).load(movie.getPoster()).into(posterImageView);

        titleTextView.append(movie.getTitle());
        timeTextView.append(movie.getTime());
        placeTextView.append(movie.getPlace());
        typeTextView.append(movie.getType());
        actorsTextView.append(movie.getActors());
//        storylineTextView.append();
    }

    private void showStoryline() {
        String address = "http://gh.whu.edu.cn/!gh/index/~query/Q_LOAD_SUB_MENU_DATA_DETAIL?" +
                "code_table=BI_SCHOOL_CULTURE&" +
                "id=" + movie.getId() +
                "&__resultType=json";

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response.body().string());
                    String storylineContent = InfoResponseParser.utf8Converter(responseJson.getJSONObject("body").getString("content"));
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //  有 &nbsp
                                storylineTextView.append(Html.fromHtml(storylineContent));
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}