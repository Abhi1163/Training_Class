package com.hobbyer.android.view.activities.signup;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;

import com.hobbyer.android.databinding.ActivitySignupBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.interfaces.SingUpNavigator;
import com.hobbyer.android.viewmodel.activity.singupviewmodel.SignUpViewModel;

public class SignUpActivity extends BindingActivity<ActivitySignupBinding,SignUpViewModel>implements SingUpNavigator {

    @Override
    public SignUpViewModel onCreate() {
        return new SignUpViewModel(this,this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    public void openLoginActivity() {

    }

    @Override
    public void openHomeActivity() {

    }

   /* @Override
    public void openFreeTrailActivity() {
        ActivityController.startActivity(this, ActivityFreeTrial.class);
    }*/
}
