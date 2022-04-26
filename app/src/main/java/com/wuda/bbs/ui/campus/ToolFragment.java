package com.wuda.bbs.ui.campus;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wuda.bbs.R;
import com.wuda.bbs.logic.bean.campus.AnnouncementBean;
import com.wuda.bbs.logic.bean.campus.ToolBean;
import com.wuda.bbs.ui.campus.CampusActivity;
import com.wuda.bbs.ui.campus.detial.AnnouncementDetailFragment;

public class ToolFragment extends Fragment {

    protected ToolBean tool;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolkit_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (tool == null) return true;

        if (item.getItemId() == R.id.info_detail_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, tool.getName() + "  " + tool.getUrl());
            shareIntent.setType("text/plain");
            startActivity(shareIntent);
        } else if (item.getItemId() == R.id.info_detail_open_in_browser) {
            Intent openInBrowserIntent = new Intent(Intent.ACTION_VIEW);
            openInBrowserIntent.setData(Uri.parse(tool.getUrl()));
            startActivity(openInBrowserIntent);
        }

        return true;
    }

    protected void showProgressBar() {
        Activity activity = getActivity();
        if (activity == null)
            return;
        if (activity instanceof CampusActivity) {
            ((CampusActivity) activity).showProgressBar();
        }
    }

    protected void closeProgressBar() {
        Activity activity = getActivity();
        if (activity == null)
            return;
        if (activity instanceof  CampusActivity) {
            ((CampusActivity) activity).hideProgressBar();
        }
    }
}
