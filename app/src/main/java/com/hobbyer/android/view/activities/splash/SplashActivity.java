package com.hobbyer.android.view.activities.splash;

import com.crashlytics.android.Crashlytics;
import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivitySplashBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.interfaces.SplashNavigator;
import com.hobbyer.android.view.activities.tutorial.TutorialActivity;
import com.hobbyer.android.viewmodel.activity.splashviewmodel.SplashViewModel;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends BindingActivity<ActivitySplashBinding, SplashViewModel> implements SplashNavigator {
    @Override
    public SplashViewModel onCreate() {

        return new SplashViewModel(this, this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void openFindYourCityActivity() {
        ActivityController.startActivity(this, TutorialActivity.class);
    }

    @Override
    public void openHomeActivity() {

    }


}
