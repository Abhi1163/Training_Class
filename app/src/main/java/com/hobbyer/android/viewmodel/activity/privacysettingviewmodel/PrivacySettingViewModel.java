package com.hobbyer.android.viewmodel.activity.privacysettingviewmodel;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.GetPrivacysSettingRequest;
import com.hobbyer.android.api.request.PrivacySettingRequest;
import com.hobbyer.android.api.request.ReferEarnRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.getprivacysetting.GetPrivacySettingData;
import com.hobbyer.android.api.response.auth.getprivacysetting.GetPrivacySettingResponse;
import com.hobbyer.android.api.response.auth.referearn.ReferEarnData;
import com.hobbyer.android.api.response.auth.referearn.ReferEarnResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.privacysetting.PrivacySettings;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import retrofit2.Call;

public class PrivacySettingViewModel extends ActivityViewModel implements View.OnClickListener {
    private PrivacySettings activity;
    private int flag;
    private int flagUpcoming;
    private int flagFav;
   private boolean flags;
    private boolean flagUpcomings;
    private boolean flagFavs;
    public PrivacySettingViewModel(PrivacySettings activity) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        getPrivacySettingConnection();
    }
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.ivToggle:
                if (flag == 0) {
                    activity.getBinding().ivToggle.setImageResource(R.mipmap.ic_off);
                    flag = 1;
                    privacySettingConnection();
                } else if (flag == 1) {
                    activity.getBinding().ivToggle.setImageResource(R.mipmap.ic_on);
                    flag = 0;
                    privacySettingConnection();
                }
                break;
            case R.id.ivUpcoming:
                if (flagUpcoming == 1) {
                    activity.getBinding().ivUpcoming.setImageResource(R.mipmap.ic_off);
                    flagUpcoming = 0;
                    privacySettingConnection();
                } else if (flagUpcoming == 0) {
                    activity.getBinding().ivUpcoming.setImageResource(R.mipmap.ic_on);
                    flagUpcoming = 1;
                    privacySettingConnection();
                }
                break;
            case R.id.ivFourite:
                if (flagFav == 1) {
                    activity.getBinding().ivFourite.setImageResource(R.mipmap.ic_off);
                    flagFav = 0;
                    privacySettingConnection();
                } else if (flagFav == 0) {
                    activity.getBinding().ivFourite.setImageResource(R.mipmap.ic_on);
                    flagFav = 1;
                    privacySettingConnection();
                }
                break;

        }
    }
    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.privacy_setting);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }

    private void getPrivacySettingConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                GetPrivacysSettingRequest contactUs = new GetPrivacysSettingRequest();
                contactUs.setUser_id("" + userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.get_Privacy_Setting(contactUs).enqueue(new BaseCallback<GetPrivacySettingResponse>((AppCompatActivity) activity) {
                    @Override
                    public void onSuccess(GetPrivacySettingResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                GetPrivacySettingData getPrivacyData = response.getResult().getData();
                                if (getPrivacyData.getPastClass() == 0) {
                                    activity.getBinding().ivToggle.setImageResource(R.mipmap.ic_off);
                                    flag = 0;
                                } else if (getPrivacyData.getPastClass() == 1) {
                                    flag = 1;
                                    activity.getBinding().ivToggle.setImageResource(R.mipmap.ic_on);
                                }
                                if (getPrivacyData.getUpcomingClass() == 0) {
                                    flagUpcoming = 0;
                                    activity.getBinding().ivUpcoming.setImageResource(R.mipmap.ic_off);
                                } else if (getPrivacyData.getUpcomingClass() == 1) {
                                    flagUpcoming = 1;
                                    activity.getBinding().ivUpcoming.setImageResource(R.mipmap.ic_on);
                                }
                                if (getPrivacyData.getFavouriteClass() == 0) {
                                    flagFav = 0;
                                    activity.getBinding().ivFourite.setImageResource(R.mipmap.ic_off);
                                } else if (getPrivacyData.getUpcomingClass() == 1) {
                                    flagFav = 1;
                                    activity.getBinding().ivFourite.setImageResource(R.mipmap.ic_on);
                                }
                                //flagFav=getPrivacyData.getFavouriteClass();
                            } else {

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<GetPrivacySettingResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(activity, "" + baseResponse.getMessage());
                    }

                });


            } catch (Exception e) {

            }
        }else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }
    private void privacySettingConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                if (flag==0){
                    flags=false;
                }else if (flag==1){
                    flags=true;
                }
                if (flagUpcoming==0){
                    flagUpcomings=false;
                }else if (flagUpcoming==1){
                    flagUpcomings=true;
                }
                if (flagFav==0){
                    flagFavs=false;
                }else if (flagFav==1){
                    flagFavs=true;
                }
                PrivacySettingRequest contactUs = new PrivacySettingRequest();
                contactUs.setUser_id("" + userModel.getId());
                contactUs.setPast_class(flags);
                contactUs.setUpcoming_class(flagUpcomings);
                contactUs.setFavourite_studio(flagFavs);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.privacy_Setting(contactUs).enqueue(new BaseCallback<BaseResponse>(activity) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                getPrivacySettingConnection();

                            } else {
                                getPrivacySettingConnection();
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(activity, "" + baseResponse.getMessage());
                    }

                });

            } catch (Exception e) {

            }
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.ivBack:
                finish();

                break;
        }

    }
}
