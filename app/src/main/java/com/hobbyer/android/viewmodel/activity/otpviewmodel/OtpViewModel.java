package com.hobbyer.android.viewmodel.activity.otpviewmodel;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.AddPhoneNumberRequest;
import com.hobbyer.android.api.request.PhoneOtpRequest;
import com.hobbyer.android.api.request.VerifyOtpRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.addphonenumber.AddPhoneNumberResponse;
import com.hobbyer.android.api.response.auth.verifyotp.VerifyOtpData;
import com.hobbyer.android.api.response.auth.verifyotp.VerifyOtpResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.interfaces.RestPassword;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.forgotpassword.RestPasswordActivity;
import com.hobbyer.android.view.activities.membership.ChooseMembershipActivity;
import com.hobbyer.android.view.activities.otpscreen.OtpActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

public class OtpViewModel extends ActivityViewModel<OtpActivity> implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    SmsVerifyCatcher smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
        @Override
        public void onSmsCatch(String message) {
            String code = parseCode(message);
            Log.d("OTP", code);
            Toast.makeText(getActivity(), "OTP :" + code, Toast.LENGTH_SHORT).show();
            //  pinViewOtp.setText(code);
        }
    });
    private long otp;
    private String email, phone, countryCode, type;
    private Integer phoneNumber;
    private RestPassword mRestPasswordNavigator;
    private PinView pinViewOtp;
    private ImageView mBackImageView;
    private String bundleOTPStr = "";
    private String bundleEmailStr = "";
    private String bundlePhoneStr = "";
    private String bundleCodeStr = "";
    private String bundleTypeStr = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    public OtpViewModel(OtpActivity activity, RestPassword RestPasswordNavigator) {
        super(activity);
        this.activity = activity;
        this.mRestPasswordNavigator = RestPasswordNavigator;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        pinViewOtp = activity.findViewById(R.id.pinViewOtp);
        pinViewOtp.setItemCount(4);
        pinViewOtp.setAnimationEnable(true);
        pinViewOtp.setCursorVisible(true);

        /*intent = getIntent().getExtras();
        if (intent!=null){

        }*/

        pinViewOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pinViewOtp.setItemBackgroundColor(Color.BLACK);
        pinViewOtp.setHideLineWhenFilled(false);
        pinViewOtp.setItemBackground(getActivity().getDrawable(R.drawable.item_background));
        pinViewOtp.setItemBackgroundResources(R.drawable.item_background);
        getBindleData();
    }

    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText("Enter OTP");
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }

    private void getBindleData() {

        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());

            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.USER_OTP)) {
                otp = bundle.getLong(AppConstant.BUNDLE_KEY.USER_OTP);
            }
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.USER_EMAIL)) {
                email = bundle.getString(AppConstant.BUNDLE_KEY.USER_EMAIL);
            }
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.PHONE_NUMBER)) {
                phone = bundle.getString(AppConstant.BUNDLE_KEY.PHONE_NUMBER);
            }
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.COUNTRY_CODE)) {
                countryCode = bundle.getString(AppConstant.BUNDLE_KEY.COUNTRY_CODE);
            }
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.TYPE_PHONE)) {
                type = bundle.getString(AppConstant.BUNDLE_KEY.TYPE_PHONE);
            }

            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.USER_OTP)) {
                otp = bundle.getLong(AppConstant.BUNDLE_KEY.USER_OTP);
            }
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.USER_EMAIL)) {
                email = bundle.getString(AppConstant.BUNDLE_KEY.USER_EMAIL);
            }
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.TYPE_EMAIL)) {
                type = bundle.getString(AppConstant.BUNDLE_KEY.TYPE_EMAIL);

            }

            String otpString = Long.toString(otp);
            if (!TextUtils.isEmpty(otpString)) {
                /*bundleOTPStr = otp;*/
                //   parseCode(otpString);
                Log.d("playlist_response", "Passed Background track : " + "otp" + bundleOTPStr);
            }
            if (!TextUtils.isEmpty(email)) {
                bundleEmailStr = email;
                Log.d("playlist_response", "Passed Background track : " + "email" + bundleEmailStr);
                // pinViewOtp.setText(bundleOTPStr);
            }

            if (!TextUtils.isEmpty(phone)) {
                bundlePhoneStr = phone;
                Log.d("playlist_response", "Passed Background track : " + "email" + bundlePhoneStr);
                // pinViewOtp.setText(bundleOTPStr);
            }

            if (!TextUtils.isEmpty(countryCode)) {
                bundleCodeStr = countryCode;
                Log.d("playlist_response", "Passed Background track : " + "email" + bundleCodeStr);
                // pinViewOtp.setText(bundleOTPStr);
            }
            if (!TextUtils.isEmpty(type)) {
                bundleTypeStr = type;
                Log.d("playlist_response", "Passed Background track : " + "email" + bundleTypeStr);
                // pinViewOtp.setText(bundleOTPStr);
            }


        }
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mBackLayout:
                activity.finish();
                break;
            case R.id.mOtpSubmit:
                if (pinViewOtp.length() == 4) {
                    if (type.equalsIgnoreCase("phone_number")) {

                        phoneOtpConnection();
                    } else if (type.equalsIgnoreCase("email")) {
                        emailOtpConnection();
                    }

                    break;
                } else {
                    Toast.makeText(activity, "Enter Correct OTP", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.tvResend:

                if (type.equalsIgnoreCase("phone_number")) {
                    phoneConnection();
                } else if (type.equalsIgnoreCase("email")) {
                    emailOtpConnection();
                }


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String parseCode(String message) {
        Pattern pattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher matcher = pattern.matcher(message);
        String code = "";
        while (matcher.find()) {
            code = matcher.group(0);
        }
        //   pinViewOtp.setText(code);
        return code;

    }

    private void emailOtpConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                VerifyOtpRequest otp = new VerifyOtpRequest();
                otp.setEmail(bundleEmailStr);
                otp.setType(type);
                otp.setOtp(pinViewOtp.getText().toString());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.verifyOtp(otp).enqueue(new BaseCallback<VerifyOtpResponse>(activity) {
                    @Override
                    public void onSuccess(VerifyOtpResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                VerifyOtpData signUpData = response.getResult().getData();
                             /*   Intent intent = new Intent(activity, BillingActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.FROM, AppConstant.OTP);
                                bundle.putString(AppConstant.BUNDLE_KEY.USER_EMAIL, signUpData.getUser().getEmail());
                                intent.putExtras(bundle);
                                activity.startActivity(intent);*/

                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.BUNDLE_KEY.USER_EMAIL, signUpData.getUser().getEmail());
                                bundle.putString(AppConstant.FROM, AppConstant.OTP);
                                ActivityController.startActivityForResult(activity, RestPasswordActivity.class, bundle, true);
                                /*mRestPasswordNavigator.openRestActivity();*/
                            } else {
                                ToastUtils.showToastLong(activity, "" + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<VerifyOtpResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void phoneOtpConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                PhoneOtpRequest otp = new PhoneOtpRequest();
                otp.setOtp(pinViewOtp.getText().toString());
                otp.setEmail(userModel.getEmail());
                otp.setType(type);
                otp.setPhone_number(bundlePhoneStr);
                otp.setCountry_code(bundleCodeStr);
                otp.setUser_id("" + userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.verifyPhoneOtp(otp).enqueue(new BaseCallback<VerifyOtpResponse>(activity) {
                    @Override
                    public void onSuccess(VerifyOtpResponse response) {

                        if (response != null) {



                            if (response.getStatus() == 1) {


                                VerifyOtpData signUpData = response.getResult().getData();

                                UserModel userModel = createUserModel(signUpData);
                                PreferenceUtils.saveUserModel(userModel);
                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.BUNDLE_KEY.USER_EMAIL, signUpData.getUser().getEmail());

                                ActivityController.startActivityForResult(activity, ChooseMembershipActivity.class, bundle, true);


                            } else {


                                ToastUtils.showToastShort(activity, response.getMessage());

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<VerifyOtpResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();


                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private UserModel createUserModel(VerifyOtpData verifyOtpData) {

        UserModel userModel = new UserModel();
        userModel.setId(verifyOtpData.getUser().getId());
        userModel.setFirstName(verifyOtpData.getUser().getFirstName());
        userModel.setLastName(verifyOtpData.getUser().getLastName());
        userModel.setUserImage("");
        userModel.setCity(verifyOtpData.getUser().getCity());
        userModel.setEmail(verifyOtpData.getUser().getEmail());
        userModel.setPhone_code_number(verifyOtpData.getUser().getPhoneCode().getPhone_code());


        userModel.setPhoneNumber(verifyOtpData.getUser().getPhoneNumber());

        return userModel;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }


    private void phoneConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);

                UserModel userModel = PreferenceUtils.getUserModel();
                long userId = userModel.getId();
                int inUserId = (int) userId;
                if (userModel == null) {
                    return;
                }


                ProgressDialogUtils.show(activity);
                AddPhoneNumberRequest addPhoneRequest = new AddPhoneNumberRequest();
                addPhoneRequest.setUser_id("" + inUserId);
                addPhoneRequest.setPhone_number(phone);
                addPhoneRequest.setCountry_code(countryCode);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.addPhoneNumber(addPhoneRequest).enqueue(new BaseCallback<AddPhoneNumberResponse>(activity) {
                    @Override
                    public void onSuccess(AddPhoneNumberResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ToastUtils.showToastShort(activity, "otp resend successfully");
                           /*     AddPhoneNumberData getPrivacyData = response.getResult().getData();
                                Bundle bundle = new Bundle();
                                bundle.putLong(AppConstant.BUNDLE_KEY.USER_OTP, getPrivacyData.getOtp());
                                bundle.putString(AppConstant.BUNDLE_KEY.PHONE_NUMBER, addPhoneRequest.getPhone_number());
                                bundle.putString(AppConstant.BUNDLE_KEY.TYPE_PHONE, "phone_number");
                                ActivityController.startActivity(activity, OtpActivity.class, bundle);
                                activity.finish();*/

                            } else {
                                ToastUtils.showToastLong(activity, "  " + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<AddPhoneNumberResponse> call, BaseResponse baseResponse) {
                        ToastUtils.showToastLong(activity, "  " + baseResponse.getMessage());

                        ProgressDialogUtils.dismiss();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }


}
