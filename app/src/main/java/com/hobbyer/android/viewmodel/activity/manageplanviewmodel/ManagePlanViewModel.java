package com.hobbyer.android.viewmodel.activity.manageplanviewmodel;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanData;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanResponse;
import com.hobbyer.android.api.response.auth.mangeplan.ManagePlanResult;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.DialogReviewSelectionBinding;
import com.hobbyer.android.databinding.DilogCongratulationBinding;
import com.hobbyer.android.interfaces.OnItemClickListenerr;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.billing.BillingActivity;
import com.hobbyer.android.view.activities.home.HomeActivity;
import com.hobbyer.android.view.activities.manageplan.ManagePlanActivity;
import com.hobbyer.android.view.activities.privacypolicy.PrivacyPolicyActivity;
import com.hobbyer.android.view.activities.termandcondition.TermsAndConditionsActivity;
import com.hobbyer.android.view.adapter.AdapterManagePlan;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;


public class ManagePlanViewModel extends ActivityViewModel implements View.OnClickListener {
    private ManagePlanActivity activity;
    private boolean position;
    private int memberId, isCurrent, duration;
    private String price = "", points = "", memebershipType = "", fromMember = "";
    private List<ManagePlanData> data = new ArrayList<>();
    private List datalist = new ArrayList();
    private String isFrom;
    private int alreadySeletedPlan = 0;
    private int choosePlan = 0;
    private String  expDate;
    private  String memberType;
    private   Dialog dialog;
    private List<ManagePlanData> managePlanDataList = new ArrayList<>();
    private AdapterManagePlan adapterManagePlan;
    public ManagePlanViewModel(ManagePlanActivity activity) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        getIntentData();
        isFrom = SharedPref.getStringPreferences(activity, AppConstant.CHOSSEMEMBERSHIP);
        setRecyclerView();
        getPlanApi();
    }
    private void getIntentData() {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("BillingMember")) {
                fromMember = bundle.getString("BillingMember");
            }
        }
    }
    private void removeList() {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getMembershipType().equals("free")/* && data.get(i).getIsCurrent() == 1*/) {
                    data.remove(i);
                }
                if (data.get(i).getIsCurrent() == 1) {
                    data.get(i).setSelected(true);
                } else {
                    data.get(i).setSelected(false);
                }
            }
            adapterManagePlan.notifyDataSetChanged();
        }
    }
    private void addList() {
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
//                if (data.get(i).getMembershipType().equals("free")/* && data.get(i).getIsCurrent() == 1*/) {
//                    data.remove(i);
//                }
                if (data.get(i).getIsCurrent() == 1) {
                    data.get(i).setSelected(true);
                } else {
                    data.get(i).setSelected(false);
                }
            }
            adapterManagePlan.notifyDataSetChanged();
        }
    }
    private void setRecyclerView() {
        adapterManagePlan = new AdapterManagePlan(activity, data, new OnItemClickListenerr() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.rbManage:
                        for (int i = 0; i < data.size(); i++) {
                            if (position == i) {
                                if (data.get(i).getIsCurrent() == 1) {
                                    choosePlan = 0;
                                    data.get(position).setIsCurrent(0);
                                } else {
                                    choosePlan = data.get(position).getId();
                                    data.get(position).setIsCurrent(1);
                                }
                            } else {
                                data.get(i).setIsCurrent(0);
                            }
                        }
                        memberId = data.get(position).getId();
                        price = data.get(position).getMembership();
                        points = data.get(position).getPoints();
                        isCurrent = data.get(position).getIsCurrent();
                        memebershipType = data.get(position).getMembershipType();
                        adapterManagePlan.notifyDataSetChanged();
                        break;
                }
            }
        });
