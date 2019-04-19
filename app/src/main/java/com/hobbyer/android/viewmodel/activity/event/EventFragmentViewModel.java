package com.hobbyer.android.viewmodel.activity.event;

import android.databinding.ViewDataBinding;
import android.view.View;

import com.hobbyer.android.R;
import com.hobbyer.android.databinding.FragmentEventBinding;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.event.ReminderActivity;
import com.hobbyer.android.view.activities.event.RepetitionActivity;
import com.hobbyer.android.view.fragments.base.FragmentViewModel;


public class EventFragmentViewModel extends FragmentViewModel {
    private  FragmentEvent fragment;

    public EventFragmentViewModel(FragmentEvent fragment, FragmentEventBinding binding) {
        super(fragment);
        this.fragment=fragment;
    }


    @Override
    protected void initialize(ViewDataBinding binding) {

    }
    public  void onClickView(View v)
    {
        switch (v.getId())
        {
            case R.id.llFrom:
                break;
            case R.id.llReminder:
                ActivityController.startActivity(fragment.getActivity(),ReminderActivity.class);

                break;
            case R.id.llTo:
                break;
            case R.id.llRepetition:
                ActivityController.startActivity(fragment.getActivity(),RepetitionActivity.class);
                break;



        }
    }
}
