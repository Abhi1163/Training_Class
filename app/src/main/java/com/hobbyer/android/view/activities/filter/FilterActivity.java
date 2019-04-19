package com.hobbyer.android.view.activities.filter;

import android.widget.ImageView;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityFilterBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.filterviewmodel.FilterViewModel;

public class FilterActivity extends BindingActivity<ActivityFilterBinding,FilterViewModel> {
    ImageView ivActing , ivArt ,ivBeauty, ivBusiness, ivFood, ivFashion, ivFitness, ivLanguage,ivMusic,ivTech;


    @Override
    public FilterViewModel onCreate() {
        return new FilterViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_filter;
    }


}
