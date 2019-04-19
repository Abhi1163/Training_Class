package com.hobbyer.android.view.activities.intro;


import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityIntroseriesBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.intro.IntroSeriesActivityViewModel;

public class IntroSeriesActivity extends BindingActivity<ActivityIntroseriesBinding, IntroSeriesActivityViewModel> {
    @Override
    public IntroSeriesActivityViewModel onCreate() {
        return new IntroSeriesActivityViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_introseries;
    }
}
