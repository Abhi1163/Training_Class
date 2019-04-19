package com.hobbyer.android.viewmodel.activity.forgotpasswordviewmodel;


import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.ForgotPasswordRequest;
import com.hobbyer.android.api.response.auth.forgotpassword.ForgotPasswordData;
import com.hobbyer.android.api.response.auth.forgotpassword.ForgotPasswordResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.interfaces.ForgotPasswordNavigator;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;
import com.hobbyer.android.view.activities.forgotpassword.ForgotPasswordActivity;
import com.hobbyer.android.view.activities.otpscreen.OtpActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import retrofit2.Call;

public class ForgotPasswordViewModel extends ActivityViewModel<ForgotPasswordActivity> implements TextWatcher,View.OnClickListener {
    private ImageView mBackImageView;
    private ForgotPasswordNavigator mForgotPasswordNavigator;
    private String email;
    private boolean isInternetConnected = false;

    public ForgotPasswordViewModel(ForgotPasswordActivity activity, ForgotPasswordNavigator ForgotPasswordNavigators) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        setTextWatcher();

        this.mForgotPasswordNavigator = ForgotPasswordNavigators;
        isInternetConnected= isInternetConnected = CommonUtils.isOnline(activity);

    }

    private void setTextWatcher() {
        activity.getBinding().etEmailId.addTextChangedListener(this);
    }

    public void onClickView(View view) {

        switch (view.getId()) {
            case R.id.mEmailSubmit:
                if (isInternetConnected) {
                    try {
                        if (isValid()) {
                            forgotPasswordConnection();
                        }
                    } catch (Exception e) {
                    }
                }else {
                    Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.forgot);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }
    private boolean isValid() {
        email=activity.getBinding().etEmailId.getText().toString().trim();

        if (StringUtils.isBlank(email)) {
            activity.getBinding().etEmailId.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(R.string.please_enter_email);
            return false;
        } else if (!RegexUtils.isValidEmail(email)) {
            activity.getBinding().etEmailId.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(R.string.please_enter_valid_email);
            return false;
        }
        return true;
    }
    private void forgotPasswordConnection() {
        if (CommonUtils.isOnline(activity)) {
            ProgressDialogUtils.show(activity);
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
            forgotPasswordRequest.setEmail(email);
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
            webServices.forgotPassword(forgotPasswordRequest).enqueue(new BaseCallback<ForgotPasswordResponse>(activity) {
                @Override
                public void onSuccess(ForgotPasswordResponse response) {
                    if (response != null) {
                        if (response.getStatus() == 1) {
                            ForgotPasswordData forgotPasswordData = response.getResult().getData();
                            Bundle bundle = new Bundle();
                            bundle.putLong(AppConstant.BUNDLE_KEY.USER_OTP, forgotPasswordData.getOtp());
                            bundle.putString(AppConstant.BUNDLE_KEY.USER_EMAIL, forgotPasswordRequest.getEmail());
                            bundle.putString(AppConstant.BUNDLE_KEY.TYPE_EMAIL, "email");
                            ActivityController.startActivity(activity, OtpActivity.class, bundle);
                            activity.finish();
                        }else {
                            ToastUtils.showToastLong(activity, " " +response.getMessage());
                        }
                    }

                    ProgressDialogUtils.dismiss();
                }

                @Override
                public void onFail(Call<ForgotPasswordResponse> call, BaseResponse baseResponse) {
                    ProgressDialogUtils.dismiss();
                }

            });

        }  else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);

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
