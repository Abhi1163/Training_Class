package com.hobbyer.android.viewmodel.activity.singupviewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.SignUpRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.login.SignInData;
import com.hobbyer.android.api.response.auth.signup.SignUpData;
import com.hobbyer.android.api.response.auth.signup.SignUpResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.model.ProfileImageModel;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.SnackbarUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.freetrial.ActivityFreeTrial;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.view.activities.membership.ChooseMembershipActivity;
import com.hobbyer.android.view.activities.phonenumber.PhoneNumberActivity;
import com.hobbyer.android.view.activities.review.Review_Activity;
import com.hobbyer.android.view.activities.signup.SignUpActivity;
import com.hobbyer.android.view.activities.termandcondition.TermsAndConditionsActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.hobbyer.android.interfaces.SingUpNavigator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

public class SignUpViewModel extends ActivityViewModel<SignUpActivity> implements TextWatcher, View.OnClickListener {

    private boolean isInternetConnected = false;
    String get_id, get_name, get_email;
    CallbackManager mCallbackManager;
    private Pattern pattern;
    private PrefManager prefManager;
    private Matcher matcher;
    private int termCondition = 0;
    private CheckBox chAgree;
    String deviceToken;
    private String  firstName,lastName,email,password,confirmPassword;


    private ImageView mPasswordImage, mPasswordGon, mConfirm, mConfirmGon;

    private boolean clickable = true;
    private SingUpNavigator mSingUpNavigator;
    private String passwordd, ConfPassword;
    String id,name,city,country,latitude,longitude;
    private String bundleCityStr = "";
    private String bundleIdStr = "";
    private String bundleNameStr="";
    private String bundleCountryStr = "";
    private String bundleLatitudeStr="";
    private String bundleLongitudeStr="";
    public SignUpViewModel(SignUpActivity activity, SingUpNavigator SingUpNavigators) {
        super(activity);
        this.mSingUpNavigator = SingUpNavigators;
        this.activity = activity;
        prefManager = new PrefManager(activity);
        mPasswordImage = (ImageView) activity.findViewById(R.id.mPasswordImage);
        mPasswordGon = (ImageView) activity.findViewById(R.id.mPasswordGon);
        mConfirm = (ImageView) activity.findViewById(R.id.mConfirm);
        mConfirmGon = (ImageView) activity.findViewById(R.id.mConfirmGon);
        chAgree = (CheckBox) activity.findViewById(R.id.chAgree);
        //token = FirebaseInstanceId.getInstance().getToken();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        getBindleData();
        setToolbar();
        clearText();
        deviceToken = PrefManager.getPreferencesString(activity, AppConstant.DEVICE_TOKEN);



    }

    private void setToolbar() {

        activity.getBinding().toolbar.rlLayout.setBackgroundResource(R.color.white);
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
        activity.getBinding().toolbar.tvTitle.setAllCaps(true);
        activity.getBinding().toolbar.tvTitle.setText(R.string.signup);
        activity.getBinding().toolbar.view.setVisibility(View.VISIBLE);


    }


    private void clearText() {
        activity.getBinding().mFistName.addTextChangedListener(this);
        activity.getBinding().mLastName.addTextChangedListener(this);
        activity.getBinding().mEmailId.addTextChangedListener(this);
        activity.getBinding().mPassword.addTextChangedListener(this);
        activity.getBinding().mConfirmPassword.addTextChangedListener(this);
    }
    private void getBindleData() {
        Bundle bundle = activity.getIntent().getExtras();
        if(bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());
            if(activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.ADDRESS_ID)) {
            id = bundle.getString(AppConstant.BUNDLE_KEY.ADDRESS_ID);
            }