/*        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        activity.getBinding().rvManagePlan.setLayoutManager(layoutManager);*/
        activity.getBinding().rvManagePlan.setAdapter(adapterManagePlan);
        activity.getBinding().rvManagePlan.setHasFixedSize(true);
        activity.getBinding().rvManagePlan.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        /*activity.getBinding().rvManagePlan.setItemAnimator(new DefaultItemAnimator());
        activity.getBinding().rvManagePlan.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));*/
    }
    public boolean isCardChosen() {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getIsCurrent() == 1) {
                return true;
            }
        }
        return false;
    }
    private void getPlanApi() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                long userId = userModel.getId();
                int userid = (int) userId;
                AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
                authWebServices.getplan(userid).enqueue(new BaseCallback<ManagePlanResponse>(activity) {
                    @Override
                    public void onSuccess(ManagePlanResponse response) {
                        ProgressDialogUtils.dismiss();
                        if (response != null) {
                            if (response.getStatus() == 1) {
                               // expDate = managePlanResult.getDate();
                                expDate =  response.getResult().getDate();

                                /*ManagePlanResult managePlanResult = new ManagePlanResult();*/


                                  PrefManager.savePreferences(activity, AppConstant.DATE, expDate);
                                for (int i = 0; i < managePlanDataList.size(); i++) {
                                    isCurrent = managePlanDataList.get(i).getIsCurrent();
                                    memberType = managePlanDataList.get(i).getMembershipType();
                                }
                                data.clear();

                                if (isFrom.equals("withoutfree")) {

                                    for (int i = 0; i < response.getResult().getContentList().size(); i++) {


                                        if (response.getResult().getContentList().get(i).getMembershipType().equalsIgnoreCase("")) {


                                            data.add(response.getResult().getContentList().get(i));
                                        }


                                    }

                                }
                                else {

                                    data.addAll(response.getResult().getContentList());


                                }
                                    adapterManagePlan.notifyDataSetChanged();
                                    if (data != null) {
                                        for (int i = 0; i < data.size(); i++) {
                                            if (data.get(i).getIsCurrent() == 1) {
                                                alreadySeletedPlan = data.get(i).getId();
                                                choosePlan = data.get(i).getId();
                                                memberId = data.get(i).getId();
                                                price = data.get(i).getMembership();
                                                points = data.get(i).getPoints();
                                                duration = data.get(i).getDuration();
                                                isCurrent = data.get(i).getIsCurrent();
                                                memebershipType = data.get(i).getMembershipType();
                                                //   duration=data.get(i).getDuration();
                                            }
                                        }
                                    }
                               /* if (isFrom.equals("")) {
                                    removeList();
                                } else if (isFrom.equals("free")) {
                                    addList();
                                } else {
                                    adapterManagePlan.notifyDataSetChanged();
                                }*/
                                }
                            }
                        }

                    @Override
                    public void onFail(Call<ManagePlanResponse> call, BaseResponse baseResponse) {
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
    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.btConfirm:
                if (isCardChosen()) {
                    if (choosePlan == alreadySeletedPlan) {
                        ToastUtils.showToastShort(activity, "*This plan is already purchased.");
                    } else {
                        dialogReview();
                    }
                    // confirmplanApi();
                } else {
                    ToastUtils.showToastShort(activity, "*Please select plan.");
                }
                //   dialogReview();
                break;
            case R.id.tvPrivacy:
                break;
            default:
                break;
        }
    }
    private void confirmplanApi() {
        if (CommonUtils.isOnline(activity)) {
            ProgressDialogUtils.show(activity);
            UserModel userModel = PreferenceUtils.getUserModel();
            if (userModel == null) {
                return;
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("user_id", userModel.getId());
            jsonObject.addProperty("membership_id", memberId);
            AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
            authWebServices.updateMembership(jsonObject).enqueue(new BaseCallback<BaseResponse>(activity) {
                @Override
                public void onSuccess(BaseResponse response) {
                    if (response != null) {
                        if (response.getStatus() == 1) {
                            ToastUtils.showToastShort(activity, response.getMessage());
                            openCongrats();
                        } else {
                            ToastUtils.showToastShort(activity, response.getMessage());
                        }
                    }
                    ProgressDialogUtils.dismiss();
                }
                @Override
                public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                    ProgressDialogUtils.dismiss();
                    ToastUtils.showToastShort(activity, baseResponse.getMessage());
                }
            });
        } else {
            Toast.makeText(activity, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }
    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.manage_plan);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }
    private void dialogReview() {
        DialogReviewSelectionBinding reviewSelectionBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),
                R.layout.dialog_review_selection, null, false);
        dialog = DialogUtils.createDialog(activity, reviewSelectionBinding.getRoot());
        if (duration == 1) {
            if (price.equals("Free Trial")) {
                reviewSelectionBinding.tvPoints.setText("$0 per month ");
            }
            // reviewSelectionBinding.tvPrice.setText(price + " - Points Plan");
            reviewSelectionBinding.tvPoints.setText("$" + price + ".00");
            reviewSelectionBinding.tvPrice.setText(points + " - Points Plan");
        } else if (duration == 2) {
            if (price.equals("Free Trial")) {
                reviewSelectionBinding.tvPoints.setText("$ 0 per month");
            }
            reviewSelectionBinding.tvPoints.setText("$" + price + ".00");
            reviewSelectionBinding.tvPrice.setText(points + " - Points Plan");
        } else {
            if (price.equals("Free Trial")) {
                reviewSelectionBinding.tvPoints.setText("$ 0 per year");
            }
            reviewSelectionBinding.tvPoints.setText("$" + price + ".00");
            reviewSelectionBinding.tvPrice.setText(points + " - Points Plan");
        }
       /* if (price.equals("Free Trial")) {
            reviewSelectionBinding.tvPoints.setText("$" + price);
            reviewSelectionBinding.tvPrice.setText(points + " - Points Plan");
        } else {
            reviewSelectionBinding.tvPoints.setText("$" + price + ".00  per month");
            reviewSelectionBinding.tvPrice.setText(points + " - Points Plan");
        }*/
      /*  UserModel userModel = PreferenceUtils.getUserModel();
        String expDate = userModel.getExpiryDate();*/
/*
        expDate =  PrefManager.getPreferencesString(activity, AppConstant.DATE);
*/
        if (expDate==null || expDate.equalsIgnoreCase("0")) {
            reviewSelectionBinding.tvDate.setText("This will take effect from today");
        } else {
            reviewSelectionBinding.tvDate.setText("This will take effect on " + expDate);
        }
        reviewSelectionBinding.tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityController.startActivity(activity, PrivacyPolicyActivity.class);
            }
        });
        SpannableString ss = new SpannableString("You agree to the Terms of Use and");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                ActivityController.startActivity(activity, TermsAndConditionsActivity.class);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, 17, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        reviewSelectionBinding.tvTerms.setText(ss);
        reviewSelectionBinding.tvTerms.setMovementMethod(LinkMovementMethod.getInstance());
        reviewSelectionBinding.tvTerms.setHighlightColor(Color.TRANSPARENT);
        reviewSelectionBinding.btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getPlanApi();

                confirmplanApi();
                // openCongrats();
            }
        });
        reviewSelectionBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    private void openCongrats() {
        DilogCongratulationBinding congratulationBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dilog_congratulation, null, false);
        Dialog dialog = DialogUtils.createDialog(activity, congratulationBinding.getRoot());
        congratulationBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromMember != null && fromMember.equalsIgnoreCase("BillingMember")) {
                    ActivityController.startActivity(activity, HomeActivity.class, true);
                } else {
                    finish();
                }
                dialog.dismiss();
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