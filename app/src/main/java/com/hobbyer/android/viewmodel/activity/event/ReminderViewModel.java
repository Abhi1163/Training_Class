package com.hobbyer.android.viewmodel.activity.event;

import android.os.Build;

import com.hobbyer.android.R;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.view.activities.event.ReminderActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

public class ReminderViewModel extends ActivityViewModel {
    private ReminderActivity activity;

    public ReminderViewModel(ReminderActivity activity) {
        super(activity);
        this.activity=activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
    }
}
