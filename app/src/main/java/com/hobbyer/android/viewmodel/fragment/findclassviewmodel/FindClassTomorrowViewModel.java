/*
package com.hobbyer.android.viewmodel.fragment.findclassviewmodel;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.FindClassTomorrowRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.findclasstomorrow.FindClassTomorrowContentList;
import com.hobbyer.android.api.response.auth.findclasstomorrow.FindClassTomorrowResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.FragmentFindClassTomorrowBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.view.adapter.AdapterFindClassTomorrow;
import com.hobbyer.android.view.fragments.base.FragmentViewModel;
import com.hobbyer.android.view.fragments.findaclass.FragmentFindClassTomorrow;

import java.util.ArrayList;

import retrofit2.Call;

public class FindClassTomorrowViewModel extends FragmentViewModel<FragmentFindClassTomorrow,FragmentFindClassTomorrowBinding> {


    private FragmentFindClassTomorrow fragment;
    private Activity activity;
    private RecyclerView rvTomorrow;
    private AdapterFindClassTomorrow FindClassTomorrowAdapter;
    public FindClassTomorrowViewModel(FragmentFindClassTomorrow fragment, FragmentFindClassTomorrowBinding binding) {
        super(fragment);
        this.fragment=fragment;
        this.activity=getActivity();
        rvTomorrow = (RecyclerView)activity.findViewById(R.id.rvTomorrow);
    */
/*    Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date today = calendar.getTime();*//*

        findClassConnection();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        rvTomorrow.setLayoutManager(mLayoutManager);
        rvTomorrow.setLayoutManager(new GridLayoutManager(activity, numberOfColumns));
    }

    @Override
    protected void initialize(FragmentFindClassTomorrowBinding binding) {

    }




    private void findClassConnection() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                UserModel userModel = PreferenceUtils.getUserModel();
                if(userModel == null) {
                    return;
                }
                FindClassTomorrowRequest homeRequest = new FindClassTomorrowRequest();
                homeRequest.setUserId(""+userModel.getId());
                homeRequest.setPageNumber(1);
                homeRequest.setPageSize(10);
                homeRequest.setDay("Wednesday");
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
                webServices.find_Class_Tomorrow(homeRequest).enqueue(new BaseCallback<FindClassTomorrowResponse>(fragment) {
                    @Override
                    public void onSuccess(FindClassTomorrowResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ArrayList<FindClassTomorrowContentList> studiosList = response.getResult().getContentList();
                                FindClassTomorrowAdapter = new AdapterFindClassTomorrow(activity, studiosList);
                                rvTomorrow.setAdapter(FindClassTomorrowAdapter);

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<FindClassTomorrowResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
*/
