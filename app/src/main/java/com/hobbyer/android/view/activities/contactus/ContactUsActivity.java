package com.hobbyer.android.view.activities.contactus;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityContactUsBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.contactusviewmodel.ContactUsViewModel;

public class ContactUsActivity extends BindingActivity<ActivityContactUsBinding,ContactUsViewModel> {
    @Override
    public ContactUsViewModel onCreate() {
        return new ContactUsViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_contact_us;
    }
}
