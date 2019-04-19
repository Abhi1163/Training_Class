package com.hobbyer.android.view.activities.privacysetting;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.databinding.library.baseAdapters.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityPrivacySettingsBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.privacysettingviewmodel.PrivacySettingViewModel;


public class PrivacySettings extends BindingActivity<ActivityPrivacySettingsBinding,PrivacySettingViewModel> {
    private ActivityPrivacySettingsBinding binding;



    @Override
    public PrivacySettingViewModel onCreate() {
        return new PrivacySettingViewModel(this);
    }


    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_privacy_settings;
    }
}
