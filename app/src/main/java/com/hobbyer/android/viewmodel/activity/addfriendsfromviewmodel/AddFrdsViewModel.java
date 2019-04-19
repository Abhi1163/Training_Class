package com.hobbyer.android.viewmodel.activity.addfriendsfromviewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.view.activities.addfriend.AddByNameActivity;
import com.hobbyer.android.view.activities.addfriendfrom.AddFriendActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

public class AddFrdsViewModel extends ActivityViewModel<AddFriendActivity> {

    private AddFriendActivity activity;

    private ImageView imBack;
    private String socialId;
    private int isFbUser=1;

    public AddFrdsViewModel(AddFriendActivity activity) {
        super(activity);

        this.activity = activity;


        UserModel userModel = PreferenceUtils.getUserModel();
        if (userModel == null) {
            return;
        }
        socialId= userModel.getSocialType();
        if (socialId==null)
        {
            activity.getBinding().llFacbook.setVisibility(View.GONE);
            activity.getBinding().viewFb.setVisibility(View.GONE);

        }
        else
            {
            activity.getBinding().llFacbook.setVisibility(View.VISIBLE);
            activity.getBinding().viewFb.setVisibility(View.VISIBLE);

        }

        imBack = (ImageView) activity.findViewById(R.id.imBack);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mAddByName:
                Intent home = new Intent(activity, AddByNameActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(home);
                break;


            case R.id.llFacbook:
                Bundle bundle=new Bundle();
                bundle.putInt("isFrom",isFbUser);
                ActivityController.startActivity(activity,AddByNameActivity.class,bundle);

                break;

        }
    }

}
