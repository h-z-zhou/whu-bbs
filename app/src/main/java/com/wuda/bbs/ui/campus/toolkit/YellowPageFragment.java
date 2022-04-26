package com.wuda.bbs.ui.campus.toolkit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.ContactBean;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.YellowPageAdapter;
import com.wuda.bbs.ui.campus.CampusActivity;
import com.wuda.bbs.ui.campus.ToolFragment;
import com.wuda.bbs.ui.campus.detial.YellowPageDetailFragment;
import com.wuda.bbs.ui.campus.detial.YellowPageUtil;

import java.util.List;


public class YellowPageFragment extends ToolFragment {

    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = view.findViewById(R.id.list);

        List<ContactBean> contactList = YellowPageUtil.getAllContacts();
        YellowPageAdapter adapter = new YellowPageAdapter(getContext(), contactList);
        adapter.setAdapterItemListener(new AdapterItemListener<ContactBean>() {
            @Override
            public void onItemClick(ContactBean data, int position) {
                ((CampusActivity) requireActivity()).navigationTo(
                        YellowPageDetailFragment.newInstance(data),
                        true
                );
            }

            @Override
            public void onItemLongClick(ContactBean data, int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }
}
