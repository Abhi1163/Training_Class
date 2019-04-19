package com.hobbyer.android.view.activities.event;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityEventBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.hobbyer.android.viewmodel.activity.event.EventViewModel;

public class EventActivity extends BindingActivity<ActivityEventBinding,EventViewModel> {
    private ActivityEventBinding binding;


    @Override
    public EventViewModel onCreate() {
        return new EventViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_event;
    }
}
