/*
package com.hobbyer.android.viewmodel.activity.gymtime;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.ObservableField;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.AddPhoneNumberRequest;
import com.hobbyer.android.api.request.ClassDetailRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.addphonenumber.AddPhoneNumberData;
import com.hobbyer.android.api.response.auth.addphonenumber.AddPhoneNumberResponse;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailData;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.interfaces.LocationResult;
import com.hobbyer.android.locationtracker.LocationTracker;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;
import com.hobbyer.android.view.activities.mybooking.ActivityMyBooking;

import com.hobbyer.android.view.activities.otpscreen.OtpActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import retrofit2.Call;

public class GymTimeViewModel extends ActivityViewModel {
    private static int MY_BOOKING_REQ_CODE = 208;
    public Bundle bundle;
    public ObservableField<String> classText = new ObservableField<>("");
    private GymTimeActivity activity;
    private int flag = 1;
    private MapView mapView;
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private GoogleApiClient googleApiClient;
    private LocationTracker locationTracker;
    private  String result;
    private String classScheduleId;
    private String bundleScheduleIdStr = "";

    public GymTimeViewModel(GymTimeActivity activity) {
        super(activity);
        this.activity = activity;
        setRatingText();
        classText.set("Reserve class");
        mapView = (MapView) activity.findViewById( R.id.map );
        getBundleData();
        classDetailsConnection();
    }


    private void setRatingText() {
        String rating = String.valueOf(activity.getBinding().ratingBar2.getRating());
        activity.getBinding().tvRating.setText(rating + " /5.0 based on 256 ratings");

    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btnReserveClass:

                Intent i = new Intent(activity, ActivityMyBooking.class);
                Bundle bundle=new Bundle();
                bundle.putString("type", classText.get());
                i.putExtras(bundle);
                getActivity().startActivityForResult(i, MY_BOOKING_REQ_CODE);
                break;
            case R.id.ivGymTimeBack:
                finish();

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_BOOKING_REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            result = data.getStringExtra("ok");
            classText.set(result);
        }

    }


    private void classDetailsConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                ProgressDialogUtils.show(activity);
                ClassDetailRequest classDetailRequest = new ClassDetailRequest();
                classDetailRequest.setUser_id(""+userModel.getId());
                classDetailRequest.setClass_schedule_id(bundleScheduleIdStr);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
                webServices.class_Details(classDetailRequest).enqueue(new BaseCallback<ClassDetailResponse>(activity) {
                    @Override
                    public void onSuccess(ClassDetailResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ClassDetailData getPrivacyData = response.getResult().getData();
                                classDetails(getPrivacyData);
                            }else {
                                ToastUtils.showToastLong(activity, "  " +response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ClassDetailResponse> call, BaseResponse baseResponse) {
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




    private void getBundleData() {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());

            if(activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID)) {
                classScheduleId = bundle.getString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID);
            }
            if(!TextUtils.isEmpty(classScheduleId)) {
                bundleScheduleIdStr=classScheduleId;
                Log.d("playlist_response", "Passed Background track : "
                        + bundleScheduleIdStr +"City id"+ bundleScheduleIdStr);
            }
        }
    }
    private void classDetails(ClassDetailData classDetailData) {

        activity.getBinding().tvName.setText(classDetailData.getStudioName());

        String todayAsString = StringUtils.dateFormat(classDetailData.getDays());
        String timeStart = StringUtils.timeFormat(classDetailData.getStartTime());
        String timeEnd = StringUtils.timeFormat(classDetailData.getEndTime());
        activity.getBinding().tvTime.setText(classDetailData.getDay() + "," + todayAsString + "," + timeStart.toString() + "-" + timeEnd.toString());
        if (classDetailData.getStudioImage() != null && !classDetailData.getStudioImage().equalsIgnoreCase("")) {
            Picasso.with(activity).load(classDetailData.getStudioImage()).placeholder(R.drawable.ic_image).fit().into(activity.getBinding().imStudioImage, new Callback() {
                @Override
                public void onSuccess() {
                    activity.getBinding().pbClassDetails.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    activity.getBinding().pbClassDetails.setVisibility(View.GONE);
                }

            });
        } else {
            activity.getBinding().imStudioImage.setImageResource(R.drawable.ic_image);
            activity.getBinding().pbClassDetails.setVisibility(View.GONE);
        }


        // latitude and longitude
        if (classDetailData.getLatitude() != null && classDetailData.getLongitude() != null && classDetailData.getStudioName() != null) {
            double mmLag = Double.parseDouble(classDetailData.getLatitude());
            double mmLng = Double.parseDouble(classDetailData.getLongitude());

            mapFragment = (SupportMapFragment)activity.getSupportFragmentManager().findFragmentById(R.id.map);
            */
/*mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(mmLag, mmLng)).title(classDetailData.getStudioName());

                    // Changing marker icon
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gon));
                    googleMap.addMarker(marker);
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(mmLag, mmLng)).zoom(17).build();
                    googleMap.animateCamera(CameraUpdateFactory
                            .newCameraPosition(cameraPosition));
                }
            });*//*

        }
    }

    //    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        googleMap.getUiSettings().setMapToolbarEnabled(false);
//        googleMap.getUiSettings().setZoomControlsEnabled(false);
//        if (checkPermission()) {
//            googleMap.setMyLocationEnabled(true);
//        }
//        try {
//            double mmLag = Double.parseDouble(classDetailData.getLatitude());
//            double mmLng = Double.parseDouble(classDetailData.getLongitude());
//            MarkerOptions marker = new MarkerOptions().position(new LatLng(mmLag, mmLng)).title(classDetailData.getStudioName());
//
//            // Changing marker icon
//            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_gon));
//            googleMap.addMarker(marker);
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(new LatLng(mmLag, mmLng)).zoom(17).build();
//            googleMap.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(cameraPosition));
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//    }
//    private void setMap(){
//        mapFragment = (SupportMapFragment)activity.getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//    }







}
*/
