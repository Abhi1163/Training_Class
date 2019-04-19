package com.hobbyer.android.viewmodel.activity.filterviewmodel;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.response.auth.countrylist.CountryContentList;
import com.hobbyer.android.api.response.auth.countrylist.CountryListResponse;
import com.hobbyer.android.api.response.auth.filter.CategoryListContentList;
import com.hobbyer.android.api.response.auth.filter.CategoryListResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.interfaces.OnClickInterFace;
import com.hobbyer.android.model.FilterModel;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.view.activities.filter.FilterActivity;
import com.hobbyer.android.view.activities.filter.LocationActivity;
import com.hobbyer.android.view.adapter.AdapterFilter;
import com.hobbyer.android.viewmodel.activity.baseviewmodel.ActivityViewModel;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;

import static android.app.Activity.RESULT_OK;

public class FilterViewModel extends ActivityViewModel<FilterActivity> implements View.OnClickListener {
    private static int MIN_POINT = 10;
    private static int MAX_POINT = 500;
    private static int MINUTES_IN_AN_HOUR = 1;
    private static int SECONDS_IN_A_MINUTE = 24;
    private static int STEP_POINT = 1;
    private ArrayList<CountryContentList> contentList;
    private int pointCount;
    private FilterActivity activity;
    private ArrayList<String> list = new ArrayList();
    private ArrayList<CategoryListContentList> studiosList = new ArrayList<>();
    private int startPoint, endPoint;
    private int minPoint, maxPoint, minPointShared, maxPointShared;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FilterModel filterModel;
    private Serializable listData = new ArrayList();

    private double longitude, latitude;
    private String depMinShared = "", depMaxShared = "", addressPref = "", depMinPointShared = "", depMaxPointShared = "", startTimeShared = "", endTimeShared = "";
    private String dep_min_time = "", dep_max_time = "", dep_min_point = "", dep_max_point = "", country = "", depMinSaved = "", depMaxSaved = "";
    private AdapterFilter adapterFilter;
    private int minPointValue = 0, maxPointValue = 100;
    private String address;

    public FilterViewModel(FilterActivity activity) {
        super(activity);
        this.activity = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CommonUtils.updateStatusBar(activity, activity.getResources().getColor(R.color.colorPrimary));
        }
        setToolbar();


        countryConnection();
        activity.getBinding().etLocation.setText(address);

        /*getEditProfileConnection();*/
        activity.getBinding().btnDone.setOnClickListener(this);
        activity.getBinding().toolbar.tvRight.setOnClickListener(this);
        activity.getBinding().etLocation.setOnClickListener(this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 4;
        activity.getBinding().rvActivityList.setLayoutManager(mLayoutManager);
        activity.getBinding().rvActivityList.setLayoutManager(new GridLayoutManager(activity, numberOfColumns));
        filterConnection();
        initUI();
        getIntent();
        // setPrefData();

    }


