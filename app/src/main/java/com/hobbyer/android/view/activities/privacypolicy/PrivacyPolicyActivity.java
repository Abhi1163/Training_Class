package com.hobbyer.android.view.activities.privacypolicy;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityPrivacyPolicyBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.privacypolicyviewmodel.PrivacyPolicyViewModel;


public class PrivacyPolicyActivity extends BindingActivity<ActivityPrivacyPolicyBinding,PrivacyPolicyViewModel> {
    @Override
    public PrivacyPolicyViewModel onCreate() {
        return new PrivacyPolicyViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy_policy;
    }
}
