package com.hobbyer.android.view.activities.findclassdetials;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.ClassDetailRequest;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.request.FavouriteStudioRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailData;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailResponse;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.ActivityGymTimeBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.filter.LocationActivity;
import com.hobbyer.android.view.activities.mybooking.ActivityMyBooking;
import com.hobbyer.android.view.activities.review.Review_Activity;
import com.hobbyer.android.widget.CustomDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import retrofit2.Call;


public class GymTimeActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static int MY_BOOKING_REQ_CODE = 208;
    public Bundle bundle;
    double mmLag, mmLng;
    float rating;
    String ratingCount;
    String favourite_key = null;
    ClassDetailData classDetailData;
    String StudioId = null;
    String timeStarts;
    ClassDetailData classDetail;
    private ActivityGymTimeBinding binding;
    private Context context;
    private String result;
    private String classScheduleId, date;
    private String bundleScheduleIdStr, dateCurrent = "";
    private MapView mapView;
    private GoogleMap gmap;
    private CustomDialog dialog;
    private Activity activity;
    private TextView tvOk, tvMessage;
    private ImageView ivGymTimeBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gym_time);
        context = GymTimeActivity.this;
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(GymTimeActivity.this, context.getResources().getColor(R.color.colorPrimary));
        }
        getBundleData();
        countPointConnection();
        classDetailsConnection();
        setToolbar();
        binding.btnReserveClass.setOnClickListener(this);
        binding.btnCancelClass.setOnClickListener(this);
        /* binding.btnReserveClass.setText("Reserve class");*/
        classDetail = PreferenceUtils.getClassModel();
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        binding.btReviews.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private void setToolbar() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
    //  binding.toolbar.tvTitle.setText(classDetailData.getClassName());
     //  binding.toolbar.tvTitle.setText("Gym Time");
        binding.toolbar.ivLike.setVisibility(View.VISIBLE);
        binding.toolbar.ivLike.setOnClickListener(this);
        binding.toolbar.ivUnLike.setOnClickListener(this);
        binding.toolbar.ivEvent.setVisibility(View.VISIBLE);
        binding.toolbar.tvRight.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.toolbar.ivEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnReserveClass:
                Intent reserve = new Intent(context, ActivityMyBooking.class);
                Bundle bundleReserve = new Bundle();
                bundleReserve.putInt(AppConstant.BUNDLE_KEY.RESERVED, classDetail.getIsReserved());
                bundleReserve.putString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID, bundleScheduleIdStr);
                bundleReserve.putString(AppConstant.BUNDLE_KEY.DATE, dateCurrent);
                reserve.putExtras(bundleReserve);
                startActivityForResult(reserve, MY_BOOKING_REQ_CODE);

                break;
            case R.id.btnCancelClass:
                Intent cancel = new Intent(context, ActivityMyBooking.class);
                Bundle bundleCancel = new Bundle();
                bundleCancel.putInt(AppConstant.BUNDLE_KEY.RESERVED, classDetail.getIsReserved());
                bundleCancel.putString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID, bundleScheduleIdStr);
                bundleCancel.putString(AppConstant.BUNDLE_KEY.DATE, dateCurrent);
                cancel.putExtras(bundleCancel);
                startActivityForResult(cancel, MY_BOOKING_REQ_CODE);

                break;

            case R.id.ivBack:
                finish();
                break;
            case R.id.ivLike:
                binding.toolbar.ivLike.setVisibility(View.GONE);
                binding.toolbar.ivUnLike.setVisibility(View.VISIBLE);
                favourite_key = "1";
                favouriteStudioConnection();
                break;
            case R.id.ivUnLike:
                binding.toolbar.ivLike.setVisibility(View.VISIBLE);
                binding.toolbar.ivUnLike.setVisibility(View.GONE);
                favourite_key = "0";
                favouriteStudioConnection();
                break;
            case R.id.ivEvent:
                classEvent();
                break;
            case R.id.btReviews:
                Bundle bundle1 = new Bundle();
                ActivityController.startActivity(context, Review_Activity.class);

        }
    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_BOOKING_REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            result = data.getStringExtra("ok");
            binding.btnReserveClass.setText(result);
            //classText.set(result);
        }

    }*/

    @Override
    protected void onRestart() {
        super.onRestart();
        classDetailsConnection();
    }

    /*classDetailsConnection*/
    private void classDetailsConnection() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                ProgressDialogUtils.show(context);
                ClassDetailRequest classDetailRequest = new ClassDetailRequest();
                classDetailRequest.setUser_id("" + userModel.getId());
                classDetailRequest.setClass_schedule_id(bundleScheduleIdStr);
                classDetailRequest.setDate(dateCurrent);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.class_Details(classDetailRequest).enqueue(new BaseCallback<ClassDetailResponse>(this) {
                    @Override
                    public void onSuccess(ClassDetailResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ClassDetailData getPrivacyData = response.getResult().getData();
                                classDetails(getPrivacyData);
                                ClassDetailData classDetailData = createClassModel(getPrivacyData);
                                PreferenceUtils.saveClassModel(classDetailData);
                            } else {
                                ToastUtils.showToastLong(context, "  " + response.getMessage());
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
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    /*favouriteStudioConnection*/
    private void favouriteStudioConnection() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                FavouriteStudioRequest favouriteStudioRequest = new FavouriteStudioRequest();
                favouriteStudioRequest.setUser_id("" + userModel.getId());
                favouriteStudioRequest.setStudio_id(StudioId);
                favouriteStudioRequest.setFavourite_key(favourite_key);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.favouriteStudio(favouriteStudioRequest).enqueue(new BaseCallback<BaseResponse>(this) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                showDialog(response.getMessage());
                            } else {
                                ToastUtils.showToastLong(context, "  " + response.getMessage());
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
        } else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());

           /* if (getIntent().hasExtra(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID)) {
                classScheduleId = bundle.getString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID);
            } */
            if (getIntent().hasExtra(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID)) {
                classScheduleId = bundle.getString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID);
            }
            if (getIntent().hasExtra(AppConstant.BUNDLE_KEY.DATE)) {
                date = bundle.getString(AppConstant.BUNDLE_KEY.DATE);
            }
            if (!TextUtils.isEmpty(classScheduleId)) {
                bundleScheduleIdStr = classScheduleId;
                Log.d("playlist_response", "Passed Background track : "
                        + bundleScheduleIdStr + "City id" + bundleScheduleIdStr);
            }
            if (!TextUtils.isEmpty(date)) {
                dateCurrent = date;
                Log.d("playlist_response", "Passed Background track : "
                        + dateCurrent + "City id" + dateCurrent);
            }
        }
    }

    /*Set Result class Details*/
    private void classDetails(ClassDetailData classDetailData) {

        if (classDetailData.getIsReserved() == 0) {
            binding.btnReserveClass.setVisibility(View.VISIBLE);
            binding.btnCancelClass.setVisibility(View.GONE);
        } else if (classDetailData.getIsReserved() == 1) {
            binding.btnReserveClass.setVisibility(View.GONE);
            binding.btnCancelClass.setVisibility(View.VISIBLE);
        }
        binding.tvName.setText(classDetailData.getStudioName());
        StudioId = classDetailData.getStudioId();
        String todayAsString = StringUtils.dateFormat(classDetailData.getDays());
        String timeStart = StringUtils.timeFormat(classDetailData.getStartTime());
        String timeEnd = StringUtils.timeFormat(classDetailData.getEndTime());
        binding.tvTime.setText(classDetailData.getDay() + "," + todayAsString + "," + timeStart.toString() + "-" + timeEnd.toString());
        binding.tvClassName.setText(classDetailData.getClassName());
        binding.tvAbout.setText(classDetailData.getHowToPrepare());
        binding.tvArrive.setText(classDetailData.getHowToPrepare());
        binding.toolbar.tvTitle.setText(classDetailData.getClassName());

        if (classDetailData.getLiked_status() != null && !classDetailData.getLiked_status().equalsIgnoreCase("")) {
            if (classDetailData.getLiked_status().equalsIgnoreCase("0")) {
                binding.toolbar.ivLike.setVisibility(View.VISIBLE);
                binding.toolbar.ivUnLike.setVisibility(View.GONE);
            } else if (classDetailData.getLiked_status().equalsIgnoreCase("1")) {
                binding.toolbar.ivLike.setVisibility(View.GONE);
                binding.toolbar.ivUnLike.setVisibility(View.VISIBLE);
            } else {

            }
        }
        if (classDetailData.getRatingCount() != 0) {
            rating = Float.parseFloat(classDetailData.getRating());
            ratingCount = Integer.toString(classDetailData.getRatingCount());
            binding.ratingBar.setRating(rating);
            binding.tvRating.setText(rating + " / " + ratingCount);
        } else {

            binding.ratingBar.setVisibility(View.GONE);
            binding.tvRating.setText(classDetailData.getRating());
            binding.tvRating.setVisibility(View.VISIBLE);
        }

        binding.tvPoint.setText(classDetailData.getPoints() + " Points");
        binding.tvAddress.setText(classDetailData.getAddress());
        binding.tvAbout.setText(classDetailData.getDescription());
        if (classDetailData.getStudioImage() != null && !classDetailData.getStudioImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(classDetailData.getStudioImage()).placeholder(R.drawable.ic_image).fit().into(binding.imStudioImage, new Callback() {
                @Override
                public void onSuccess() {
                    binding.pbClassDetails.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    binding.pbClassDetails.setVisibility(View.GONE);
                }

            });
        } else {
            binding.imStudioImage.setImageResource(R.drawable.ic_image);
            binding.pbClassDetails.setVisibility(View.GONE);
        }
        if (classDetailData.getLatitude() != null && classDetailData.getLongitude() != null && classDetailData.getStudioName() != null) {
            mmLag = Double.parseDouble(classDetailData.getLatitude());
            mmLng = Double.parseDouble(classDetailData.getLongitude());
        }

    }


    private void classEvent() {

        if (classDetail == null) {
            return;
        }
        UserModel userModel = PreferenceUtils.getUserModel();
        if (userModel == null) {
            return;
        }
        Calendar beginTime = Calendar.getInstance();
        String[] mDate = classDetail.getStartDate().split(" ");
        int mon = StringUtils.changeDateFormat(mDate[0])[1];
        int day = StringUtils.changeDateFormat(mDate[0])[2];
        int year = StringUtils.changeDateFormat(mDate[0])[0];
        timeStarts = StringUtils.timeFormatCalender(classDetail.getStartTime());
        String[] mTime = timeStarts.split(" ");
        int StartMin = StringUtils.changeTimeFormat(mTime[0])[1];
        int StartHour = StringUtils.changeTimeFormat(mTime[0])[0];
        beginTime.set(year, mon, day, StartHour, StartMin);
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, mon, day, StartHour, StartMin);
        Intent intent = new Intent(Intent.ACTION_INSERT)
       // Intent intent = new Intent(Intent.ACTION_EDIT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, classDetail.getStudioName())
                .putExtra(CalendarContract.Events.CAN_INVITE_OTHERS, 1)
                .putExtra(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1)
                .putExtra(CalendarContract.Events.CALENDAR_DISPLAY_NAME, classDetail.getStudioName())
                .putExtra(CalendarContract.Events.DESCRIPTION, classDetail.getStudioName())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, String.valueOf(classDetail.getLatitude() + "" + classDetail.getLongitude()))
                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                .putExtra(Intent.EXTRA_EMAIL, userModel.getEmail());

        try
        {
            startActivity(intent);
        }
        catch (ActivityNotFoundException ErrVar)
        {
            Toast.makeText(this, "Install Calender App", Toast.LENGTH_LONG).show();
        }


       // startActivity(intent);

    }

    private ClassDetailData createClassModel(ClassDetailData classDetailData) {
        ClassDetailData classModel = new ClassDetailData();
        classModel.setId(classDetailData.getId());
        classModel.setClassId(classDetailData.getClassId());
        classModel.setStudioId(classDetailData.getStudioId());
        classModel.setCategoryId(classDetailData.getCategoryId());
        classModel.setDays(classDetailData.getDays());
        classModel.setStartTime(classDetailData.getStartTime());
        classModel.setEndTime(classDetailData.getEndTime());
        classModel.setDuration(classDetailData.getDuration());
        classModel.setCompleteStatus(classDetailData.getCompleteStatus());
        classModel.setCreatedAt(classDetailData.getCreatedAt());
        classModel.setUpdatedAt(classDetailData.getUpdatedAt());
        classModel.setUserId(classDetailData.getUserId());
        classModel.setClassName(classDetailData.getClassName());
        classModel.setStartDate(classDetailData.getStartDate());
        classModel.setEndDate(classDetailData.getEndDate());
        classModel.setAddress(classDetailData.getAddress());
        classModel.setWeekDays(classDetailData.getWeekDays());
        classModel.setInstructorName(classDetailData.getInstructorName());
        classModel.setCost(classDetailData.getCost());
        classModel.setPoints(classDetailData.getPoints());
        classModel.setSeats(classDetailData.getSeats());
        classModel.setDescription(classDetailData.getDescription());
        classModel.setHowToPrepare(classDetailData.getHowToPrepare());
        classModel.setClassImage(classDetailData.getClassImage());
        classModel.setDay(classDetailData.getDay());
        classModel.setStudioName(classDetailData.getStudioName());
        classModel.setLatitude(classDetailData.getLatitude());
        classModel.setLongitude(classDetailData.getLongitude());
        classModel.setIsReserved(classDetailData.getIsReserved());
        classModel.setReservationId(classDetailData.getReservationId());
        classModel.setReserveId(classDetailData.getReserveId());
        classModel.setBookedUserId(classDetailData.getBookedUserId());
        classModel.setClassScheduleId(classDetailData.getClassScheduleId());
        classModel.setBookingDate(classDetailData.getBookingDate());
        classModel.setBookingStartTime(classDetailData.getBookingStartTime());
        classModel.setBookingEndTime(classDetailData.getBookingEndTime());
        classModel.setReservationStatus(classDetailData.getReservationStatus());
        classModel.setPaidPoints(classDetailData.getPaidPoints());
        classModel.setBookedCount(classDetailData.getBookedCount());
        return classModel;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        gmap.setIndoorEnabled(true);
        UiSettings uiSettings = gmap.getUiSettings();
        uiSettings.setIndoorLevelPickerEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setMapToolbarEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        LatLng ny = new LatLng(mmLag, mmLng);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(ny);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_icon));
        gmap.addMarker(markerOptions);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(ny));
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setScrollGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        uiSettings.setZoomGesturesEnabled(false);
    }


    private void showDialog(String message) {
        dialog = new CustomDialog(context, R.layout.row_message);
        tvOk = dialog.findViewById(R.id.btnOk);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }


    private void countPointConnection() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                CountPointRequest countPointRequest = new CountPointRequest();
                countPointRequest.setUserId("" + userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.count_Point(countPointRequest).enqueue(new BaseCallback<CountPointResponse>((AppCompatActivity) activity) {
                    @Override
                    public void onSuccess(CountPointResponse response) {

                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CountPointData countPointData = response.getResult().getData();
                                CountPointData countPoint = createCountPoint(countPointData);
                                PreferenceUtils.savePoint(countPoint);
                                pointData();
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<CountPointResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    private CountPointData createCountPoint(CountPointData countPointData) {
        CountPointData countPointData1 = new CountPointData();
        countPointData1.setFriend_count(countPointData.getFriend_count());
        countPointData1.setFavourite_studios(countPointData.getFavourite_studios());
        countPointData1.setCompleted_class(countPointData.getCompleted_class());
        countPointData1.setUpcoming_class(countPointData.getUpcoming_class());
        countPointData1.setUser_point(countPointData.getUser_point());


        return countPointData1;
    }

    private void pointData() {
        CountPointData point = PreferenceUtils.getPoint();

        binding.toolbar.tvRight.setText(point.getUser_point() + " Points ");
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

