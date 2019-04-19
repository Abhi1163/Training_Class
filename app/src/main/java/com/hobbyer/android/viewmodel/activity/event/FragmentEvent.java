package com.hobbyer.android.viewmodel.activity.event;


import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.FragmentEventBinding;
import com.hobbyer.android.view.fragments.base.BindingFragment;
import com.hobbyer.android.view.fragments.base.FragmentViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEvent extends BindingFragment<EventFragmentViewModel,FragmentEventBinding> {


    public FragmentEvent() {
        // Required empty public constructor
    }





    @Override
    protected EventFragmentViewModel onCreateViewModel(FragmentEventBinding binding) {
        return new EventFragmentViewModel(this,getBinding());
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }
    @Override
    public int getLayoutResources() {
        return R.layout.fragment_event;
    }

}
