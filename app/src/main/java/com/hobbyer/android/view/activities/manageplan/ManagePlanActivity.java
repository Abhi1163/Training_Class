package com.hobbyer.android.view.activities.manageplan;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityManagePlanBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.manageplanviewmodel.ManagePlanViewModel;


public class ManagePlanActivity extends BindingActivity<ActivityManagePlanBinding,ManagePlanViewModel> {



    @Override
    public ManagePlanViewModel onCreate() {
        return new ManagePlanViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_manage_plan;
    }
}
