package com.hobbyer.android.view.activities.forgotpassword;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityRestPasswordBinding;
import com.hobbyer.android.interfaces.SingUpNavigator;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.viewmodel.activity.restpasswordviewmodel.RestPasswordViewModel;

public class RestPasswordActivity extends BindingActivity<ActivityRestPasswordBinding,RestPasswordViewModel> implements SingUpNavigator {
    @Override
    public RestPasswordViewModel onCreate() {
        return new RestPasswordViewModel(this,this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_rest_password;
    }

    @Override
    public void openLoginActivity() {

    }

    @Override
    public void openHomeActivity() {

    }


}
