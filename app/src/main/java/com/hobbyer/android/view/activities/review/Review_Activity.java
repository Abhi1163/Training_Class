package com.hobbyer.android.view.activities.review;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailData;
import com.hobbyer.android.api.response.auth.review.ReviewContentList;
import com.hobbyer.android.api.response.auth.review.ReviewResponse;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.request.ReviewRequest;
import com.hobbyer.android.api.response.auth.videos.VideosContentList;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.ActivityReviewBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.view.adapter.AdapterReview;
import com.hobbyer.android.view.adapter.AdapterTopLiveUser;

import java.util.ArrayList;

import retrofit2.Call;


public class Review_Activity extends AppCompatActivity implements View.OnClickListener{

    ActivityReviewBinding binding;
    private AdapterReview adapterReview;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review);
        mContext=Review_Activity.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(Review_Activity.this, mContext.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        initViews();
    }

    private void initViews() {
        reviewConnection();
        LinearLayoutManager rvTopLiveLayoutManager = new LinearLayoutManager(binding.rvReview.getContext(), LinearLayoutManager.VERTICAL,false);
        binding.rvReview.setLayoutManager(rvTopLiveLayoutManager);

    }

    private void setToolbar() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText(R.string.review_screen);
        binding.toolbar.ivBack.setOnClickListener(this);
    }
    private void reviewConnection() {

        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                ClassDetailData classDetail = PreferenceUtils.getClassModel();
                if (classDetail == null) {
                    return;
                }
                ReviewRequest reviewRequest = new ReviewRequest();
                reviewRequest.setStudio_id(classDetail.getStudioId());
                reviewRequest.setPage_number(1);
                reviewRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.studioReview(reviewRequest).enqueue(new BaseCallback<ReviewResponse>((AppCompatActivity)Review_Activity.this) {
                    @Override
                    public void onSuccess(ReviewResponse response) {

                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ArrayList<ReviewContentList> videosCategoryVideo =response.getResult().getContentList();
                                adapterReview = new AdapterReview(Review_Activity.this,videosCategoryVideo);
                                binding.rvReview.setAdapter(adapterReview);



                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<ReviewResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
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
