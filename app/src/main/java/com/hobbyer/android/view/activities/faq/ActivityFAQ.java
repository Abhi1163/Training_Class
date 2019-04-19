package com.hobbyer.android.view.activities.faq;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.faq.FaqContentList;
import com.hobbyer.android.api.response.auth.faq.FaqResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.ActivityFaqBinding;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;

import com.hobbyer.android.view.adapter.FaqExpandableListAdapter;

import java.util.ArrayList;


import retrofit2.Call;

public class ActivityFAQ extends AppCompatActivity implements View.OnClickListener {
    private Context mContext;
    FaqExpandableListAdapter faqListAdapter;
    private ActivityFaqBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_faq);
        mContext = ActivityFAQ.this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(ActivityFAQ.this, mContext.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        citySearchConnection();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        binding.reFaqList.setLayoutManager(mLayoutManager);
        binding.reFaqList.setLayoutManager(new GridLayoutManager(mContext, numberOfColumns));


    }

    private void setToolbar() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.ivBack.setOnClickListener(this);
        binding.toolbar.tvTitle.setText(R.string.faqtool);
    }


    private void citySearchConnection() {

        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.Faq().enqueue(new BaseCallback<FaqResponse>(ActivityFAQ.this) {
                    @Override
                    public void onSuccess(FaqResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ArrayList<FaqContentList> contentList = response.getResult().getContentList();
                                faqListAdapter = new FaqExpandableListAdapter(ActivityFAQ.this, mContext, contentList);
                                binding.reFaqList.setAdapter(faqListAdapter);

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<FaqResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mContext, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.ivBack:
                finish();
                break;
        }

    }
}
