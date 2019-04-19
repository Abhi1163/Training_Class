package com.hobbyer.android.view.fragments.profile;


import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.PastUserViewRequest;
import com.hobbyer.android.api.request.RatingRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.pastprofile.PastUserViewContentList;
import com.hobbyer.android.api.response.auth.pastprofile.PastUserViewResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.DialogFeedbackRatingBinding;
import com.hobbyer.android.interfaces.ProfileInterface;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.DialogUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.adapter.AdapterPast;

import java.util.List;

import retrofit2.Call;

public class PastsFragment extends Fragment {
    private Context context;
    private android.support.v4.app.Fragment fragment;
    private RecyclerView mPastRecylerView;
    private ProfileInterface onItemClick;
    private Dialog dialog;
    private String rating, comment="";

    private TextView tvNodata;
    private List<PastUserViewContentList> contentLists;
    private int studioId, ratingNum;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_past, container, false);

        fragment = new android.support.v4.app.Fragment();
        context = getActivity();
        tvNodata = view.findViewById(R.id.tvNoData);

        mPastRecylerView = view.findViewById(R.id.mPastRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mPastRecylerView.setLayoutManager(linearLayoutManager);
        pastPageConnection();
        return view;
    }

    public void getStudio() {
        for (int i = 0; i < contentLists.size(); i++) {
            studioId = contentLists.get(i).getStudioId();


        }

    }

    private void pastPageConnection() {
        if (CommonUtils.isOnline(context)) {
            ProgressDialogUtils.show(context);
            UserModel userModel = PreferenceUtils.getUserModel();
            if (userModel != null) {
            }
            PastUserViewRequest pastUserViewRequest = new PastUserViewRequest();
            pastUserViewRequest.setUserid("" + userModel.getId());
            pastUserViewRequest.setPage_number(1);
            pastUserViewRequest.setPage_size(4);
            AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
            authWebServices.pastUserView(pastUserViewRequest).enqueue(new BaseCallback<PastUserViewResponse>(fragment) {
                @Override
                public void onSuccess(PastUserViewResponse response) {

                    if (response != null) {
                        if (response.getResult().getContentList().size() > 0) {
                            mPastRecylerView.setVisibility(View.VISIBLE);
                            tvNodata.setVisibility(View.GONE);

                            contentLists = response.getResult().getContentList();

                            AdapterPast adapterPast = new AdapterPast(context, contentLists, new ProfileInterface() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    getStudio();
                                    switch (view.getId()) {
                                        case R.id.btFeedback:
                                            feedbackDialog();
                                            break;


                                    }


                                }
                            });
                            mPastRecylerView.setAdapter(adapterPast);

                        } else {
                            mPastRecylerView.setVisibility(View.GONE);
                            tvNodata.setVisibility(View.VISIBLE);
                        }
                    }
                    ProgressDialogUtils.dismiss();

                }

                @Override
                public void onFail(Call<PastUserViewResponse> call, BaseResponse baseResponse) {
                    ProgressDialogUtils.dismiss();

                    ToastUtils.showToastShort(context, baseResponse.getMessage());
                }
            });
        } else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    private void feedbackDialog() {
        DialogFeedbackRatingBinding dialogFeedbackRatingBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_feedback_rating, null, false);
        dialog = DialogUtils.createDialog(context, dialogFeedbackRatingBinding.getRoot());


        dialogFeedbackRatingBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ratingNum = (int) dialogFeedbackRatingBinding.rating.getRating();
                comment = dialogFeedbackRatingBinding.etComment.getText().toString();
                if (ratingNum != 0) {

                    ratingApi();
                } else {
                    ToastUtils.showToastShort(context, "Please give rating");
                }


            }
        });
        dialogFeedbackRatingBinding.ivCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void ratingApi() {
        if (CommonUtils.isOnline(context)) {
            try {


                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel != null) {

                }

                RatingRequest ratingRequest = new RatingRequest();
                ratingRequest.setUserid("" + userModel.getId());
                ratingRequest.setStudioId(studioId);
                ratingRequest.setRating(ratingNum);
                ratingRequest.setFeedback("" + comment);
                AuthWebServices authWebServices = RequestController.createService(AuthWebServices.class, false);
                authWebServices.submitRating(ratingRequest).enqueue(new BaseCallback<BaseResponse>(fragment) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        dialog.dismiss();

                        ToastUtils.showToastShort(context, response.getMessage());
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
            ToastUtils.showToastShort(context, String.valueOf(R.string.msg_network_error));
        }
    }

}