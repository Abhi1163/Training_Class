package com.hobbyer.android.viewmodel.activity.restpasswordviewmodel;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.ChangePasswordRequest;
import com.hobbyer.android.api.response.auth.signup.SignUpData;
import com.hobbyer.android.api.response.auth.signup.SignUpResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.interfaces.SingUpNavigator;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.SnackbarUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;
import com.hobbyer.android.view.activities.forgotpassword.RestPasswordActivity;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import retrofit2.Call;

public class RestPasswordViewModel extends ActivityViewModel<RestPasswordActivity> implements TextWatcher,View.OnClickListener {
     private ImageView mBackImageView;
      private EditText etPassword, etConfirmPassword;
    private SingUpNavigator mSingUpNavigator;
    private String password,confirmPassword;
    String email;
    private String bundleEmailStr = "";
    private ImageView mPasswordImage, mPasswordGon, mConfirm, mConfirmGon;
    public RestPasswordViewModel(RestPasswordActivity activity,SingUpNavigator SingUpNavigators) {
        super(activity);
        this.activity=activity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
setToolbar();
        setTextWatcher();

        this.mSingUpNavigator = SingUpNavigators;
        mBackImageView =(ImageView)activity.findViewById(R.id.mBackImageview);
        etPassword = (EditText)activity.findViewById(R.id.etPasswordRest);
        etConfirmPassword = (EditText)activity.findViewById(R.id.etConfirmPasswordRest);
        mPasswordImage = (ImageView) activity.findViewById(R.id.mPasswordImage);
        mPasswordGon = (ImageView) activity.findViewById(R.id.mPasswordGon);
        mConfirm = (ImageView) activity.findViewById(R.id.mConfirm);
        mConfirmGon = (ImageView) activity.findViewById(R.id.mConfirmGon);

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBindleData();
    }
    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.app_name);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }


    private void setTextWatcher() {
        activity.getBinding().etPasswordRest.addTextChangedListener(this);
        activity.getBinding().etConfirmPasswordRest.addTextChangedListener(this);
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btSubmit:
            if (isValid()){
                restPasswordConnection();
            }
            case R.id.mPasswordImage:
                Password("1");
                break;
            case R.id.mPasswordGon:
                PasswordGon("2");
                break;
            case R.id.mConfirm:
                ConfirmPassword("1");
                break;
            case R.id.mConfirmGon:
                ConfirmPasswordGone("2");
                break;

           }
          }



    private void getBindleData() {
        Bundle bundle = activity.getIntent().getExtras();
        if(bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.USER_EMAIL)) {
                email = bundle.getString(AppConstant.BUNDLE_KEY.USER_EMAIL);
            }
            if(!TextUtils.isEmpty(email)) {
                bundleEmailStr =email;
                Log.d("playlist_response", "Passed Background track : "  +"email"+ bundleEmailStr);
                // pinViewOtp.setText(bundleOTPStr);
            }
           }
         }




    private void restPasswordConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                ChangePasswordRequest changePassword = new ChangePasswordRequest();
                changePassword.setPassword(etPassword.getText().toString());
                changePassword.setPasswordConfirmation(etConfirmPassword.getText().toString());
                changePassword.setEmail(email);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.resetPassword(changePassword).enqueue(new BaseCallback<BaseResponse>(activity) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                BaseResponse changePasswordResult = response;

                                Intent home= new Intent(activity, LogInActivity.class);
                                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                activity.startActivity(home);


                            }else {
                                ToastUtils.showToastLong(activity, "" + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });


            }catch (Exception e){

            }
             }else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

            }

            private boolean isValid() {
                password=activity.getBinding().etPasswordRest.getText().toString().trim();
                confirmPassword=activity.getBinding().etConfirmPasswordRest.getText().toString().trim();
                if (StringUtils.isBlank(password)) {
                    activity.getBinding().etPasswordRest.requestFocus();
                    activity.getBinding().tvPasswordError.setVisibility(View.VISIBLE);
                    activity.getBinding().tvPasswordError.setText(R.string.please_enter_password);
                    return false;
                } else if (!RegexUtils.isValidPassword(password)) {
                    activity.getBinding().etPasswordRest.requestFocus();
                    activity.getBinding().tvPasswordError.setVisibility(View.VISIBLE);
                    activity.getBinding().tvPasswordError.setText(R.string.please_enter_valid_password);
                    return false;
                } else if (StringUtils.isBlank(confirmPassword)) {
                    activity.getBinding().etConfirmPasswordRest.requestFocus();
                    activity.getBinding().tvConfirmPassError.setVisibility(View.VISIBLE);
                    activity.getBinding().tvConfirmPassError.setText(R.string.please_enter_confirm_password);
                    return false;
                } else if (!RegexUtils.isValidPassword(confirmPassword)) {
                    activity.getBinding().etConfirmPasswordRest.requestFocus();
                    activity.getBinding().tvConfirmPassError.setVisibility(View.VISIBLE);
                    activity.getBinding().tvConfirmPassError.setText(R.string.please_enter_confirm_password);
                    return false;
                } else if (RegexUtils.isValidPassword(password) && RegexUtils.isValidPassword(confirmPassword)) {
                    if (!RegexUtils.isPasswordMatch(confirmPassword, password)) {
                        activity.getBinding().etConfirmPasswordRest.requestFocus();
                        activity.getBinding().tvConfirmPassError.setVisibility(View.VISIBLE);
                        activity.getBinding().tvConfirmPassError.setText(R.string.please_enter_valid_confirm_password);
                        return false;
                    }

                }
                return true;
            }

    private void Password(String s) {
        int start, end;
        // show password
        mPasswordImage.setVisibility(View.GONE);
        if (s.equalsIgnoreCase("1")) {
            start = activity.getBinding().etPasswordRest.getSelectionStart();
            end = activity.getBinding().etPasswordRest.getSelectionEnd();
            activity.getBinding().etPasswordRest.setTransformationMethod(null);
            activity.getBinding().etPasswordRest.setSelection(start, end);
            mPasswordGon.setVisibility(View.VISIBLE);
        }

    }

    private void PasswordGon(String s) {
        int start, end;
        mPasswordGon.setVisibility(View.GONE);
        if (s.equalsIgnoreCase("2")) {
            start = activity.getBinding().etPasswordRest.getSelectionStart();
            end = activity.getBinding().etPasswordRest.getSelectionEnd();
            activity.getBinding().etPasswordRest.setTransformationMethod(new PasswordTransformationMethod());
            activity.getBinding().etPasswordRest.setSelection(start, end);
            mPasswordImage.setVisibility(View.VISIBLE);
        }
    }


    private void ConfirmPassword(String p) {
        int start, end;
        // show password
        mConfirm.setVisibility(View.GONE);
        if (p.equalsIgnoreCase("1")) {
            start = activity.getBinding().etConfirmPasswordRest.getSelectionStart();
            end = activity.getBinding().etConfirmPasswordRest.getSelectionEnd();
            activity.getBinding().etConfirmPasswordRest.setTransformationMethod(null);
            activity.getBinding().etConfirmPasswordRest.setSelection(start, end);
            mConfirmGon.setVisibility(View.VISIBLE);
        }

    }

    private void ConfirmPasswordGone(String cp) {
        int start, end;
        mConfirmGon.setVisibility(View.GONE);
        if (cp.equalsIgnoreCase("2")) {
            start = activity.getBinding().etConfirmPasswordRest.getSelectionStart();
            end = activity.getBinding().etConfirmPasswordRest.getSelectionEnd();
            activity.getBinding().etConfirmPasswordRest.setTransformationMethod(new PasswordTransformationMethod());
            activity.getBinding().etConfirmPasswordRest.setSelection(start, end);
            mConfirm.setVisibility(View.VISIBLE);
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
        activity.getBinding().tvPasswordError.setVisibility(View.GONE);
        activity.getBinding().tvConfirmPassError.setVisibility(View.GONE);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}
