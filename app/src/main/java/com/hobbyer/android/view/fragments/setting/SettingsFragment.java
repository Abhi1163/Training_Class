package com.hobbyer.android.view.fragments.setting;



import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.FragmentSettingsBinding;
import com.hobbyer.android.view.fragments.base.BindingFragment;

import com.hobbyer.android.viewmodel.fragment.settingsviewmodel.SettingsViewModel;

public class SettingsFragment extends BindingFragment<SettingsViewModel,FragmentSettingsBinding> {



    @Override
    protected SettingsViewModel onCreateViewModel(FragmentSettingsBinding binding) {
        return new SettingsViewModel(this,getBinding());
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutResources() {
        return R.layout.fragment_settings;
    }


}