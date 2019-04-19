package com.hobbyer.android.viewmodel.activity.findyourcityviewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CheckCityRequest;
import com.hobbyer.android.api.request.ForgotPasswordRequest;
import com.hobbyer.android.api.request.StudioUserNotExistRequest;
import com.hobbyer.android.api.response.auth.forgotpassword.ForgotPasswordData;
import com.hobbyer.android.api.response.auth.forgotpassword.ForgotPasswordResponse;
import com.hobbyer.android.api.response.auth.searchcity.CityContentList;
import com.hobbyer.android.api.response.auth.searchcity.CityResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.interfaces.LocationResult;
import com.hobbyer.android.locationtracker.LocationTracker;
import com.hobbyer.android.model.FindCityModel;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.citysearch.ActivityCitySearch;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;
import com.hobbyer.android.view.activities.findyourcity.FindYourCityActivity;
import com.hobbyer.android.view.activities.login.LogInActivity;
import com.hobbyer.android.view.activities.otpscreen.OtpActivity;
import com.hobbyer.android.view.activities.signup.SignUpActivity;
import com.hobbyer.android.view.adapter.AdapterCitySearch;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.hobbyer.android.interfaces.FindCityNavigator;
import com.hobbyer.android.widget.CustomDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;


public class FindYourCityViewModel extends ActivityViewModel<FindYourCityActivity> implements LocationResult,TextWatcher {
    private String CityName;
    private String Location;
    private Location mLocation;
    //FindCityModel cityModels;
    private LocationTracker locationTracker;
    private boolean isFirstTime = true;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE = 1;
    public ObservableField<String> address = new ObservableField<>();
    private PrefManager prefManager;
    private Intent mIntent;
    private TextView mCityName;
    private String cityName,latitude,longitude;
    private CustomDialog dialog;
    private FindCityNavigator NavigatorActivity;
    private  TextView etCityName,btConfirm,btCancel;
    private TextView mEmailSubmit,bnCancel;
    private EditText etEmailId;
    private TextView tvEmailError;

    private String addressId = "", addressName = "", addressCity = "", addressCountry = "",
            addressLatitude = "", addressLongitude = "";


    public FindYourCityViewModel(FindYourCityActivity activity,FindCityNavigator FindCityNavigator) {
        super(activity);
        this.NavigatorActivity = FindCityNavigator;
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        mCityName=(TextView)activity.findViewById(R.id.mCityName);
        checkLocationSetting();
        prefManager = new PrefManager(activity);
        mIntent = activity.getIntent();
        try {
            cityName = mIntent.getStringExtra("CITY_NAME");
            latitude = mIntent.getStringExtra("CITY_LATITUDE");
            longitude = mIntent.getStringExtra("CITY_LONGITUDE");
            mCityName.setText(cityName);
        }catch (Exception e){

        }
        requestPermission();

    }

