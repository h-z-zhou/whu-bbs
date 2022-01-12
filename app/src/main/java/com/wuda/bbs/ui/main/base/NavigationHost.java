package com.wuda.bbs.ui.main.base;

import androidx.fragment.app.Fragment;

public interface NavigationHost {
    void navigationTo(Fragment fragment, boolean addToBackstack);
}
