package com.hobbyer.android.view.fragments.findaclass;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hobbyer.android.BR;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.FindClassRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.findclass.FindClassContentList;
import com.hobbyer.android.api.response.auth.findclass.FindClassResponse;
import com.hobbyer.android.api.response.auth.homepage.HomePageContentList;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.FragmentFindClassTodayBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.view.adapter.AdapterFindClassToday;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

public class FragmentFindClassTomorrow extends Fragment {

    private Activity mContext;
    private RecyclerView rvToday;
    String day;
    Date today;
    String todayAsString;
    FragmentFindClassTodayBinding fragmentFindClassTodayDataBinding;

    private AdapterFindClassToday findClassTodayAdapter;

    public FragmentFindClassTomorrow() {
        // Required empty public constructor
    }

    String classType = "find";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentFindClassTodayDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_class_today, container, false);
        mContext = getActivity();
        return fragmentFindClassTodayDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date today = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
        todayAsString = dateFormat.format(today);

        //getBindleData();
        findClassConnection();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        fragmentFindClassTodayDataBinding.rvTodays.setLayoutManager(mLayoutManager);
        fragmentFindClassTodayDataBinding.rvTodays.setLayoutManager(new GridLayoutManager(mContext, numberOfColumns));
    }

    private void findClassConnection() {

        if (CommonUtils.isOnline(mContext)) {
            try {
                if (classType.equalsIgnoreCase("home")) {
                    HomePageContentList homeModel = PreferenceUtils.getHome();
                    if (homeModel != null) {
                        day = homeModel.getDay();
                    }
                } else if (classType.equalsIgnoreCase("filter")) {

                } else if (classType.equalsIgnoreCase("find")) {
                    day = StringUtils.currentDays();

                }
                ProgressDialogUtils.show(mContext);
                UserModel userModel = PreferenceUtils.getUserModel();
                long userId = userModel.getId();
                int inUserId = (int) userId;
                if (userModel == null) {
                    return;
                }

                FindClassRequest homeRequest = new FindClassRequest();
                homeRequest.setUserId(inUserId);
                homeRequest.setPageNumber(1);
                homeRequest.setPageSize(10);
                homeRequest.setDay(Integer.valueOf(day));
                homeRequest.setDate(todayAsString);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class,false);
                webServices.find_Class(homeRequest).enqueue(new BaseCallback<FindClassResponse>((AppCompatActivity) mContext) {
                    @Override
                    public void onSuccess(FindClassResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                ArrayList<FindClassContentList> studiosList = response.getResult().getContentList();
                                /*findClassTodayAdapter = new AdapterFindClassToday(mContext, studiosList);
                                fragmentFindClassTodayDataBinding.rvTodays.setAdapter(findClassTodayAdapter);*/

                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<FindClassResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
