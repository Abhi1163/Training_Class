package com.hobbyer.android.viewmodel.activity.loginviewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.SignInRequest;
import com.hobbyer.android.api.request.SocialLoginRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.login.SignInData;
import com.hobbyer.android.api.response.auth.login.SignInDataTemp;
import com.hobbyer.android.api.response.auth.login.SignInResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.ActivityLoginBinding;
import com.hobbyer.android.interfaces.ApiResponseListener;
import com.hobbyer.android.model.ProfileImageModel;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.EmailValidation;
import com.hobbyer.android.utils.FbConnectHelper;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.SnackbarUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.findyourcity.FindYourCityActivity;
import com.hobbyer.android.view.activities.forgotpassword.ForgotPasswordActivity;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.view.activities.phonenumber.PhoneNumberActivity;
import com.hobbyer.android.view.activities.signup.SignUpActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.hobbyer.android.interfaces.LogInNavigator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;


public class LogInViewModel extends ActivityViewModel<LogInActivity> implements ApiResponseListener,FbConnectHelper.OnFbSignInListener, TextWatcher {
    private FbConnectHelper fbConnectHelper;
    private LogInNavigator mLogInNavigator;
    private EditText etEmail,etPassword;
    private ImageView mImagePsGon,mImagePsLog;
    private ActivityLoginBinding binding;
    private String saveEmailId=null,savePassword=null;
    private Context mContact;
    private CheckBox cbEmail;
    private String deviceToken;
    String date;
    public LogInViewModel(LogInActivity activity, LogInNavigator LogInNavigator) {
        super(activity);
        this.mLogInNavigator = LogInNavigator;
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.white));
        }

        addTextWatcher();
        saveMail();
        /*facebookHashkey();*/
        FacebookSdk.sdkInitialize(activity);
        /*addTextWatcher();*/
        fbConnectHelper = new FbConnectHelper(activity, this);

        mImagePsGon=(ImageView)activity.findViewById(R.id.mImagePsGon);
        mImagePsLog=(ImageView)activity.findViewById(R.id.mImagePsLog);
        etEmail =(EditText)activity.findViewById(R.id.etEmailLogin);
        etPassword =(EditText)activity.findViewById(R.id.etPassword);
        cbEmail=(CheckBox)activity.findViewById(R.id.cbEmail);
        deviceToken = PrefManager.getPreferencesString(activity, AppConstant.DEVICE_TOKEN);


    }

    private void addTextWatcher() {
        activity.getBinding().etEmailLogin.addTextChangedListener(this);
        activity.getBinding().etPassword.addTextChangedListener(this);
    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.SignUp:

                if (CommonUtils.isOnline(activity)) {
                    try {
                        Intent findCity= new Intent(activity,FindYourCityActivity.class);
                        activity.startActivity(findCity);

                    } catch (Exception e) {
                    }
                }else {
                    Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.mLogin:

                if (isValid()) {
                    if (cbEmail.isChecked()){


                      //  PrefManager.saveMailId(activity,"Email",etEmail.getText().toString());
                        PrefManager.savePreferences(activity, AppConstant.USER_PASSWORD, etPassword.getText().toString());
                        PrefManager.savePreferences(activity, AppConstant.USER_EMAIL, etEmail.getText().toString());
                    }else {

                    }
                    sigInConnection();

                }
                break;
            case R.id.mForgotPassword:

                Bundle bundle = new Bundle();
                ActivityController.startActivity(activity, ForgotPasswordActivity.class, bundle);
                //mLogInNavigator.openForgotPasswordActivity();
                break;
            case R.id.mLogInWithFb:
                if (CommonUtils.isOnline(activity)) {
                    try {
                        fbConnectHelper.connect();

                    } catch (Exception e) {

                    }
                }else {
                    Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.mImagePsLog:
                Password("1");
                break;
            case R.id.mImagePsGon:
                PasswordGon("2");
                break;

            case R.id.cbEmail:

                break;


        }
    }



    private boolean isValid() {
        if (StringUtils.isBlank(etEmail.getText().toString())) {
            activity.getBinding().etEmailLogin.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(activity.getResources().getString(R.string.please_enter_email));
            return false;
        }

      /*  else if (!EmailValidation.validEmail(etEmail.getText().toString())) {
            activity.getBinding().etEmailLogin.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(activity.getResources().getString(R.string.please_enter_valid_email));
            return false;
        }*/


        else if (!RegexUtils.isValidEmail(etEmail.getText().toString())) {
            activity.getBinding().etEmailLogin.requestFocus();
            activity.getBinding().tvEmailError.setVisibility(View.VISIBLE);
            activity.getBinding().tvEmailError.setText(activity.getResources().getString(R.string.please_enter_valid_email));
            return false;
        }else if (StringUtils.isBlank(etPassword.getText().toString())) {
            activity.getBinding().etPassword.requestFocus();
            activity.getBinding().tvPasswordError.setVisibility(View.VISIBLE);
            activity.getBinding().tvPasswordError.setText(activity.getResources().getString(R.string.please_enter_password));
            return false;
        } else if (!RegexUtils.isValidPassword(etPassword.getText().toString())){
            activity.getBinding().etPassword.requestFocus();
            activity.getBinding().tvPasswordError.setVisibility(View.VISIBLE);
            activity.getBinding().tvPasswordError.setText(R.string.please_enter_valid_password);
            return false;
        }else {
            return true;
        }


    }
    @Override
    public void onApiSuccess(Object response, String apiName) {

    }

    @Override
    public void onApiFailure(String failureMessage, String apiName) {

    }
    private void saveMail(){
        saveEmailId = PrefManager.getPreferencesString(activity,AppConstant.USER_EMAIL);
        savePassword = PrefManager.getPreferencesString(activity,AppConstant.USER_PASSWORD);
        if (!TextUtils.isEmpty(saveEmailId)&&saveEmailId!=null && savePassword!=null) {
            activity.getBinding().etEmailLogin.setText(saveEmailId);
            activity.getBinding().etPassword.setText(savePassword);
            activity.getBinding().cbEmail.setChecked(true);
        }else{
            activity.getBinding().cbEmail.setChecked(false);
        }
    }


    private void sigInConnection() {
        if (CommonUtils.isOnline(activity)) {
          /*  try {*/
                ProgressDialogUtils.show(activity);
                SignInRequest signInRequest = new SignInRequest();
                signInRequest.setEmail(etEmail.getText().toString());
                signInRequest.setPassword(etPassword.getText().toString());
                signInRequest.setDeviceToken(deviceToken);
                signInRequest.setDeviceType(AppConstant.DEVICE_TYPE);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.signIn(signInRequest).enqueue(new BaseCallback<SignInResponse>(activity) {
                    @Override
                    public void onSuccess(SignInResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SignInDataTemp signInData = response.getResult().getData();

                              date=  signInData.getExpiryDate();
                                PrefManager.savePreferences(activity,AppConstant.DATE,date);

                                UserModel userModel = createUserModel(signInData);
                                PreferenceUtils.saveUserModel(userModel);
                                //image
                                ProfileImageModel profileModel = createProfile(signInData);
                                PreferenceUtils.saveProfle(profileModel);

                                if (response.getResult().getData().getPhoneVerified() == 1) {

                                    if (cbEmail.isChecked()){

                                        PrefManager.savePreferences(activity, AppConstant.USER_PASSWORD, etPassword.getText().toString());
                                        PrefManager.savePreferences(activity, AppConstant.USER_EMAIL, etEmail.getText().toString());

                                     //   PrefManager.saveMailId(activity,"Email",etEmail.getText().toString());
                                        Intent home = new Intent(activity, HomeActivity.class);
                                        activity.startActivity(home);
                                    }else {
                                        Intent home = new Intent(activity, HomeActivity.class);
                                        activity.startActivity(home);
                                    }
                                    Intent home = new Intent(activity, HomeActivity.class);
                                    activity.startActivity(home);


                                }
                                else {
                                    Intent intent = new Intent(activity, PhoneNumberActivity.class);
                                    activity.startActivity(intent);

                                }
                            }else {
                                ToastUtils.showToastLong(activity, " " +response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<SignInResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });





            /*catch (Exception e){

            }*/
        }  else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private ProfileImageModel createProfile(SignInDataTemp signInData) {
        ProfileImageModel profileImageModel = new ProfileImageModel();
        profileImageModel.setUserImage(signInData.getUserImage());
        profileImageModel.setFirstName(signInData.getFirstName());
        profileImageModel.setLastName(signInData.getLastName());
        profileImageModel.setSocialId(signInData.getSocialId());


        return profileImageModel;
    }




    private UserModel createUserModel(SignInDataTemp signInData) {
        UserModel userModel = new UserModel();
        userModel.setId(signInData.getId());
        userModel.setFirstName(signInData.getFirstName());
        userModel.setLastName(signInData.getLastName());
        userModel.setBusinessName(signInData.getBusinessName());
        userModel.setWebsiteUrl(signInData.getWebUrl());
        userModel.setEmail(signInData.getEmail());
        userModel.setPhoneCode(signInData.getPhoneCode());
        userModel.setPhoneNumber(signInData.getPhoneNumber());
        userModel.setCountryId(signInData.getCountryId());
        userModel.setBusinessOpen(signInData.getBusinessOpen());
        userModel.setCity(signInData.getCity());
        userModel.setSocialType(signInData.getSocialType());
        userModel.setSocialId(signInData.getSocialId());
        userModel.setAlternatePhone(signInData.getAlternatePhone());
        userModel.setStreet(signInData.getStreet());
        userModel.setApartment(signInData.getApartment());
        userModel.setPostalCode(signInData.getPostalCode());
        userModel.setGender(signInData.getGender());
        userModel.setDob(signInData.getDob());
        userModel.setUserImage(signInData.getUserImage());
        userModel.setEmergencyContactName(signInData.getEmergencyContactName());
        userModel.setPhoneVerified(signInData.getPhoneVerified());
        userModel.setEmergencyContactPhone(signInData.getEmergencyContactPhone());
        return userModel;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbConnectHelper.onActivityResult(requestCode, resultCode, data);

    }



    @Override
    public void OnFbSuccess(GraphResponse graphResponse) {
        SocialLoginRequest userModel = getUserModelFromGraphResponse(graphResponse);
        if(userModel!=null) {
            //PrefManager.getSharedInstance().saveUserModel(userModel);
            /*PreferenceUtils.saveUserModel(userModel);*/
            SocialLoginConnection(userModel);
        }
    }

    @Override
    public void OnFbError(String errorMessage) {

    }
    private SocialLoginRequest getUserModelFromGraphResponse(GraphResponse graphResponse)
    {
        SocialLoginRequest userModel = new SocialLoginRequest();
        try {
            JSONObject jsonObject = graphResponse.getJSONObject();
            userModel.setFirst_name(jsonObject.getString("first_name"));
            userModel.setLast_name(jsonObject.getString("last_name"));
            userModel.setEmail(jsonObject.getString("email"));
            userModel.setSocial_id(jsonObject.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userModel;
    }


    private void Password(String s){
        int start,end;
        // show password
        mImagePsLog.setVisibility(View.GONE);
        if (s.equalsIgnoreCase("1")) {
            start = activity.getBinding().etPassword.getSelectionStart();
            end = activity.getBinding().etPassword.getSelectionEnd();
            activity.getBinding().etPassword.setTransformationMethod(null);
            activity.getBinding().etPassword.setSelection(start, end);
            mImagePsGon.setVisibility(View.VISIBLE);
        }

    }
    private void PasswordGon(String s){
        int start,end;
        mImagePsGon.setVisibility(View.GONE);
        if (s.equalsIgnoreCase("2")) {
            start = activity.getBinding().etPassword.getSelectionStart();
            end = activity.getBinding().etPassword.getSelectionEnd();
            activity.getBinding().etPassword.setTransformationMethod(new PasswordTransformationMethod());
            activity.getBinding().etPassword.setSelection(start, end);
            mImagePsLog.setVisibility(View.VISIBLE);
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
        activity.getBinding().tvEmailError.setVisibility(View.INVISIBLE);
        activity.getBinding().tvPasswordError.setVisibility(View.INVISIBLE);

    }


    private void SocialLoginConnection(SocialLoginRequest socialLoginRequest) {
        if (CommonUtils.isOnline(activity)) {
            try {

                ProgressDialogUtils.show(activity);
                socialLoginRequest.setSocial_type("fb");
                socialLoginRequest.setDevice_token(deviceToken);
                socialLoginRequest.setDevice_type("1");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.socialLogin(socialLoginRequest).enqueue(new BaseCallback<SignInResponse>(activity) {
                    @Override
                    public void onSuccess(SignInResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                SignInDataTemp signInData = response.getResult().getData();
                                UserModel userModel = createUserModel(signInData);
                                PreferenceUtils.saveUserModel(userModel);
                                ProfileImageModel profileModel = createProfile(signInData);
                                PreferenceUtils.saveProfle(profileModel);
                                Intent home = new Intent(activity, HomeActivity.class);
                                activity.startActivity(home);

                            }else {
                                ToastUtils.showToastLong(activity, " " +response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<SignInResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });





            }catch (Exception e){

            }
        }  else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    public  void facebookHashkey() {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}





















