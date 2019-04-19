package com.hobbyer.android.viewmodel.activity.splashviewmodel;

import android.os.Build;
import android.os.Handler;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hobbyer.android.R;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.interfaces.SplashNavigator;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.LogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.splash.SplashActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

public class SplashViewModel extends ActivityViewModel<SplashActivity> {

    private static int SPLASH_TIME_OUT = 3000;
    private SplashNavigator splashNavigator;
    private PrefManager prefManager;
    private String refreshedToken;


    public SplashViewModel(SplashActivity activity, SplashNavigator splashNavigator) {
        super(activity);
        this.splashNavigator = splashNavigator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        getDeviceToken();

    }

    private void decideNextActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            splashNavigator.openFindYourCityActivity();
            finish();
        }, SPLASH_TIME_OUT);

    }

/*
    public void getDeviceToken(){


            decideNextActivity();


    }*/

    private void getDeviceToken() {
        if (prefManager.getPreferencesString(activity, AppConstant.DEVICE_TOKEN) != null && prefManager.getPreferencesString(activity, AppConstant.DEVICE_TOKEN).toString().trim().length() > 0) {
            decideNextActivity();
        } else {
            if (CommonUtils.isOnline(activity)) {
                try {
                    refreshedToken = FirebaseInstanceId.getInstance().getToken();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (prefManager.getPreferencesString(activity, AppConstant.DEVICE_TOKEN) != null && !prefManager.getPreferencesString(activity, AppConstant.DEVICE_TOKEN).toString().trim().isEmpty()) {
                            decideNextActivity();

                        } else {
                            if (refreshedToken != null) {
                                prefManager.savePreferences(activity, AppConstant.DEVICE_TOKEN, refreshedToken);
                                if (refreshedToken.trim().length() > 0) {
                                    decideNextActivity();
                                } else {
                                    getDeviceToken();
                                }
                            } else {
                                getDeviceToken();
                            }
                        }
                    }
                }, SPLASH_TIME_OUT);
            } else {

                ToastUtils.showToastShort(activity, AppConstant.CHECK_CONNECTION);

            }
        }
        LogUtils.LOGE("token", "" + prefManager.getPreferencesString(activity, AppConstant.DEVICE_TOKEN));
    }


}
