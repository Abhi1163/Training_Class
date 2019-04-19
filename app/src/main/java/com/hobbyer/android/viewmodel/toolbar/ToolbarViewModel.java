package com.hobbyer.android.viewmodel.toolbar;

import android.app.Activity;

import com.hobbyer.android.utils.CommonUtils;

public class ToolbarViewModel {
    private Activity activity;

    public void onBackClick() {

            activity.finish();

    }


}
