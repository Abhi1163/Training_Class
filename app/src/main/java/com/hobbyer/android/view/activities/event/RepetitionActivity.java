package com.hobbyer.android.view.activities.event;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityRepetitionBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.event.ReptitionViewModel;


public class RepetitionActivity extends BindingActivity<ActivityRepetitionBinding,ReptitionViewModel> {



    @Override
    public ReptitionViewModel onCreate() {
        return new ReptitionViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repetition;
    }
}
