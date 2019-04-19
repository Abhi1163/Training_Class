package com.hobbyer.android.view.activities.addpoint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.databinding.library.baseAdapters.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityAddPointsBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.addpontviewmodel.AddPointsViewModel;


public class AddPointsActivity extends BindingActivity<ActivityAddPointsBinding,AddPointsViewModel>
{


        @Override
        public AddPointsViewModel onCreate() {
            return new AddPointsViewModel(this);
        }


    @Override
    public int getVariable() {
        return BR.viewModel;
    }

        @Override
        public int getLayoutId() {
            return R.layout.activity_add_points;
        }

}
