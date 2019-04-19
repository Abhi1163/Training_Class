package com.hobbyer.android.view.activities.event;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityReminderBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.event.ReminderViewModel;


public class ReminderActivity extends BindingActivity<ActivityReminderBinding,ReminderViewModel> {


    @Override
    public ReminderViewModel onCreate() {
        return new ReminderViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reminder;
    }
}
