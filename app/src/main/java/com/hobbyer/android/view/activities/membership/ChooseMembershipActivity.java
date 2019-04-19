package com.hobbyer.android.view.activities.membership;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityChooseMembershipBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.choosemembership.ChooseMembershipViewModel;

public class ChooseMembershipActivity extends BindingActivity<ActivityChooseMembershipBinding, ChooseMembershipViewModel> {



    @Override
    public ChooseMembershipViewModel onCreate() {
        return new ChooseMembershipViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_membership;
    }
}
