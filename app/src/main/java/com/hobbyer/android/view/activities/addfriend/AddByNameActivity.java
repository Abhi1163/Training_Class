package com.hobbyer.android.view.activities.addfriend;




import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityAddbyNameBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.addfriendsviewmodel.AddFriendsViewModel;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

public class AddByNameActivity extends BindingActivity<ActivityAddbyNameBinding,AddFriendsViewModel> {

    @Override
    public AddFriendsViewModel onCreate() {
        return new AddFriendsViewModel(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_addby_name;
    }
}
