package com.wuda.bbs.ui.campus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.adapter.AdapterItemListener;
import com.wuda.bbs.ui.adapter.ToolkitRecyclerViewAdapter;


public class ToolkitFragment extends Fragment {

   RecyclerView recyclerView;

    public static ToolkitFragment newInstance() {
        return new ToolkitFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.list);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ToolkitRecyclerViewAdapter adapter = new ToolkitRecyclerViewAdapter(getContext(), ToolProvider.getAllTools());
        adapter.setAdapterItemListener(new AdapterItemListener<ToolBean>() {
            @Override
            public void onItemClick(ToolBean data, int position) {
                try {
                    ((CampusActivity) requireActivity()).navigationTo(
                            (Fragment) data.getTargetFragmentClz().newInstance(),
                            true
                    );
                } catch (IllegalAccessException | java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onItemLongClick(ToolBean data, int position) {

            }
        });
        recyclerView.setAdapter(adapter);

        return recyclerView;
    }
}