package com.hobbyer.android.view.fragments.profile;



import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.FragmentProfileBinding;

import com.hobbyer.android.view.fragments.base.BindingFragment;
import com.hobbyer.android.viewmodel.fragment.profileviewmodel.ProfileFragmentViewModel;


public class ProfileFragment extends BindingFragment<ProfileFragmentViewModel,FragmentProfileBinding> {
    public ProfileFragmentViewModel viewModel;
    @Override
    protected ProfileFragmentViewModel onCreateViewModel(FragmentProfileBinding binding) {
        viewModel= new ProfileFragmentViewModel(this,getBinding());
        return  viewModel;
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutResources() {
        return R.layout.fragment_profile;
    }
}