    private void getIntent() {
        if (getActivity().getIntent() != null) {

            maxPointValue = getActivity().getIntent().getIntExtra("depMaxPoint", 1);
            minPointValue = getActivity().getIntent().getIntExtra("depMinPoint", 100);
            //  getActivity().getIntent().getStringExtra("list");

            depMinSaved = getActivity().getIntent().getStringExtra("depMinTimeShared");
            depMaxSaved = getActivity().getIntent().getStringExtra("depMaxTimeShared");


            getActivity().getIntent().getStringExtra("longitude");
            getActivity().getIntent().getStringExtra("longitude");
            addressPref = getActivity().getIntent().getStringExtra("address");

            listData = getActivity().getIntent().getStringExtra("list");

            // getActivity().getIntent().getStringExtra("address");

            activity.getBinding().etLocation.setText(addressPref);


            // getActivity().getBinding().etLocation.setText(addressPref);
            getActivity().getBinding().rangeSeekbar2.setMinStartValue(minPointValue != 0 ? minPointValue : 0).setMaxStartValue(maxPointValue != 0 ? maxPointValue : 100).apply();


        }


        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            String title = bundle.getString("Address");
            longitude = bundle.getDouble("longitude");
            latitude = bundle.getDouble("latitude");
            //  getActivity().getBinding().etLocation.setText(title);
        }
    }


    public void getTime() {
        int minHour = activity.getBinding().rangeSeekbar1.getSelectedMinValue().intValue() + 720;
        int maxHour = activity.getBinding().rangeSeekbar1.getSelectedMaxValue().intValue() + 720;

        int hour = minHour / 60;
        String minute = String.valueOf(minHour - (hour * 60));
        String hours = String.valueOf(hour);

        int hour1 = (maxHour / 60) - 24;
        String minute1 = String.valueOf(((maxHour - (hour1 * 60)) / 24) - 60);
        String hours1 = String.valueOf(hour1);

        if (minute.equals("60") || minute.equals("0")) {
            minute = "00";
        } else if (minute1.equals("60") || minute.equals("0")) {
            minute1 = "00";
        }
        if (minute.length() < 2) {
            minute = "0" + minute;
        } else if (minute1.length() < 2) {
            minute1 = "0" + minute1;
        }
        if (hours.length() < 2) {
            hours = "0" + hours;
        } else if (hours1.length() < 2) {
            hours1 = "0" + hours1;
        }


        depMinSaved = (String.valueOf(hours) + ":" + String.valueOf(minute));
        depMaxSaved = (String.valueOf(hours1) + ":" + String.valueOf(minute1));
        activity.getBinding().tvStartTime.setText(StringUtils.timeFormats("" + depMinSaved + "  To "));
        activity.getBinding().tvEndTime.setText(StringUtils.timeFormats("" + depMaxSaved));
    }

    public void onClickView(View view) {
        switch (view.getId()) {

            case R.id.btnDone:

                Intent intent = new Intent();
                intent.putExtra("depMinTime", dep_min_time + ":00");
                intent.putExtra("depMaxTime", dep_max_time + ":00");
                intent.putExtra("depMinTimeShared", dep_min_time);
                intent.putExtra("depMaxTimeShared", dep_max_time);
                intent.putExtra("depMaxPoint", maxPoint);

                intent.putExtra("depMinPoint", minPoint);
                intent.putExtra("list", list);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);

                intent.putExtra("address", address);
                activity.setResult(RESULT_OK, intent);


                finish();
                break;


            case R.id.etLocation:
                Intent intentt = new Intent(activity, LocationActivity.class);
                activity.startActivityForResult(intentt, 120);


                break;

        }
    }


    private void setToolbar() {
        activity.getBinding().toolbar.ivBack.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvRight.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.tvTitle.setVisibility(View.VISIBLE);
        activity.getBinding().toolbar.ivBack.setImageResource(R.mipmap.ic_back);
        activity.getBinding().toolbar.ivBack.setOnClickListener(this);
        activity.getBinding().toolbar.tvRight.setOnClickListener(this);
        activity.getBinding().toolbar.tvTitle.setText(R.string.refine_search);
        activity.getBinding().toolbar.tvRight.setText(R.string.clear_all);

    }

    private void initUI() {
        activity.getBinding().rangeSeekbar1.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {


                activity.getBinding().rangeSeekbar1.setMinValue(0);
                activity.getBinding().rangeSeekbar1.setMaxValue(1440);

                int minHour = activity.getBinding().rangeSeekbar1.getSelectedMinValue().intValue() + 720;
                int maxHour = activity.getBinding().rangeSeekbar1.getSelectedMaxValue().intValue() + 720;


                int hour = minHour / 60;
                String minute = String.valueOf(minHour - (hour * 60));
                String hours = String.valueOf(hour);

                int hour1 = (maxHour / 60) - 24;
                String minute1 = String.valueOf(((maxHour - (hour1 * 60)) / 24) - 60);
                String hours1 = String.valueOf(hour1);

                if (minute.equals("60") || minute.equals("0")) {
                    minute = "00";
                } else if (minute1.equals("60") || minute.equals("0")) {
                    minute1 = "00";
                }
                if (minute.length() < 2) {
                    minute = "0" + minute;
                } else if (minute1.length() < 2) {
                    minute1 = "0" + minute1;
                }
                if (hours.length() < 2) {
                    hours = "0" + hours;
                } else if (hours1.length() < 2) {
                    hours1 = "0" + hours1;
                }


                dep_min_time = (String.valueOf(hours) + ":" + String.valueOf(minute));
                dep_max_time = (String.valueOf(hours1) + ":" + String.valueOf(minute1));
                activity.getBinding().tvStartTime.setText(StringUtils.timeFormats("" + dep_min_time + "  To "));
                activity.getBinding().tvEndTime.setText(StringUtils.timeFormats("" + dep_max_time));
            }
        });

        pointCount = (MAX_POINT - MIN_POINT) / STEP_POINT;
        activity.getBinding().rangeSeekbar2.setTag(pointCount);
        activity.getBinding().rangeSeekbar2.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minPoint = activity.getBinding().rangeSeekbar2.getSelectedMinValue().intValue();
                maxPoint = activity.getBinding().rangeSeekbar2.getSelectedMaxValue().intValue();
                if (minPoint > maxPoint) {
                    int temp = minPoint;
                    minPoint = maxPoint;
                    maxPoint = temp;
                }
                if (minPoint < 0) {
                    minPoint = 0;
                    activity.getBinding().rangeSeekbar2.setMaxStartValue(maxPoint);
                    activity.getBinding().rangeSeekbar2.setMinStartValue(minPoint);
                }
                dep_min_point = (String.valueOf(minPoint));
                dep_max_point = (String.valueOf(maxPoint));
                activity.getBinding().tvStartPoint.setText(dep_min_point + "  To ");
                activity.getBinding().tvEndPoint.setText(dep_max_point + " Points");
            }
        });

    }

    public String setValueAfterDecimalPoint(String price) {

        if (price != null && !price.equalsIgnoreCase("")) {
            try {
                double value2 = Double.parseDouble(price);
                double value = Double.parseDouble(new DecimalFormat("#.##").format(value2));
                return String.format("%.2f", value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return "00";
            }


        } else {
            return "00";
        }
    }


    private void filterConnection() {

        if (CommonUtils.isOnline(activity)) {
            try {
                ProgressDialogUtils.show(activity);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.category_List().enqueue(new BaseCallback<CategoryListResponse>((AppCompatActivity) activity) {
                    @Override
                    public void onSuccess(CategoryListResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                studiosList = response.getResult().getContentList();
                                adapterFilter = new AdapterFilter(activity, studiosList, new OnClickInterFace() {
                                    @Override
                                    public void onItemClick(View view, int position, CategoryListContentList listContentList) {
                                        list = new ArrayList<>();
                                        for (int i = 0; i < listContentList.getId().length(); i++) {
                                            list.add(listContentList.getId());
                                        }
                                    }
                                });
                                activity.getBinding().rvActivityList.setAdapter(adapterFilter);
                            }
                        }
                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<CategoryListResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                                //   activity.getBinding().spCountry.setAdapter(new CountrySpinnerAdapter(activity, contentList));

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                finish();
                break;

            case R.id.tvRight:
                for (int i = 0; i < studiosList.size(); i++) {

                    studiosList.get(i).setSelected(false);
                }
                adapterFilter.notifyDataSetChanged();
                activity.getBinding().rangeSeekbar2.setMinValue(0);
                activity.getBinding().rangeSeekbar2.setMaxValue(100);
                activity.getBinding().rangeSeekbar1.setMinStartValue(0).setMaxStartValue(1440).apply();
                activity.getBinding().rangeSeekbar2.setMinStartValue(0).setMaxStartValue(100).apply();
                activity.getBinding().etLocation.setText("");

                break;


            case R.id.spCountry:
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            address = data.getStringExtra("address");
            latitude = (double) data.getExtras().get("latitude");
            longitude = (double) data.getExtras().get("longitude");
            activity.getBinding().etLocation.setText(address);


        }
    }
}

