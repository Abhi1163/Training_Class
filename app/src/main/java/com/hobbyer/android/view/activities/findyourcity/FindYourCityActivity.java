package com.hobbyer.android.view.activities.findyourcity;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityFindYourCityBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.view.activities.citysearch.ActivityCitySearch;
import com.hobbyer.android.viewmodel.activity.findyourcityviewmodel.FindYourCityViewModel;
import com.hobbyer.android.interfaces.FindCityNavigator;

public class FindYourCityActivity extends BindingActivity<ActivityFindYourCityBinding, FindYourCityViewModel> implements FindCityNavigator {

    @Override
    public FindYourCityViewModel onCreate() {
        return new FindYourCityViewModel(this,this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_find_your_city;
    }

    @Override
    public void openLoginActivity() {

    }




}





