package com.hobbyer.android.view.activities.mybooking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityMyBookingBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.mybookingviewmodel.MyBookingViewModel;

public class ActivityMyBooking extends BindingActivity<ActivityMyBookingBinding,MyBookingViewModel> {




    @Override
    public MyBookingViewModel onCreate() {
        return new MyBookingViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_booking;
    }
}
