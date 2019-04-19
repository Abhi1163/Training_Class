package com.hobbyer.android.viewmodel.activity.intro;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.request.FavouriteStudioRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.videos.VideosCategoryVideo;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.intro.IntroSeriesActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import com.hobbyer.android.widget.CustomDialog;

import hb.xvideoplayer.MxVideoPlayer;
import retrofit2.Call;

public class IntroSeriesActivityViewModel extends ActivityViewModel<IntroSeriesActivity> implements View.OnClickListener {

    String favourite_key = null;
    private IntroSeriesActivity activity;
    private CustomDialog dialog;
    private VideosCategoryVideo videosCategoryVideo;
    private TextView tvOk, tvMessage;

    public IntroSeriesActivityViewModel(IntroSeriesActivity activity) {
        super(activity);
        this.activity = activity;

        getBindleData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }

        setToolbar();
        countPointConnection();

        setVideoPlayer();

    }

    private void setToolbar() {
        activity.getBinding().toolbarSecond.ivBacks.setVisibility(View.VISIBLE);
        activity.getBinding().toolbarSecond.ivBacks.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbarSecond.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbarSecond.tvTitle.setText(R.string.intro_series);
        activity.getBinding().toolbarSecond.tvTitle.setText(videosCategoryVideo.getVideoName());
        activity.getBinding().toolbarSecond.tvRights.setVisibility(View.VISIBLE);
        activity.getBinding().toolbarSecond.ivLocations.setVisibility(View.VISIBLE);
        activity.getBinding().toolbarSecond.ivLocations.setImageResource(R.mipmap.ic_forward);
        activity.getBinding().toolbarSecond.ivBacks.setOnClickListener(this);
        activity.getBinding().toolbarSecond.ivLocations.setOnClickListener(this);
        activity.getBinding().toolbarSecond.ivLike.setImageResource(R.mipmap.ic_heart);
        activity.getBinding().toolbarSecond.ivLike.setVisibility(View.VISIBLE);
        activity.getBinding().toolbarSecond.ivLike.setOnClickListener(this);
        activity.getBinding().toolbarSecond.ivUnLikes.setOnClickListener(this);
    }

    private void getBindleData() {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            bundle.setClassLoader(getClass().getClassLoader());
            if (activity.getIntent().hasExtra(AppConstant.BUNDLE_KEY.VIDEOS_LIST)) {
                videosCategoryVideo = bundle.getParcelable(AppConstant.BUNDLE_KEY.VIDEOS_LIST);
            }
        }
    }

    private void setVideoPlayer() {
        if (!TextUtils.isEmpty(videosCategoryVideo.getVideoUrl())) {
            activity.getBinding().vvVideo.startPlay(videosCategoryVideo.getVideoUrl(), MxVideoPlayer.SCREEN_LAYOUT_NORMAL, videosCategoryVideo.getVideoName());
        }


        if (videosCategoryVideo.getLiked_status() != null && !videosCategoryVideo.getLiked_status().equalsIgnoreCase("")) {
            if (videosCategoryVideo.getLiked_status().equalsIgnoreCase("0")) {

                activity.getBinding().toolbarSecond.ivLike.setVisibility(View.VISIBLE);
                activity.getBinding().toolbarSecond.ivUnLikes.setVisibility(View.INVISIBLE);
            } else if (videosCategoryVideo.getLiked_status().equalsIgnoreCase("1")) {
                activity.getBinding().toolbarSecond.ivLike.setVisibility(View.INVISIBLE);
                activity.getBinding().toolbarSecond.ivUnLikes.setVisibility(View.VISIBLE);
            } else {


            }
        }

        activity.getBinding().tvInstructor.setText(videosCategoryVideo.getInstructor());
        activity.getBinding().tvActivityText.setText(videosCategoryVideo.getInstructor());
        activity.getBinding().tvBodyFocusText.setText(videosCategoryVideo.getBody_focus());
        activity.getBinding().tvEquipment.setText(videosCategoryVideo.getEquipment_needed());
        activity.getBinding().tvHowTo.setText(videosCategoryVideo.getHow_to_prepare());
        activity.getBinding().tvAbout.setText(videosCategoryVideo.getDescription());
        activity.getBinding().tvVideoName.setText(videosCategoryVideo.getVideoName());

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
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBacks:
                finish();
                break;
            case R.id.ivLike:
                activity.getBinding().toolbarSecond.ivLike.setVisibility(View.GONE);
                activity.getBinding().toolbarSecond.ivUnLikes.setVisibility(View.VISIBLE);
                favourite_key = "1";
                favouriteStudioConnection(videosCategoryVideo.getId());
                break;
            case R.id.ivUnLikes:
                activity.getBinding().toolbarSecond.ivLike.setVisibility(View.VISIBLE);
                activity.getBinding().toolbarSecond.ivUnLikes.setVisibility(View.GONE);
                favourite_key = "0";
                favouriteStudioConnection(videosCategoryVideo.getId());
                break;
        }
    }


    /*favouriteStudioConnection*/
    private void favouriteStudioConnection(String studioId) {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                FavouriteStudioRequest favouriteStudioRequest = new FavouriteStudioRequest();
                favouriteStudioRequest.setUser_id("" + userModel.getId());
                favouriteStudioRequest.setStudio_id(studioId);
                favouriteStudioRequest.setFavourite_key(favourite_key);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.favouriteStudio(favouriteStudioRequest).enqueue(new BaseCallback<BaseResponse>(activity) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                showDialog(response.getMessage());
                            } else {
                                ToastUtils.showToastLong(activity, "  " + response.getMessage());
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
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }


    private void showDialog(String message) {
        dialog = new CustomDialog(activity, R.layout.row_message_city);
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


    public void countPointConnection() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                CountPointRequest countPointRequest = new CountPointRequest();
                countPointRequest.setUserId("" + userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.count_Point(countPointRequest).enqueue(new BaseCallback<CountPointResponse>(activity) {
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
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
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

        activity.getBinding().toolbarSecond.tvRights.setText(point.getUser_point() + " Points ");
    }


}