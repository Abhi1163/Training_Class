package com.hobbyer.android.viewmodel.activity.termandconditionviewmodel;

import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.constant.AppConstant;

import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.view.activities.termandcondition.TermsAndConditionsActivity;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;


public class TermsAndConditionsViewModel extends ActivityViewModel<TermsAndConditionsActivity> implements View.OnClickListener {
    private TermsAndConditionsActivity activity;
    private String isFrom="";
    private TextView name;
    private ImageView imBack;
    public TermsAndConditionsViewModel(TermsAndConditionsActivity activity) {
        super(activity);
        this.activity = activity;
        //setHeader();
        name=(TextView)activity.findViewById(R.id.name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        Bundle extras = activity.getIntent().getExtras();
        if (extras!= null && extras.containsKey(AppConstant.URL_KEY)) {
            isFrom = extras.getString(AppConstant.URL_KEY);
            loadUrl(isFrom);
        }
        if (extras!= null && extras.containsKey(AppConstant.TITLE_NAME)) {
            isFrom = extras.getString(AppConstant.TITLE_NAME);
            activity.getBinding().toolbar.tvTitle.setText(isFrom);
        }
     /*   imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/


    }

    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setText("Terms & Conditions");
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
    }

    private void loadUrl(String dsa) {
        activity.getBinding().myWebView.loadUrl(dsa);
    }

    private void setHeader() {

       /* activity.getBinding().name.setText("Terms & Conditions");
        activity.getBinding().mBackImageview.setVisibility(View.VISIBLE);*/
    }



    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.mBackImageview:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            default:
                break;
        }}
}
