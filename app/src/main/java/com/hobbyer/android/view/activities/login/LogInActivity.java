package com.hobbyer.android.view.activities.login;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;

import com.hobbyer.android.databinding.ActivityLoginBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.view.activities.findyourcity.FindYourCityActivity;
import com.hobbyer.android.view.activities.forgotpassword.ForgotPasswordActivity;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.interfaces.LogInNavigator;
import com.hobbyer.android.viewmodel.activity.loginviewmodel.LogInViewModel;

public class LogInActivity extends BindingActivity<ActivityLoginBinding, LogInViewModel> implements LogInNavigator {

    @Override
    public LogInViewModel onCreate() {
        return new LogInViewModel(this,this);
    }
    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void openLoginActivity() {

    }


}