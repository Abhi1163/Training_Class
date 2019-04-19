package com.hobbyer.android.view.fragments.findaclass;


import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CategoryId;
import com.hobbyer.android.api.request.FindClassRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.findclass.FindClassContentList;
import com.hobbyer.android.api.response.auth.findclass.FindClassResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.FragmentFindClassTodayBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.view.adapter.AdapterFindClassToday;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;

import static com.hobbyer.android.utils.StringUtils.currentDays;


public class FragmentFindClassToday extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static TextView mtxtLeftMenu;
    public Fragment fragment;
    public ArrayList<String> listData;
    public ArrayList<FindClassContentList> studiosList = new ArrayList<>();
    public String todayAsString;
    String day;
    Date today;
    FragmentFindClassTodayBinding fragmentFindClassTodayDataBinding;
    String classType = "find";
    int portion;
    private int days;
    private Activity mContext;
    private String dateDay;
    private int dayss;
    private RecyclerView rvToday;
    private String todayAsStringg;
    private AdapterFindClassToday findClassTodayAdapter;
    private String title;
    private int page;
    private String mParam1;
    private String mParam2;
    private Serializable list = new ArrayList();
    private int minPoint, maxPoint;
    private String time;
    private int min, sec, duration;
    private String longitude, lattitude;
    private String isFrom,categoryTop;
    private ArrayList<String> categoryList=new ArrayList<>();


    private String dep_min_time = "", dep_max_time = "", dep_min_point = "", dep_max_point = "", country = "", maxTime = "";


    public FragmentFindClassToday() {
        // Required empty public constructor
    }

    public static FragmentFindClassToday newInstance(String param1, String param2) {
        FragmentFindClassToday fragment = new FragmentFindClassToday();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void SetHeaderTitle(String mTitle) {
        //mtxtLeftMenu.setText(mTitle);
    }

    public void setAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        findClassTodayAdapter = new AdapterFindClassToday(mContext, studiosList, todayAsString);
        //findClassTodayAdapter = new AdapterFindClassToday(mContext, studiosList, todayAsString);
        fragmentFindClassTodayDataBinding.rvTodays.setAdapter(findClassTodayAdapter);
        fragmentFindClassTodayDataBinding.rvTodays.setLayoutManager(mLayoutManager);
        fragmentFindClassTodayDataBinding.rvTodays.setLayoutManager(new GridLayoutManager(mContext, numberOfColumns));
    }

    // newInstance constructor for creating fragment with arguments
  /*  public FragmentFindClassToday newInstance(int page, String title) {
        fragment = new FragmentFindClassToday();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {

            dep_min_time = data.getStringExtra("depMinTime");
            dep_max_time = data.getStringExtra("depMaxTime");
            maxPoint = (int) data.getExtras().get("depMaxPoint");
            minPoint = (int) data.getExtras().get("depMinPoint");
            lattitude = String.valueOf((double) data.getExtras().get("latitude"));
            longitude = String.valueOf((double) data.getExtras().get("longitude"));
            dep_min_point = data.getStringExtra("depMinPoint");
            listData = (ArrayList<String>) data.getExtras().get("list");
            findClassConnection();
        }


    }

    // Store instance variables based on arguments passed
    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt("someInt", 0);
            title = getArguments().getString("someTitle");
        }
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentFindClassTodayDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_class_today, container, false);
        mContext = getActivity();
        getIntentData();

        //  getTime();
        return fragmentFindClassTodayDataBinding.getRoot();
    }

    private void getTime() {
        time = dep_max_time;
        String[] units = time.split(":"); //will break the string up into an array
        min = Integer.parseInt(units[0]); //first element
        sec = Integer.parseInt(units[1]); //second element
        duration = 60 * min + duration;
        maxTime = String.valueOf(duration);
    }


    private void getIntentData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            portion = bundle.getInt("pos");
            todayAsString = todayAsStringg = bundle.getString("date");
            dateDay = bundle.getString("day");
            categoryList = (ArrayList<String>) bundle.getSerializable("categoryList");
            isFrom =  bundle.getString("isFrom");

         //    isFrom=bundle.getString("isFrom");
           //  categoryTop=bundle.getString("categoryList");

            SimpleDateFormat yourDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            switch (dateDay) {
                case "monday":
                    dayss = 1;
                    break;
                case "tuesday":
                    dayss = 2;
                    break;
                case "wednesday":
                    dayss = 3;
                    break;
                case "thursday":
                    dayss = 4;
                    break;
                case "friday":
                    dayss = 5;
                case "saturday":
                    dayss = 6;
                    break;
                case "sunday":
                    dayss = 7;
                    break;
            }


        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {

        fragmentFindClassTodayDataBinding.swipeFindClass.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                findClassConnection();
            }
        });
        fragmentFindClassTodayDataBinding.swipeFindClass.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorPrimaryDark);
        //getBindleData();

     /*   if (isFrom.equalsIgnoreCase("top"))
        {
            findClassConnectionTop();

        }
        else {*/
            findClassConnection();

        setAdapter();

    }

    private void findClassConnectionTop() {



        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                UserModel userModel = PreferenceUtils.getUserModel();
                long userId = userModel.getId();
                int inUserId = (int) userId;
                String maxTime = "";
                String minTime = "";
                days = Integer.parseInt(currentDays());
                FindClassRequest homeRequest = new FindClassRequest();
                homeRequest.setUserId(inUserId);
                homeRequest.setPageNumber(1);
                homeRequest.setPageSize(10);
                if ((maxTime != null && !maxTime.equals("")) && (minTime != null && !minTime.equals(""))) {
                    homeRequest.setEndTime(maxTime);
                    homeRequest.setStartTime(minTime);
                }
                if (minPoint != 0 && maxPoint <= 100) {
                    homeRequest.setStartPoint(0);
                    homeRequest.setEndPoint(0);
                }
                if (lattitude != null && longitude != null) {
                    homeRequest.setLatitude("");
                    homeRequest.setLongitude("");
                }
                if (categoryList != null) {
                    List<CategoryId> dataList = new ArrayList<>();
                    for (int i = 0; i < categoryList.size(); i++) {
                        CategoryId model = new CategoryId();
                        model.set1(categoryList.get(i));
                        dataList.add(model);
                    }
                    homeRequest.setCategoryId(dataList);
                }
                homeRequest.setDay(dayss);
              /*  homeRequest.setStartTime(dep_min_time);
                homeRequest.setEndTime(dep_max_time);*/

                homeRequest.setDate(todayAsString);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.find_Class(homeRequest).enqueue(new BaseCallback<FindClassResponse>((AppCompatActivity) mContext) {
                    @Override
                    public void onSuccess(FindClassResponse response) {
                        fragmentFindClassTodayDataBinding.swipeFindClass.setRefreshing(false);
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                if (response.getResult().getContentList().size()>0) {
                                    fragmentFindClassTodayDataBinding.llRecycler.setVisibility(View.VISIBLE);
                                    fragmentFindClassTodayDataBinding.llNoData.setVisibility(View.GONE);
                                    studiosList.clear();
                                    studiosList.addAll(response.getResult().getContentList());
                                    findClassTodayAdapter.notifyDataSetChanged();

                                } else {

                                    fragmentFindClassTodayDataBinding.llRecycler.setVisibility(View.GONE);
                                    fragmentFindClassTodayDataBinding.llNoData.setVisibility(View.VISIBLE);
                                    studiosList.clear();
                                    findClassTodayAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {

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



    private void findClassConnection() {

        if (CommonUtils.isOnline(mContext)) {
            try {


                ProgressDialogUtils.show(mContext);
                UserModel userModel = PreferenceUtils.getUserModel();
                long userId = userModel.getId();
                int inUserId = (int) userId;
                String maxTime = dep_max_time;
                String minTime = dep_min_time;
                days = Integer.parseInt(currentDays());
                FindClassRequest homeRequest = new FindClassRequest();
                homeRequest.setUserId(inUserId);
                homeRequest.setPageNumber(1);
                homeRequest.setPageSize(10);
                if ((maxTime != null && !maxTime.equals("")) && (minTime != null && !minTime.equals(""))) {
                    homeRequest.setEndTime(maxTime);
                    homeRequest.setStartTime(minTime);
                }
                if (minPoint != 0 && maxPoint <= 100) {
                    homeRequest.setStartPoint(minPoint);
                    homeRequest.setEndPoint(maxPoint);
                }
                if (lattitude != null && longitude != null) {
                    homeRequest.setLatitude(lattitude);
                    homeRequest.setLongitude(longitude);
                }
                if (isFrom != null) {
                    if (isFrom.equalsIgnoreCase("top")) {
                        if (categoryList != null) {
                            List<CategoryId> data = new ArrayList<>();
                            for (int i = 0; i < categoryList.size(); i++) {
                                CategoryId model = new CategoryId();
                                model.set1(categoryList.get(i));
                                data.add(model);
                            }
                            homeRequest.setCategoryId(data);
                        }
                    }
                } else if (listData != null) {
                    List<CategoryId> data = new ArrayList<>();
                    for (int i = 0; i < listData.size(); i++) {
                        CategoryId model = new CategoryId();
                        model.set1(listData.get(i));
                        data.add(model);
                    }
                    homeRequest.setCategoryId(data);
                }
                homeRequest.setDay(dayss);
              /*  homeRequest.setStartTime(dep_min_time);
                homeRequest.setEndTime(dep_max_time);*/

                homeRequest.setDate(todayAsString);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.find_Class(homeRequest).enqueue(new BaseCallback<FindClassResponse>((AppCompatActivity) mContext) {
                    @Override
                    public void onSuccess(FindClassResponse response) {
                        fragmentFindClassTodayDataBinding.swipeFindClass.setRefreshing(false);
                        if (response != null) {
                            if (response.getStatus() == 1) {

                                if (response.getResult().getContentList().size() > 0) {
                                    fragmentFindClassTodayDataBinding.llRecycler.setVisibility(View.VISIBLE);
                                    fragmentFindClassTodayDataBinding.llNoData.setVisibility(View.GONE);
                                    studiosList.clear();
                                    studiosList.addAll(response.getResult().getContentList());
                                    findClassTodayAdapter.notifyDataSetChanged();

                                } else {

                                    fragmentFindClassTodayDataBinding.llRecycler.setVisibility(View.GONE);
                                    fragmentFindClassTodayDataBinding.llNoData.setVisibility(View.VISIBLE);
                                    studiosList.clear();
                                    findClassTodayAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {

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


