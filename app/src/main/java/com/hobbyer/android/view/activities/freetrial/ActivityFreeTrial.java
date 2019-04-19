package com.hobbyer.android.view.activities.freetrial;

import com.android.databinding.library.baseAdapters.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.freetrailviewmodel.ViewModelFreeTrail;
import com.hobbyer.android.databinding.ActivityFreeTrialBinding;
public class ActivityFreeTrial extends BindingActivity<ActivityFreeTrialBinding,ViewModelFreeTrail> {
    @Override
    public ViewModelFreeTrail onCreate() {
        return new ViewModelFreeTrail(this);
    }

    @Override
    public int getVariable() {
         return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_free_trial;
    }
}
