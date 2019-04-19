package com.hobbyer.android.viewmodel.activity.mybookingviewmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.ReserveRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.reserve.ReserveResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.DialogCancelClassBinding;
import com.hobbyer.android.databinding.DialogConfirmationBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.view.activities.mybooking.ActivityMyBooking;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import hb.xvideoplayer.MxVideoPlayer;
import retrofit2.Call;


public class MyBookingViewModel extends ActivityViewModel implements View.OnClickListener {

    UserModel userModel;
    ClassDetailData classDetail;
    CountPointData point;
    private int flag = 1;
    private ActivityMyBooking activity;
    private Bundle bundle;
    private Context context;
    private String classScheduleId, date;
    private String bundleScheduleIdStr, dateCurrent = "";

    public MyBookingViewModel(ActivityMyBooking activity) {
        super(activity);
        this.activity = activity;

        userModel = PreferenceUtils.getUserModel();
        if (userModel == null) {
            return;
        }
        classDetail = PreferenceUtils.getClassModel();
        if (classDetail == null) {
            return;
        }
        point = PreferenceUtils.getPoint();
        if (point == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        getBundleData();
        setButtonText();


    }


    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.my_booking);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }


    private void getBundleData() {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());

            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID)) {
                classScheduleId = bundle.getString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID);
            }
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.DATE)) {
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

    private void setButtonText() {

        if (classDetail.getIsReserved() == 0) {
            activity.getBinding().btnConfirmReservation.setVisibility(View.VISIBLE);
            activity.getBinding().btnCancelReservation.setVisibility(View.GONE);
        } else if (classDetail.getIsReserved() == 1) {
            activity.getBinding().btnConfirmReservation.setVisibility(View.GONE);
            activity.getBinding().btnCancelReservation.setVisibility(View.VISIBLE);

        }
        System.out.println(classDetail.getPoints());
        activity.getBinding().tvPoints.setText(classDetail.getPoints() + " Points");
        String todayAsString = StringUtils.dateFormat(classDetail.getDays());
        String timeStart = StringUtils.timeFormat(classDetail.getStartTime());
        String timeEnd = StringUtils.timeFormat(classDetail.getEndTime());
        activity.getBinding().tvTimeAndDate.setText(classDetail.getDay() + "," + todayAsString + "," + timeStart.toString() + "-" + timeEnd.toString());
        activity.getBinding().tvClassName.setText(classDetail.getClassName());
        activity.getBinding().tvAddress.setText(classDetail.getAddress());
        if (classDetail.getStudioImage() != null && !classDetail.getStudioImage().equalsIgnoreCase("")) {
            Picasso.with(context).load(classDetail.getStudioImage()).placeholder(R.drawable.ic_image).fit().into(activity.getBinding().ivImage, new Callback() {
                @Override
                public void onSuccess() {
                    activity.getBinding().ivImage.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    activity.getBinding().ivImage.setVisibility(View.GONE);
                }

            });
        } else {
            activity.getBinding().ivImage.setImageResource(R.drawable.ic_image);
        }

    }

    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.btnConfirmReservation:
                congratulationDialog();

              //  reserveStudioConnection();
                break;
            case R.id.btnCancelReservation:
                cancelDialog();
            //    cancelStudioConnection();
                break;


        }

    }
    /*reserveDialog();*/

    private void reserveStudioConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                ReserveRequest reserveRequest = new ReserveRequest();
                reserveRequest.setClass_scheduled_id(bundleScheduleIdStr);
                reserveRequest.setClass_id(classDetail.getClassId());
                reserveRequest.setUser_id(userModel.getId());
                reserveRequest.setStudio_id(classDetail.getStudioId());
                reserveRequest.setClass_point(classDetail.getPoints());
                reserveRequest.setReserve_key(true);
                reserveRequest.setReserve_date(dateCurrent);
                reserveRequest.setBooking_start_time(classDetail.getStartTime());
                reserveRequest.setBooking_end_time(classDetail.getEndTime());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.reserveCancelClasses(reserveRequest).enqueue(new BaseCallback<ReserveResponse>(activity) {
                    @Override
                    public void onSuccess(ReserveResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ToastUtils.showToastShort(activity,"Class is reserved ");

                                finish();
                            //    congratulationDialog();

                            } else {
                                ToastUtils.showToastLong(activity, "  " + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ReserveResponse> call, BaseResponse baseResponse) {
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


    /*cancelDialog();*/
    private void cancelStudioConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                ReserveRequest reserveRequest = new ReserveRequest();
                reserveRequest.setClass_scheduled_id(bundleScheduleIdStr);
                reserveRequest.setClass_id(classDetail.getClassId());
                reserveRequest.setUser_id(userModel.getId());
                reserveRequest.setStudio_id(classDetail.getStudioId());
                reserveRequest.setClass_point(classDetail.getPoints());
                reserveRequest.setReserve_key(false);
                reserveRequest.setReserve_date(dateCurrent);
                reserveRequest.setReservation_id(classDetail.getReservationId());
                reserveRequest.setReserve_id(classDetail.getReserveId());
                reserveRequest.setBooking_start_time(classDetail.getStartTime());
                reserveRequest.setBooking_end_time(classDetail.getEndTime());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.reserveCancelClasses(reserveRequest).enqueue(new BaseCallback<ReserveResponse>(activity) {
                    @Override
                    public void onSuccess(ReserveResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ToastUtils.showToastShort(activity,"Class is canceled ");
                               // cancelDialog();
                                finish();

                            } else {
                                ToastUtils.showToastLong(activity, "  " + response.getMessage());
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ReserveResponse> call, BaseResponse baseResponse) {
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

    @Override
    public void onPause() {
        super.onPause();
        MxVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (MxVideoPlayer.backPress()) {
            return;
        }
        getActivity().setResult(Activity.RESULT_CANCELED);
        finish();
        //super.onBackPressed();
    }

    private void congratulationDialog() {
        DialogConfirmationBinding dialogConfirmationBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_confirmation, null, false);
        Dialog dialog = DialogUtils.createDialog(activity, dialogConfirmationBinding.getRoot());
        dialogConfirmationBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialogConfirmationBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


        dialogConfirmationBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Bundle bundle = new Bundle();
                bundle.putString("confirmDialog", "confirm");*/
                Intent i = new Intent();
                i.putExtra("ok", "Cancel class");
                getActivity().setResult(Activity.RESULT_OK, i);
                reserveStudioConnection();
                dialog.dismiss();
                finish();
            }
        });
    }

    private void cancelDialog() {
        DialogCancelClassBinding dialogCancelClassBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_cancel_class, null, false);
        Dialog dialog = DialogUtils.createDialog(activity, dialogCancelClassBinding.getRoot());
        dialogCancelClassBinding.btnCancelReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("ok", "Reserve class");
                getActivity().setResult(Activity.RESULT_OK, i);
                cancelStudioConnection();
                dialog.dismiss();
                finish();


            }
        });
        dialogCancelClassBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();


            }
        });
        dialogCancelClassBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();


            }
        });

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
