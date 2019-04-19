package com.hobbyer.android.view.activities.recentcharges;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.RecentChargesRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.recentcharges.RecentChargeList;
import com.hobbyer.android.api.response.auth.recentcharges.RecentChargeResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.ActivityRecentChargesBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.adapter.AdapterRecentCharges;

import java.util.List;

import retrofit2.Call;

public class RecentCharges extends AppCompatActivity implements View.OnClickListener {

    private ActivityRecentChargesBinding binding;
    private Context context;
    private List<RecentChargeList> recentChargeLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recent_charges);
        context = RecentCharges.this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(RecentCharges.this, getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();
        setRecycler();
        getRecentchrgesApi();
        binding.btnPrint.setOnClickListener(this);

    }

    private void setRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        binding.rvRecent.setLayoutManager(layoutManager);


    }

    private void getRecentchrgesApi() {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                RecentChargesRequest recentChargesRequest = new RecentChargesRequest();
                recentChargesRequest.setUserId("" + userModel.getId());
                recentChargesRequest.setPage_number(1);
                recentChargesRequest.setPage_size(10);
                AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
                authWebServices.recentCharges(recentChargesRequest).enqueue(new BaseCallback<RecentChargeResponse>((AppCompatActivity) context) {
                    @Override
                    public void onSuccess(RecentChargeResponse response) {
                        if (response != null) {
                            recentChargeLists = response.getResult().getContentList();
                            AdapterRecentCharges adapterRecentCharges = new AdapterRecentCharges(context, recentChargeLists);
                            binding.rvRecent.setAdapter(adapterRecentCharges);


                        }

                    }


                    @Override
                    public void onFail(Call<RecentChargeResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                        ToastUtils.showToastShort(context, baseResponse.getMessage());

                    }

                });

                ProgressDialogUtils.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            ProgressDialogUtils.dismiss();

            ToastUtils.showToastShort(context, String.valueOf(R.string.msg_network_error));
        }


    }

    private void setToolbar() {
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText("Recent Charges");
        binding.toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        binding.toolbar.ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;

            case R.id.btn_print:
                finish();
                break;
        }

    }


}
