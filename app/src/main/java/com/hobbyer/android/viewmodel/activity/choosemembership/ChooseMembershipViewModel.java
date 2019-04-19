package com.hobbyer.android.viewmodel.activity.choosemembership;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanData;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanResponse;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanResult;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.billing.BillingActivity;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.view.activities.membership.ChooseMembershipActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import java.util.List;

import retrofit2.Call;

public class ChooseMembershipViewModel extends ActivityViewModel implements View.OnClickListener {
    private ChooseMembershipActivity activity;
    private String price, points, memebershipType;
    private int memberId, isCurrent;
    private List<ManagePlanData> managePlanDataList;
    private String memberShipType,membershipFree;
    private int id;
    private String expDate;


    public ChooseMembershipViewModel(ChooseMembershipActivity activity) {
        super(activity);
        this.activity = activity;
        setToolbar();
        getPlanApi();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.llFreeTrial:



                Bundle bundle = new Bundle();

                bundle.putString(AppConstant.BUNDLE_KEY.FREE, "free");
                bundle.putInt(AppConstant.BUNDLE_KEY.Membership_ID,memberId);
                bundle.putString(AppConstant.BUNDLE_KEY.Membership_TYPE,memebershipType);
                SharedPref.saveStringPreferences(activity,AppConstant.CHOSSEMEMBERSHIP,"free");


                // SharedPref.saveStringPreferences(activity,AppConstant.CHOSSEMEMBERSHIP,"free");
                ActivityController.startActivity(activity, BillingActivity.class, bundle, false);
             //   SharedPref.saveStringPreferences(activity,AppConstant.CHOSSEMEMBERSHIP,"withoutfree");




              //  confirmplanApi();

                break;
            case R.id.llStart:


                Bundle bundle1 = new Bundle();
                bundle1.putString(AppConstant.BUNDLE_KEY.OTP, "otp");
                SharedPref.saveStringPreferences(activity,AppConstant.CHOSSEMEMBERSHIP,"withoutfree");


                ActivityController.startActivity(activity, BillingActivity.class, bundle1, false);



                //  activity.startActivity(new Intent(activity, BillingActivity.class));

                break;

        }
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
            jsonObject.addProperty("membership_id", id);

            AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
            authWebServices.updateMembership(jsonObject).enqueue(new BaseCallback<BaseResponse>(activity) {
                @Override
                public void onSuccess(BaseResponse response) {
                    if (response != null) {

                        if (response.getStatus() == 1) {
                            ActivityController.startActivity(activity, HomeActivity.class);

                            ToastUtils.showToastShort(activity, response.getMessage());

                        } else {


                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstant.BUNDLE_KEY.FREE, "free");

                             // SharedPref.saveStringPreferences(activity,AppConstant.CHOSSEMEMBERSHIP,"free");
                            ActivityController.startActivity(activity, BillingActivity.class, bundle, false);

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


    private void getPlanApi() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                long userId = userModel.getId();
                int userid = (int) userId;
                AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
                authWebServices.getplan(userid).enqueue(new BaseCallback<ManagePlanResponse>(activity) {
                    @Override
                    public void onSuccess(ManagePlanResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                expDate =  response.getResult().getDate();

                                /*ManagePlanResult managePlanResult = new ManagePlanResult();*/


                                PrefManager.savePreferences(activity, AppConstant.DATE, expDate);

                                ManagePlanResult managePlanResult = response.getResult();

                              //  expDate = managePlanResult.getDate();

                              //  PrefManager.savePreferences(activity, AppConstant.DATE, expDate);

                                managePlanDataList = managePlanResult.getContentList();
                                for (int i = 0; i < managePlanDataList.size(); i++) {

                                    memberId=managePlanDataList.get(0).getId();
                                    memebershipType=managePlanDataList.get(0).getMembershipType();

                                }




                                    /*if (managePlanDataList.get(i).getMembershipType().equalsIgnoreCase("free")) {

                                        memberShipType = managePlanDataList.get(i).getMembershipType();
                                        id = managePlanDataList.get(i).getId();
                                        SharedPref.saveIntPreferences(activity,AppConstant.MEMBERSHIP_ID,id);
                                        SharedPref.saveIntPreferences(activity,AppConstant.MEMBERSHIP_TYPE,id);

                                    }
                                    else {
                                        memberShipType = managePlanDataList.get(i).getMembershipType();

                                    }

                                //    id = managePlanDataList.get(i).getId();

                                }


                                SharedPref.saveStringPreferences(activity,AppConstant.CHOSSEMEMBERSHIP,"free");
                                SharedPref.saveStringPreferences(activity, AppConstant.MEMBERSHIP_TYPE, memberShipType);*/


                            }

                        }
                        ProgressDialogUtils.dismiss();


                    }

                    @Override
                    public void onFail(Call<ManagePlanResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastShort(activity, baseResponse.getMessage());

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }


    }

    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.GONE);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.choose_member);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }

    }
}
