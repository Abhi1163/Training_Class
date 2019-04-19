package com.hobbyer.android.viewmodel.activity.privacypolicyviewmodel;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.hobbyer.android.R;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.view.activities.privacypolicy.PrivacyPolicyActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

public class PrivacyPolicyViewModel extends ActivityViewModel<PrivacyPolicyActivity> implements View.OnClickListener {
    private PrivacyPolicyActivity activity;
    private String isFromPrivacy = "";

    public PrivacyPolicyViewModel(PrivacyPolicyActivity activity) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        Bundle extras = activity.getIntent().getExtras();
        isFromPrivacy = extras.getString(AppConstant.PRIVACY_POLICY_KEY);

        if (extras.containsKey(AppConstant.PRIVACY_POLICY_KEY)) {
            loadUrl(isFromPrivacy);
        }
    }

    private void loadUrl(String dsa) {
        activity.getBinding().toolbar.tvTitle.setText("Privacy Policy");
        activity.getBinding().wvPrivacy.loadUrl(dsa);

    }


    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mBackLayout:
                finish();
                break;
            default:
                break;
        }
    }

    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText("Privacy Policy");
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }

    }
}
