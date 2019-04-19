package com.hobbyer.android.viewmodel.activity.freetrailviewmodel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.billing.BillingActivity;
import com.hobbyer.android.view.activities.freetrial.ActivityFreeTrial;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import retrofit2.Call;

import static com.hobbyer.android.constant.AppConstant.BUNDLE_KEY.SettingBill;

public class ViewModelFreeTrail extends ActivityViewModel<ActivityFreeTrial> implements View.OnClickListener {
    private String bundleFree, from, membershipType;
    private int membershipId;

    public ViewModelFreeTrail(ActivityFreeTrial activity) {
        super(activity);
        this.activity = activity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        getIntent();
        //  membershipId= SharedPref.getIntPreferences(activity,AppConstant.MEMBERSHIP_ID);


    }

    private void getIntent() {
        Bundle i = activity.getIntent().getExtras();
         membershipId = i.getInt((AppConstant.MEMBERSHIP_ID));


        }


    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText("Free Trial");
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);

    }

    private void confirmplanApi() {

        if (CommonUtils.isOnline(activity)) {
            ProgressDialogUtils.show(activity);
            UserModel userModel = PreferenceUtils.getUserModel();
            if (userModel == null) {
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", userModel.getId());
            jsonObject.addProperty("membership_id", membershipId);

            AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
            authWebServices.updateMembership(jsonObject).enqueue(new BaseCallback<BaseResponse>(activity) {
                @Override
                public void onSuccess(BaseResponse response) {
                    if (response != null) {

                        if (response.getStatus() == 1) {


                            /*ManagePlanResult managePlanResult = new ManagePlanResult();*/

                            ActivityController.startActivity(activity, HomeActivity.class);

                        } else {

                            ToastUtils.showToastShort(activity, response.getMessage());



                            // SharedPref.saveStringPreferences(activity,AppConstant.CHOSSEMEMBERSHIP,"free");

                            //ActivityController.startActivity(activity,BillingActivity.class);
                        }
                    }
                    ProgressDialogUtils.dismiss();
                }

                @Override
                public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                    ProgressDialogUtils.dismiss();
                    ToastUtils.showToastShort(activity, baseResponse.getMessage());


                }
            });


        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();

        }
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btFree:
                confirmplanApi();
           /*     Bundle bundle=new Bundle();
                bundle.putString(AppConstant.BUNDLE_KEY.FREE,"free");
                ActivityController.startActivity(activity,HomeActivity.class,bundle,true);*/
                /*  activity.finish();*/
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(activity, HomeActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(home);
        activity.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                Intent home = new Intent(activity, HomeActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(home);
                activity.finish();
                break;
        }
    }
}
