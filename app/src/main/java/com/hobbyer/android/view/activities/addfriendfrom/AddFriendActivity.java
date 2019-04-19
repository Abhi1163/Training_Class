package com.hobbyer.android.view.activities.addfriendfrom;


import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityAddFriendsBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.addfriendsfromviewmodel.AddFrdsViewModel;

public class AddFriendActivity extends BindingActivity<ActivityAddFriendsBinding, AddFrdsViewModel> {

    @Override
    public AddFrdsViewModel onCreate() {
        return new AddFrdsViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_friends;
    }
}
