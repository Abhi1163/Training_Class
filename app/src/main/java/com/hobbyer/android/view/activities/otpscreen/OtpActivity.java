package com.hobbyer.android.view.activities.otpscreen;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.OtpActivityBinding;
import com.hobbyer.android.interfaces.RestPassword;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.view.activities.forgotpassword.RestPasswordActivity;
import com.hobbyer.android.viewmodel.activity.otpviewmodel.OtpViewModel;

public class OtpActivity extends BindingActivity<OtpActivityBinding,OtpViewModel> implements RestPassword {

    private PrefManager prefManager;
    @Override
    public OtpViewModel onCreate() {
        return new OtpViewModel(this,this);
    }

    @Override
    public int getVariable() {
        return  BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.otp_activity;
    }


    @Override
    public void openRestActivity() {
        ActivityController.startActivity(this,RestPasswordActivity.class);
    }
}
