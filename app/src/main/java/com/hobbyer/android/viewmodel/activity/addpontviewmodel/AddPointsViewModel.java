package com.hobbyer.android.viewmodel.activity.addpontviewmodel;


import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.planlist.PlanListData;
import com.hobbyer.android.api.response.auth.planlist.PlanListResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.DialogReviewDetailsBinding;
import com.hobbyer.android.interfaces.OnItemClickListners;
import com.hobbyer.android.model.AddPointModel;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.addpoint.AddPointsActivity;
import com.hobbyer.android.view.adapter.AddPointsAdpater;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class AddPointsViewModel extends ActivityViewModel<AddPointsActivity> implements View.OnClickListener {
    private AddPointsActivity activity;
    private List<AddPointModel> list;
    private AddPointsAdpater adapterAddFriend;
    private OnItemClickListners listner;
    private String plans, point;
    private int planId;
    private Dialog dialog;

    public AddPointsViewModel(AddPointsActivity activity) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }

        LinearLayoutManager rvTopLiveLayoutManager = new LinearLayoutManager(activity.getBinding().rvAddPoints.getContext(), LinearLayoutManager.VERTICAL, false);
        activity.getBinding().rvAddPoints.setLayoutManager(rvTopLiveLayoutManager);

        getPlanList();
        setToolbar();
        countPointConnection();
    }




    private void getPlanList() {
        if (CommonUtils.isOnline(activity)) {

            try {

                ProgressDialogUtils.show(activity);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.getPlanList().enqueue(new BaseCallback<PlanListResponse>((AppCompatActivity) activity) {

                    @Override
                    public void onSuccess(PlanListResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1)
                            {
                                ArrayList<PlanListData> planListData = (ArrayList<PlanListData>) response.getPlanListResult().getContentList();
                                adapterAddFriend = new AddPointsAdpater(activity, planListData, new OnItemClickListners() {
                                    @Override
                                    public void onItemClick(View view, int postion, PlanListData listData) {
                                        plans = listData.getPlans();
                                        point = listData.getPoints();
                                        planId = listData.getId();
                                        openPurchaseDialog();
                                    }


                                });
                                activity.getBinding().rvAddPoints.setAdapter(adapterAddFriend);

                            }
                            else {
                                ToastUtils.showToastShort(activity, response.getMessage());


                            }
                        }
                        else {
                            ToastUtils.showToastShort(activity, activity.getString(R.string.something_went));

                        }
                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<PlanListResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastShort(activity, baseResponse.getMessage());


                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();

        }


    }

    private void openPurchaseDialog() {
        DialogReviewDetailsBinding reviewDetailsBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_review_details, null, false);
        dialog = DialogUtils.createDialog(activity, reviewDetailsBinding.getRoot());
        reviewDetailsBinding.tvPlans.setText(plans + " -  Points Pack");
        reviewDetailsBinding.tvPoints.setText("$ " + point+".00");
      /*  UserModel userModel = PreferenceUtils.getUserModel();
        String expDate=userModel.getExpiryDate();*/
        String expDate = PrefManager.getPreferencesString(activity, AppConstant.DATE);

        reviewDetailsBinding.tvDate.setText(" Your months resets on "+expDate+" at which point up to 10 points  will automatically roll over");

        reviewDetailsBinding.btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                updatePlanApi();

            }
        });
        reviewDetailsBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }

    private void updatePlanApi() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);


                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("user_id", userModel.getId());
                jsonObject.addProperty("plan_id", planId);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.getUpdatePlan(jsonObject).enqueue(new BaseCallback<BaseResponse>(activity) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ToastUtils.showToastShort(activity, response.getMessage());

                                //  ToastUtils.showToastShort(activity, response.getMessage());
                            } else {
                                ToastUtils.showToastShort(activity, response.getMessage());
                            }
                        } else {
                            ToastUtils.showToastShort(getActivity(), getActivity().getResources().getString(R.string.something_went));

                        }
                        ProgressDialogUtils.dismiss();


                    }

                    @Override
                    public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();

                        ToastUtils.showToastShort(activity, baseResponse.getMessage());


                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setToolbar() {
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
        activity.getBinding().toolbar.tvTitle.setText(getActivity().getResources().getString(R.string.add_point));
        activity.getBinding().toolbar.tvRight.setVisibility(View.VISIBLE);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;

        }
    }

    private void countPointConnection() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if(userModel == null) {
                    return;
                }
                CountPointRequest countPointRequest = new CountPointRequest();
                countPointRequest.setUserId(""+userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.count_Point(countPointRequest).enqueue(new BaseCallback<CountPointResponse>(activity) {
                    @Override
                    public void onSuccess(CountPointResponse response) {

                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CountPointData countPointData = response.getResult().getData();
                                CountPointData   countPoint=   createCountPoint(countPointData);
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
        }else {
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
    private void pointData(){
        CountPointData point = PreferenceUtils.getPoint();

        activity.getBinding().toolbar.tvRight.setText(point.getUser_point() + " Points ");
    }


}
