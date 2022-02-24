package com.wuda.bbs.ui.base;

import androidx.fragment.app.Fragment;

public interface NavigationHost {
    void navigationTo(Fragment fragment, boolean addToBackstack);
}
