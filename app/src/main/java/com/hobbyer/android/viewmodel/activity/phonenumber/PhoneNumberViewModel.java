package com.hobbyer.android.viewmodel.activity.phonenumber;


import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.AddPhoneNumberRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.addphonenumber.AddPhoneNumberData;
import com.hobbyer.android.api.response.auth.addphonenumber.AddPhoneNumberResponse;
import com.hobbyer.android.api.response.auth.countrylist.CountryContentList;
import com.hobbyer.android.api.response.auth.countrylist.CountryListResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.DialogCountryCodeBinding;
import com.hobbyer.android.interfaces.ProfileInterface;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.ActivityController;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.RegexUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.membership.ChooseMembershipActivity;
import com.hobbyer.android.view.activities.otpscreen.OtpActivity;
import com.hobbyer.android.view.activities.phonenumber.PhoneNumberActivity;
import com.hobbyer.android.view.adapter.CountrySpinnerAdapter;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class PhoneNumberViewModel extends ActivityViewModel<PhoneNumberActivity> implements TextWatcher, View.OnClickListener {

    List<CountryContentList> contentList;
    private String phoneNumber,phoneCountry;
    private String phoneCode = "";
    private Dialog dialog;
    private DialogCountryCodeBinding dialogFeedbackRatingBinding;
    private String countryCode;
    private CountrySpinnerAdapter adapterReview;

    public PhoneNumberViewModel(PhoneNumberActivity activity) {
        super(activity);
        this.activity = activity;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();

        activity.getBinding().tvCountyCode.setOnClickListener(this);

        //  phoneCode = activity.getBinding().ietCountryCode.getText().toString().trim();
        clearText();
    }

    private void openDailog() {

        dialogFeedbackRatingBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_country_code, null, false);
        dialog = DialogUtils.createDialog(activity, dialogFeedbackRatingBinding.getRoot());
        dialogFeedbackRatingBinding.searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setRecycelView();
        countryConnection();


    }

    private void setRecycelView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        dialogFeedbackRatingBinding.rvCountry.setLayoutManager(layoutManager);


    }

    private void countryConnection() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.country_List().enqueue(new BaseCallback<CountryListResponse>(activity) {
                    @Override
                    public void onSuccess(CountryListResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                contentList = response.getResult().getContentList();
                                adapterReview = new CountrySpinnerAdapter(activity, contentList, new ProfileInterface() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        switch (view.getId()) {
                                            case R.id.tvName:
                                                countryCode =adapterReview.contentList.get(position).getPhonecode();

                                                if (countryCode != null) {


                                                    activity.getBinding().tvCountyCode.setText("+"+countryCode);

                                                    dialog.dismiss();
                                                } else {
                                                    ToastUtils.showToastShort(activity, "*Please select country code");

                                                }


                                                break;
                                        }

                                    }
                                });
                                dialogFeedbackRatingBinding.rvCountry.setAdapter(adapterReview);


                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<CountryListResponse> call, BaseResponse baseResponse) {
                        ToastUtils.showToastLong(activity, " " + baseResponse.getMessage());
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




    public void filter(String text) {

        List<CountryContentList> filteredName = new ArrayList<>();

        for (CountryContentList model : contentList) {

            if (model.getCountry().toLowerCase().contains(text.toLowerCase())) {
                filteredName.add(model);
            }

        }
        // binding.tvNumberingBroadcast.setText(count + " " +getString(R.string.of)+ " " + filteredName.size());

        adapterReview.updatedList(filteredName);

    }



    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.mEmailSubmit:
                if (isValid()) {
                    //  phoneCode = activity.getBinding().ietCountryCode.getText().toString().trim();
                    phoneConnection();
                }
                break;

            case R.id.tvCountyCode:
                openDailog();
                break;

        }
    }

    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.GONE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText(R.string.phone_Number);
    }

    private boolean isValid() {
        phoneNumber = activity.getBinding().etPhone.getText().toString().trim();
        if (StringUtils.isBlank(phoneNumber)) {
            activity.getBinding().etPhone.requestFocus();
            activity.getBinding().tvPhoneError.setVisibility(View.VISIBLE);
            activity.getBinding().tvPhoneError.setText(R.string.please_enter_phone);
            return false;
        } else if (!RegexUtils.isValidPhoneNumber(phoneNumber)) {
            activity.getBinding().etPhone.requestFocus();
            activity.getBinding().tvPhoneError.setVisibility(View.VISIBLE);
            activity.getBinding().tvPhoneError.setText(R.string.please_enter_valid_phone);
            return false;
        }

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        activity.getBinding().tvPhoneError.setVisibility(View.GONE);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void clearText() {
        activity.getBinding().etPhone.addTextChangedListener(this);
    }


    private void phoneConnection() {
        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                ProgressDialogUtils.show(activity);
                AddPhoneNumberRequest addPhoneRequest = new AddPhoneNumberRequest();
                addPhoneRequest.setUser_id("" + userModel.getId());
                addPhoneRequest.setPhone_number(phoneNumber);
                addPhoneRequest.setCountry_code(countryCode);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.addPhoneNumber(addPhoneRequest).enqueue(new BaseCallback<AddPhoneNumberResponse>(activity) {
                    @Override
                    public void onSuccess(AddPhoneNumberResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                AddPhoneNumberData getPrivacyData = response.getResult().getData();
                                phoneCountry=countryCode+phoneNumber;
                                Bundle bundle = new Bundle();
                                bundle.putLong(AppConstant.BUNDLE_KEY.USER_OTP, getPrivacyData.getOtp());
                                bundle.putString(AppConstant.BUNDLE_KEY.PHONE_NUMBER, phoneNumber);
                                bundle.putString(AppConstant.BUNDLE_KEY.COUNTRY_CODE, countryCode);
                                bundle.putString(AppConstant.BUNDLE_KEY.TYPE_PHONE, "phone_number");
                                bundle.putString(AppConstant.BUNDLE_KEY.TYPE_CODE, "country_code");
                             ActivityController.startActivity(activity, OtpActivity.class, bundle);
                             //   ActivityController.startActivity(activity, ChooseMembershipActivity.class, bundle);


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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivBack:
                finish();

                break;
        }

    }
}
