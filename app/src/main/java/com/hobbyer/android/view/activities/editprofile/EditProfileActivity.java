package com.hobbyer.android.view.activities.editprofile;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.databinding.ActivityEditProfileBinding;
import com.hobbyer.android.view.activities.base.BindingActivity;
import com.hobbyer.android.viewmodel.activity.editprofileviewmodel.EditProfileViewModel;

import java.util.Calendar;


public class EditProfileActivity extends BindingActivity<ActivityEditProfileBinding,EditProfileViewModel> {


    @Override
    public EditProfileViewModel onCreate() {
        return new EditProfileViewModel(this);
    }
    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_profile;
    }


}