    private void setToolbar() {
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.find_your_city);
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);

        activity.getBinding().toolbar.ivBack.setOnClickListener(v-> finish());

    }

    @Override
    public void onStop() {
        super.onStop();
    }
    public void onClickView(View view){
        switch (view.getId()) {
            case R.id.tv_login:
//                Intent signUp= new Intent(activity,SignUpActivity.class);
//                activity.startActivity(signUp);

                if(TextUtils.isEmpty(cityName) && TextUtils.isEmpty(latitude)
                        && TextUtils.isEmpty(longitude) ) {
                    ToastUtils.showToastLong(activity, "Address not found.");
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_ID, addressId);
                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_NAME, addressName);
                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_CITY, cityName);
                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_COUNTRY, "India");
                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_LATITUDE, latitude);
                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_LONGITUDE, longitude);
                ActivityController.startActivity(activity, SignUpActivity.class, bundle,true);
                finish();

                break;
            case R.id.navigation:
                activity.getBinding().mGetAddress.setEnabled(true);
                checkLocationSetting();
                showAlertFindCity();
                break;
            case R.id.mCityName:
                checkLocationSetting();
                ActivityController.startActivity(activity, ActivityCitySearch.class);
                break;

        }
    }






   /* private boolean isValid() {
        CityName = activity.getBinding().mCityName.getText().toString().trim();
        // Location = activity.getBinding().mLocation.getText().toString().trim();
        return true;
    }*/

    /**
     *  permission request for location
     */

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        } else {
            ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkLocationSetting();
                    //Toast.makeText(activity, activity.getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
                } else {
                    //  Toast.makeText(activity, activity.getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    finish();
                }
                break;

            default:
                break;
        }

    }


    @Override
    public void gotLocation(Location location) {

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address1 = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        address.set(address1);
        // activity.getBinding().mGetAddress.setText(address1);
        etCityName.setText(address1);
        FindCityModel cityModel = new FindCityModel();
        cityModel.setLatitude(location.getLatitude());
        cityModel.setLongitude(location.getLongitude());
        cityModel.setCity(city);
        cityModel.setCountry(country);


        addressId = "";
        addressName = address1;
        addressCity = city;
        addressCountry = country;
        addressLatitude = ""+location.getLatitude();
        addressLongitude = ""+location.getLongitude();

    }

    private void checkLocationSetting() {
        if (!checkPermission()) {
            requestPermission();

        } else {
            locationTracker = new LocationTracker(activity, this);
            locationTracker.onUpdateLocation();
        }
    }

    public void showAlertFindCity() {

        dialog = new CustomDialog(activity, R.layout.find_city_popup);
        btConfirm=(TextView) dialog.findViewById(R.id.btConfirm);
        btCancel=(TextView)dialog.findViewById(R.id.btCancel);
        etCityName=(TextView)dialog.findViewById(R.id.etCityName);

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCityConnection();

                dialog.dismiss();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void checkCityConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                if(TextUtils.isEmpty(addressCity) && TextUtils.isEmpty(addressLatitude) && TextUtils.isEmpty(addressLongitude)) {
                    ToastUtils.showToastLong(activity, "Address not found.");
                    return;
                }
                ProgressDialogUtils.show(activity);
                CheckCityRequest checkCityRequest = new CheckCityRequest();
                checkCityRequest.setCity(addressCity);
                checkCityRequest.setLatitude(addressLatitude);
                checkCityRequest.setLongitude(addressLatitude);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.check_City(checkCityRequest).enqueue(new BaseCallback<BaseResponse>(activity) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ToastUtils.showToastLong(activity, "  " +response.getMessage());

                                if(TextUtils.isEmpty(addressCity) && TextUtils.isEmpty(addressCountry)
                                        && TextUtils.isEmpty(addressLatitude) && TextUtils.isEmpty(addressLongitude)) {
                                    ToastUtils.showToastLong(activity, "Address not found.");
                                    return;
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_ID, addressId);
                                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_NAME, addressName);
                                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_CITY, addressCity);
                                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_COUNTRY, addressCountry);
                                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_LATITUDE, addressLatitude);
                                bundle.putString(AppConstant.BUNDLE_KEY.ADDRESS_LONGITUDE, addressLongitude);
                                ActivityController.startActivity(activity, SignUpActivity.class, bundle,true);
                            }else if (response.getStatus() == 0){
                                showAlertEmail();

                                /*showAlertEmail();*/
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    private void showDialog() {
        dialog=new CustomDialog(activity,R.layout.row_message_city);
        mEmailSubmit=dialog.findViewById(R.id.btnOk);

        mEmailSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });

        dialog.show();


    }

    public void showAlertEmail() {

        dialog = new CustomDialog(activity, R.layout.dialog_gmail_submit);
        mEmailSubmit =(TextView)dialog.findViewById(R.id.btYes);
        bnCancel =(TextView)dialog.findViewById(R.id.btCityNo);
        tvEmailError= (TextView)dialog.findViewById(R.id.tvEmailError);
        etEmailId=(EditText) dialog.findViewById(R.id.etEmailId);

        mEmailSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()){
                    emailConnection(etEmailId.getText().toString());
                     dialog.dismiss();
                }else {

                }

            }
        });

        bnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void emailConnection(String email) {
        if (CommonUtils.isOnline(activity)) {
            ProgressDialogUtils.show(activity);
            StudioUserNotExistRequest emailNotExist = new StudioUserNotExistRequest();
            emailNotExist.setEmail(email);
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
            webServices.studio_User_not_Exist(emailNotExist).enqueue(new BaseCallback<BaseResponse>(activity) {
                @Override
                public void onSuccess(BaseResponse response) {
                    if (response != null) {
                        if (response.getStatus() == 1) {
                            showDialog();
                        }else if (response.getStatus()==0){
                            ToastUtils.showToastLong(activity, " " +response.getMessage());
                            dialog.dismiss();
                        }
                    }

                    ProgressDialogUtils.dismiss();
                }

                @Override
                public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {


                    ProgressDialogUtils.dismiss();
                }

            });

        }  else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValid() {
        if (StringUtils.isBlank(etEmailId.getText().toString())) {
            tvEmailError.setText(activity.getResources().getString(R.string.please_enter_email));
            tvEmailError.setVisibility(View.VISIBLE);
            etEmailId.requestFocus();
            return false;

        } else if (!RegexUtils.isValidEmail(etEmailId.getText().toString())) {
            tvEmailError.setText(activity.getResources().getString(R.string.please_enter_valid_email));
            tvEmailError.setVisibility(View.VISIBLE);
            etEmailId.requestFocus();
            //SnackbarUtils.showToastShort(activity.getBinding().mLogin, activity.getResources().getString(R.string.please_enter_Valid));
            return false;
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        tvEmailError.setVisibility(View.VISIBLE);
    }
}