            if(activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.ADDRESS_CITY)) {
              city = bundle.getString(AppConstant.BUNDLE_KEY.ADDRESS_CITY);
            }

            if(activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.ADDRESS_NAME)) {
                name = bundle.getString(AppConstant.BUNDLE_KEY.ADDRESS_NAME);
            }

            if(activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.ADDRESS_COUNTRY)) {
               country = bundle.getString(AppConstant.BUNDLE_KEY.ADDRESS_COUNTRY);
            }

            if(activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.ADDRESS_LATITUDE)) {
                latitude = bundle.getString(AppConstant.BUNDLE_KEY.ADDRESS_LATITUDE);
            }

            if(activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.ADDRESS_LONGITUDE)) {
                longitude = bundle.getString(AppConstant.BUNDLE_KEY.ADDRESS_LONGITUDE);
            }
               if(!TextUtils.isEmpty(id)) {
                   bundleIdStr=id;
                   Log.d("playlist_response", "Passed Background track : "
                           + bundleCityStr +"City id"+ bundleIdStr);
               }
            if(!TextUtils.isEmpty(city)) {
                bundleCityStr = city;
                Log.d("playlist_response", "Passed Background track : " + "City Name"+bundleNameStr);
            }
            if(!TextUtils.isEmpty(name)) {
                bundleNameStr = name;
                Log.d("playlist_response", "Passed Background track : "
                        +"Country Name"+bundleCountryStr);
            }
            if(!TextUtils.isEmpty(country)) {
                bundleCountryStr=country;

            }
            if(!TextUtils.isEmpty(latitude)) {
                bundleLatitudeStr = latitude;

            }
            if(!TextUtils.isEmpty(longitude)) {
                bundleLongitudeStr=longitude;

            }
           /* Log.d("playlist_response", "Passed Background track : "
                    + bundleCityStr +"City id"+ bundleIdStr+"City Name"+bundleNameStr
                    +"Country Name"+bundleCountryStr+"Latitude"+bundleLatitudeStr+"Longitude"+bundleLongitudeStr);





                    Log.d("playlist_response", "Passed Background track : "
                            + bundleCityStr +"City id"+ bundleIdStr+"City Name"+bundleNameStr
                            +"Country Name"+bundleCountryStr+"Latitude"+bundleLatitudeStr+"Longitude"+bundleLongitudeStr);*/

            }


        }


    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mLogins:
                Intent home= new Intent(activity, LogInActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                activity.startActivity(home);
                break;
            case R.id.mSignUp:
                if (isValid()) {
                    signUpConnection();
                }
                break;
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
            case R.id.llAgree:
                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.TITLE_NAME, AppConstant.TERMS_CONDITION_NAME);
                bundle.putString(AppConstant.URL_KEY, AppConstant.TERMS_CONDITION_URL);
                Intent intentCommunity = new Intent(activity, TermsAndConditionsActivity.class);
                intentCommunity.putExtras(bundle);
                activity.startActivity(intentCommunity);
                break;

        }
    }


   /* private boolean isValid() {
        if (StringUtils.isBlank(etFirstName.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_first_name));
            activity.getBinding().mFistName.requestFocus();
            return false;
        } else if (!RegexUtils.validName(etFirstName.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_valid_name));
            activity.getBinding().mFistName.requestFocus();
            return false;
        } else if (StringUtils.isBlank(etLastName.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_last_name));
            activity.getBinding().mLastName.requestFocus();
            return false;
        } else if (!RegexUtils.validName(etLastName.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_valid_name));
            activity.getBinding().mLastName.requestFocus();
            return false;
        } else if (StringUtils.isBlank(etEmail.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_email));
            activity.getBinding().mEmailId.requestFocus();
            return false;
        } else if (!RegexUtils.isValidEmail(etEmail.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_Valid));
            activity.getBinding().mEmailId.requestFocus();
            return false;
        } else if (StringUtils.isBlank(etPassword.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_password));
            return false;
        } else if (!RegexUtils.isValidPassword(etPassword.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_password_correct));
            return false;
        } else if (StringUtils.isBlank(etConfirmPassword.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_password_conf));
            return false;
        } else if (!RegexUtils.isValidPassword(etConfirmPassword.getText().toString())) {
            SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.please_enter_password_conf_correct));
            return false;
        } else if (RegexUtils.isValidPassword(etPassword.getText().toString()) && RegexUtils.isValidPassword(etConfirmPassword.getText().toString())) {
            if (!RegexUtils.isPasswordMatch(etConfirmPassword.getText().toString(), etPassword.getText().toString())) {
                SnackbarUtils.showToastShort(activity.getBinding().mSignUp, activity.getResources().getString(R.string.password_mismatch));
                return false;
            }
        }
        return true;
    }*/
   private boolean isValid() {
       firstName=activity.getBinding().mFistName.getText().toString().trim();
       lastName=activity.getBinding().mLastName.getText().toString().trim();
       email=activity.getBinding().mEmailId.getText().toString().trim();
       password=activity.getBinding().mPassword.getText().toString().trim();
       confirmPassword=activity.getBinding().mConfirmPassword.getText().toString().trim();
       if (StringUtils.isBlank(firstName)) {
           activity.getBinding().mFistName.requestFocus();
           activity.getBinding().tvFirstError.setVisibility(View.VISIBLE);
           activity.getBinding().tvFirstError.setText(R.string.please_enter_first_name);

           return false;
       } else if (!RegexUtils.validName(firstName)) {
           activity.getBinding().mFistName.requestFocus();
           activity.getBinding().tvFirstError.setVisibility(View.VISIBLE);
           activity.getBinding().tvFirstError.setText(activity.getResources().getString(R.string.please_enter_valid_name));
           return false;
       } else if (StringUtils.isBlank(lastName)) {
           activity.getBinding().mLastName.requestFocus();
           activity.getBinding().tvLastError.setVisibility(View.VISIBLE);
           activity.getBinding().tvLastError.setText(activity.getResources().getString(R.string.please_enter_last_name));
           return false;
       } else if (!RegexUtils.validName(lastName)) {
           activity.getBinding().mLastName.requestFocus();
           activity.getBinding().tvLastError.setVisibility(View.VISIBLE);
           activity.getBinding().tvLastError.setText(R.string.please_enter_valid_name);
           return false;
       } else if (StringUtils.isBlank(email)) {
           activity.getBinding().mEmailId.requestFocus();
           activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
           activity.getBinding().tvEmailError.setText(activity.getResources().getString(R.string.please_enter_email));

           return false;
       } else if (!RegexUtils.isValidEmail(email)) {
           activity.getBinding().mEmailId.requestFocus();
           activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
           activity.getBinding().tvEmailError.setText(R.string.please_enter_valid_email);
           return false;
       } else if (StringUtils.isBlank(password)) {
           activity.getBinding().mPassword.requestFocus();
           activity.getBinding().tvPasswordError.setVisibility(View.VISIBLE);
           activity.getBinding().tvPasswordError.setText(activity.getResources().getString(R.string.please_enter_password));
           return false;
       } else if (!RegexUtils.isValidPassword(password) ) {
           activity.getBinding().mPassword.requestFocus();
           activity.getBinding().tvPasswordError.setVisibility(View.VISIBLE);
           activity.getBinding().tvPasswordError.setText(R.string.please_enter_valid_password);
           return false;
       }else if (StringUtils.isBlank(confirmPassword)) {
           activity.getBinding().mConfirmPassword.requestFocus();
           activity.getBinding().tvConfirmPassError.setVisibility(View.VISIBLE);
           activity.getBinding().tvConfirmPassError.setText(R.string.please_enter_confirm_password);
           return false;
       } else if (!confirmPassword.equals(password)) {
           activity.getBinding().mConfirmPassword.requestFocus();
           activity.getBinding().tvConfirmPassError.setVisibility(View.VISIBLE);
           activity.getBinding().tvConfirmPassError.setText(R.string.please_enter_valid_confirm_password);
           return false;
       } else {
           return true;
       }
   }




    private void signUpConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                if (chAgree.isChecked()) {
                    termCondition = 1;
                } else {
                    termCondition = 0;
                    ToastUtils.showToastLong(activity, "Please agree our Terms & Services before Sign Up");
                    return;
                }

                ProgressDialogUtils.show(activity);
                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.setFirstName(firstName);
                signUpRequest.setLastName(lastName);
                signUpRequest.setEmail(email);
                signUpRequest.setPassword(password);
                signUpRequest.setConfirm(confirmPassword);
                signUpRequest.setTermsCondition(termCondition);
                signUpRequest.setDeviceToken(deviceToken);
                signUpRequest.setDeviceType(AppConstant.DEVICE_TYPE);

                signUpRequest.setCity(bundleCityStr);
                signUpRequest.setCountry(bundleCountryStr);
                signUpRequest.setLatitude(bundleLatitudeStr);
                signUpRequest.setLongitude(bundleLongitudeStr);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.signUp(signUpRequest).enqueue(new BaseCallback<SignUpResponse>(activity) {
                    @Override
                    public void onSuccess(SignUpResponse response) {
                        if (response != null) {

                            if (response.getStatus() == 1) {
                                SignUpData signUpData = response.getResult().getData();
                                UserModel userModel = createUserModel(signUpData);
                                PreferenceUtils.saveUserModel(userModel);


                                ProfileImageModel profileModel = createProfile(signUpData);
                                PreferenceUtils.saveProfle(profileModel);

                           //     Intent home= new Intent(activity, ActivityFreeTrial.class);
                                Intent home= new Intent(activity, PhoneNumberActivity.class);

                                activity.startActivity(home);

                            }
                            else {
                                ToastUtils.showToastLong(activity, "  " +response.getMessage());
                            }
                        }


                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<SignUpResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastLong(activity, "  " +baseResponse.getMessage());

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private ProfileImageModel createProfile(SignUpData signUpData) {
            ProfileImageModel profileImageModel = new ProfileImageModel();
            profileImageModel.setFirstName(signUpData.getFirstName());
            profileImageModel.setLastName(signUpData.getLastName());


            return profileImageModel;
        }



    private UserModel createUserModel(SignUpData signUpData) {
        UserModel userModel = new UserModel();
        userModel.setId(signUpData.getId());
        userModel.setFirstName(signUpData.getFirstName());
        userModel.setLastName(signUpData.getLastName());
        userModel.setUserImage("");
        userModel.setCity(signUpData.getCity());
        userModel.setEmail(signUpData.getEmail());

        return userModel;
    }

    private void Password(String s) {
        int start, end;
        // show password
        mPasswordImage.setVisibility(View.GONE);
        if (s.equalsIgnoreCase("1")) {
            start = activity.getBinding().mPassword.getSelectionStart();
            end = activity.getBinding().mPassword.getSelectionEnd();
            activity.getBinding().mPassword.setTransformationMethod(null);
            activity.getBinding().mPassword.setSelection(start, end);
            mPasswordGon.setVisibility(View.VISIBLE);
        }

    }

    private void PasswordGon(String s) {
        int start, end;
        mPasswordGon.setVisibility(View.GONE);
        if (s.equalsIgnoreCase("2")) {
            start = activity.getBinding().mPassword.getSelectionStart();
            end = activity.getBinding().mPassword.getSelectionEnd();
            activity.getBinding().mPassword.setTransformationMethod(new PasswordTransformationMethod());
            activity.getBinding().mPassword.setSelection(start, end);
            mPasswordImage.setVisibility(View.VISIBLE);
        }
    }


    private void ConfirmPassword(String p) {
        int start, end;
        // show password
        mConfirm.setVisibility(View.GONE);
        if (p.equalsIgnoreCase("1")) {
            start = activity.getBinding().mConfirmPassword.getSelectionStart();
            end = activity.getBinding().mConfirmPassword.getSelectionEnd();
            activity.getBinding().mConfirmPassword.setTransformationMethod(null);
            activity.getBinding().mConfirmPassword.setSelection(start, end);
            mConfirmGon.setVisibility(View.VISIBLE);
        }

    }

    private void ConfirmPasswordGone(String cp) {
        int start, end;
        mConfirmGon.setVisibility(View.GONE);
        if (cp.equalsIgnoreCase("2")) {
            start = activity.getBinding().mConfirmPassword.getSelectionStart();
            end = activity.getBinding().mConfirmPassword.getSelectionEnd();
            activity.getBinding().mConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            activity.getBinding().mConfirmPassword.setSelection(start, end);
            mConfirm.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        activity.getBinding().tvFirstError.setVisibility(View.GONE);
        activity.getBinding().tvLastError.setVisibility(View.GONE);
        activity.getBinding().tvEmailError.setVisibility(View.GONE);
        activity.getBinding().tvPasswordError.setVisibility(View.GONE);
        activity.getBinding().tvConfirmPassError.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View view) {

       switch (view.getId())
       {
           case R.id.ivBack:
               finish();
               break;
       }

    }
}


// validating password with retype password

