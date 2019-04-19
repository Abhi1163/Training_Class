package com.hobbyer.android.view.activities.phonenumber;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityPhoneNumberBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.phonenumber.PhoneNumberViewModel;

public class PhoneNumberActivity extends BindingActivity<ActivityPhoneNumberBinding,PhoneNumberViewModel> {


    @Override
    public PhoneNumberViewModel onCreate() {
        return new PhoneNumberViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_number;
    }
}
