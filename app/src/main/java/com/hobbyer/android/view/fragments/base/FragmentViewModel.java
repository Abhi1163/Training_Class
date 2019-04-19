package com.hobbyer.android.view.fragments.base;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hobbyer.android.view.fragments.setting.SettingsFragment;

/**
 * Created by ashutosh on 20/3/18.
 */

public abstract class FragmentViewModel<F extends BindingFragment, B extends ViewDataBinding>
        extends BaseObservable {

    private F fragment;
    private B binding;
    private Activity activity;

    public FragmentViewModel(F fragment) {
        this.fragment = fragment;
        this.binding = binding;
        this.activity = this.fragment.getActivity();
        initialize(binding);
    }



    protected abstract void initialize(B binding);

    public F getFragment() {
        return fragment;
    }

    public Fragment getParentFragment() {
        return fragment.getParentFragment();
    }

    public B getBinding() {
        return binding;
    }

    public Activity getActivity() {
        return activity;
    }

    public void updateBinding(B binding) {
        this.binding = binding;
        initialize(binding);
    }

    /**
     * Fragment lifecycle
     */
    public void onViewCreated() {

    }

    public void onDestroy() {

    }

    public boolean setUserVisibleHint(boolean isVisibleToUser) {
        return  isVisibleToUser;
    }

    public void onPause() {

    }

    public void onResume() {

    }

    public void onDestroyView() {
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        return true;
    }

}