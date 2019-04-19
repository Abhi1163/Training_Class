package com.hobbyer.android.view.activities.forgotpassword;

import com.android.databinding.library.baseAdapters.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityForgotPasswordBinding;
import com.hobbyer.android.interfaces.ForgotPasswordNavigator;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.view.activities.otpscreen.OtpActivity;
import com.hobbyer.android.viewmodel.activity.forgotpasswordviewmodel.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends BindingActivity<ActivityForgotPasswordBinding,ForgotPasswordViewModel> implements ForgotPasswordNavigator {
    @Override
    public ForgotPasswordViewModel onCreate() {
        return new ForgotPasswordViewModel(this,this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    /*@Override
    public void openOTPActivity() {
        ActivityController.startActivity(this,OtpActivity.class);
    }*/
}
