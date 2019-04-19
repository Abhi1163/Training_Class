package com.hobbyer.android.view.activities.termandcondition;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityTermsAndConditionsBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.termandconditionviewmodel.TermsAndConditionsViewModel;


public class TermsAndConditionsActivity extends BindingActivity<ActivityTermsAndConditionsBinding, TermsAndConditionsViewModel> {
    @Override
    public TermsAndConditionsViewModel onCreate() {
        return new TermsAndConditionsViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_terms_and_conditions;
    }
}
