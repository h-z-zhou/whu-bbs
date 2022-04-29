package com.wuda.bbs.ui.campus.detial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.ContactBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.ui.campus.toolkit.YellowPageFragment;
import com.wuda.bbs.utils.campus.ServerURL;


public class YellowPageDetailFragment extends ToolFragment {

//    private YellowPageBean yellowPage;
    private ContactBean contact;
    private TextView titleTextView;
    private TextView phoneNumberTextView;
    private Button callButton;

    public static YellowPageDetailFragment newInstance(ToolBean info) {
        YellowPageDetailFragment fragment = new YellowPageDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("contact", info);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contact = (ContactBean) getArguments().getSerializable("contact");
            tool = new ToolBean(R.drawable.ic_yellow_page, "#dacd5e", "电话本", ServerURL.YELLOW_PAGES, YellowPageFragment.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_yellow_page_detail, container, false);

        titleTextView = view.findViewById(R.id.yellowPage_title_textView);
        phoneNumberTextView = view.findViewById(R.id.yellowPage_phoneNumber_textView);
        callButton = view.findViewById(R.id.yellowPage_call_button);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                startActivity(intent);
            }
        });

        showContent();

        return view;
    }

    private void showContent() {
        titleTextView.setText(contact.getName());
        phoneNumberTextView.setText(contact.getPhoneNumber());
        if (getActivity() != null) {
//            ((SubToolActivity) getActivity()).closeProgressBar();
        }
    }
}