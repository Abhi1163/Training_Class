package com.hobbyer.android.view.fragments.findaclass;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.findclass.FindClassContentList;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.databinding.FragmentFindClassBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.view.activities.filter.FilterActivity;
import com.hobbyer.android.view.activities.findclassdetials.GymTimeActivity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

public class FragmentFindAClass extends Fragment implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static int int_items = 15;
    public ArrayList<FindClassContentList> findClassContentList = new ArrayList<>();
    double mmLag, mmLng;
    String[] strArray1 = new String[17];
    String[] strDate = new String[17];
    String[] strDay = new String[17];
    Date[] date = new Date[15];
    private Context mContext;
    private FragmentFindClassBinding binding;
    private int map = 1;
    private int flag = 1;
    private FragmentFindClassToday fragment;
    private boolean isMapVisible = true;
    private MapView mapView;
    private GoogleMap mMap;
    private Bundle mapViewBundle;
    private DrawerLayout drawerLayout;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private ImageView ivBack, ivSearch, ivLocation;
    private TextView tvTitle, tvRight;

    private Serializable listData = new ArrayList();
    private int minPoint, maxPoint;
    private GoogleApiClient googleApiClient;
    private MarkerOptions markerOptions;
    private int finalI;
    private String address;
    private String isFrom;
    private ArrayList<String> categoryList = new ArrayList<>();


    private Serializable list = new ArrayList();
    private String time;
    private int min, sec, duration;
    private String longitude, lattitude, addressPref;


    private String dep_min_time = "", dep_max_time = "", dep_min_point = "", dep_max_point = "", country = "", maxTime = "", depMinTimeShare, depMaxTimeShare;


    private String classScheduleId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_class, container, false);
        mContext = getActivity();

        getIntentData();
        init();
        countPointConnection();
        if (isFrom!=null) {
            if (isFrom.equalsIgnoreCase("top")) {
                setToolbarTop();
            }
        }else {
            setToolbar();
        }
        

       /* googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();*/


        // setupTabIcons();


        binding.tabs.post(new Runnable() {
            @Override
            public void run() {
                setupTabIcons();
            }
        });
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 15; i++) {
            if (i == 0) {
                calendar.add(Calendar.DAY_OF_YEAR, 0);
                date[0] = calendar.getTime();

            } else {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                date[i] = calendar.getTime();
            }
        }
        //   SimpleDateFormat sdf = new SimpleDateFormat("E,dd MMM");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat2 = new SimpleDateFormat("  E, dd MMM ");
        DateFormat dayFormat = new SimpleDateFormat("EEEE");

        for (int i = 0; i < 15; i++) {
            strArray1[i] = dateFormat.format(date[i]);
            strDate[i] = dateFormat2.format(date[i]);
            strDay[i] = dayFormat.format(date[i]).toLowerCase();

        }

        binding.fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filter = new Intent(mContext, FilterActivity.class);
                filter.putExtra("depMinTime", dep_min_time + ":00");
                filter.putExtra("depMaxTime", dep_max_time + ":00");

                filter.putExtra("depMinTimeShared", depMinTimeShare);
                filter.putExtra("depMaxTimeShared", depMaxTimeShare);

                filter.putExtra("depMaxPoint", maxPoint);
                filter.putExtra("depMinPoint", minPoint);
                filter.putExtra("list", list);
                filter.putExtra("longitude", longitude);
                filter.putExtra("latitude", lattitude);
                filter.putExtra("address", addressPref);
                startActivityForResult(filter, 120);
            }
        });
        mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        setHasOptionsMenu(true);
      /*  if (isFrom.equalsIgnoreCase("top")) {
loadFragmentTop();
        }
        else {*/
        loadFragment();


        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (fragment != null) {


            if (resultCode == getActivity().RESULT_OK && data != null) {

                dep_min_time = data.getStringExtra("depMinTime");
                dep_max_time = data.getStringExtra("depMaxTime");
                maxPoint = (int) data.getExtras().get("depMaxPoint");
                minPoint = (int) data.getExtras().get("depMinPoint");
                depMinTimeShare = data.getStringExtra("depMinTimeShared");
                depMaxTimeShare = data.getStringExtra("depMaxTimeShared");
                lattitude = String.valueOf((double) data.getExtras().get("latitude"));
                longitude = String.valueOf((double) data.getExtras().get("longitude"));
                dep_min_point = data.getStringExtra("depMinPoint");
                addressPref = data.getStringExtra("address");
                listData = (ArrayList<String>) data.getExtras().get("list");

            }
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void getIntentData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            categoryList = (ArrayList<String>) bundle.getSerializable("categoryList");
            isFrom = bundle.getString("isFrom");
        }
    }


    private void loadFragmentTop() {

        Bundle bundle = new Bundle();
        fragment = new FragmentFindClassToday();
        bundle.putInt("pos", 0);
        bundle.putSerializable("categoryList", categoryList);
        bundle.putString("date", "" + strArray1[0]);
        bundle.putString("day", "" + strDay[0]);
        bundle.putString("isFrom", "top");

        fragment.setArguments(bundle);
        CommonUtils.setFragment(fragment, true, getActivity(), bundle, R.id.flClass);

    }


    private void setToolbarTop() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setImageResource(R.mipmap.ic_menu);

        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.find_class);

    }


    private void loadFragment() {
        Bundle bundle = new Bundle();
        fragment = new FragmentFindClassToday();
        bundle.putInt("pos", 0);
        bundle.putString("date", "" + strArray1[0]);
        bundle.putString("day", "" + strDay[0]);
        bundle.putSerializable("categoryList", categoryList);
        bundle.putString("isFrom", "top");


        fragment.setArguments(bundle);
        CommonUtils.setFragment(fragment, true, getActivity(), bundle, R.id.flClass);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.findclass, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void init() {
        ivBack = getActivity().findViewById(R.id.ivBack);
        ivLocation = getActivity().findViewById(R.id.ivLocation);
        ivSearch = getActivity().findViewById(R.id.ivSearch);
        tvTitle = getActivity().findViewById(R.id.tvTitle);
        tvRight = getActivity().findViewById(R.id.tvRight);
        drawerLayout = getActivity().findViewById(R.id.drawer_Main_layout);
    }

    private void setToolbar() {
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setImageResource(R.mipmap.ic_menu);
        tvRight.setVisibility(View.VISIBLE);
        ivLocation.setVisibility(View.VISIBLE);
        ivSearch.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.find_class);
        ivSearch.setOnClickListener(this);
        ivLocation.setOnClickListener(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        //  mapView.onSaveInstanceState(mapViewBundle);
    }

    private void loadMap() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


    }

    private void getCurrentLocation() {
        mMap.clear();
        MarkerOptions markerOptions = null;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                for (int i = 0; i < findClassContentList.size(); i++) {
                    if (findClassContentList.get(i).getLatitude() != null && findClassContentList.get(i).getLongitude() != null) {
                        markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(Double.parseDouble(findClassContentList.get(i).getLatitude()),
                                Double.parseDouble(findClassContentList.get(i).getLongitude()))).title("newdestination").snippet("islocated12miles east").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_acting));

                        mmLng = Double.parseDouble(findClassContentList.get(i).getLatitude());
                        mmLag = Double.parseDouble(findClassContentList.get(i).getLatitude());

                        Marker m1 = mMap.addMarker(markerOptions);
                        m1.showInfoWindow();
                        if (i == 0)
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(findClassContentList.get(i).getLatitude()),
                                    Double.parseDouble(findClassContentList.get(i).getLongitude()))));
                    }
                }
            }

        } else {
            if (mMap != null)
                mMap.clear();
        }

        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));


        return;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.setOnMarkerClickListener(this);
        if (mMap != null) {

            mMap.clear();
        }


        if (fragment != null && fragment.studiosList != null) {
            markerOptions = null;
            for (int j = 0; j < fragment.studiosList.size(); j++) {

                if (fragment.studiosList.get(j).getLatitude() != null && fragment.studiosList.get(j).getLongitude() != null) {
                    markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(Double.parseDouble(fragment.studiosList.get(j).getLatitude())
                            , Double.parseDouble(fragment.studiosList.get(j).getLongitude())))
                            .title("")
                            .snippet(String.valueOf(j)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location));
                    classScheduleId = fragment.studiosList.get(j).getClass_schedule_id();

                    Marker m1 = mMap.addMarker(markerOptions);
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            //   ToastUtils.showToastShort(mContext, markerOptions.getSnippet());
                            Intent intent = new Intent(mContext, GymTimeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(AppConstant.BUNDLE_KEY.CLASS_SCHEDULE_ID, classScheduleId);
//                            markerOptions.getSnippet().
                            bundle.putString(AppConstant.BUNDLE_KEY.DATE, fragment.todayAsString);
                            intent.putExtras(bundle);
                            startActivity(intent);

                            return false;
                        }
                    });

                    if (j == 0)
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(fragment.studiosList.get(j).getLatitude()),
                                Double.parseDouble(fragment.studiosList.get(j).getLongitude()))));
                }
            }
        }

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }


    private void setupTabIcons() {
        binding.tabs.addTab(binding.tabs.newTab().setText("Today "));
        binding.tabs.addTab(binding.tabs.newTab().setText("Tomorrow "));

        for (int i = 2; i < 15; i++) {
            binding.tabs.addTab(binding.tabs.newTab().setText(strDate[i]));
        }
        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                fragment = new FragmentFindClassToday();
                Bundle bundle = new Bundle();

                switch (tab.getPosition()) {
                    case 0:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[0]);
                        bundle.putString("day", "" + strDay[0]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 1:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[1]);
                        bundle.putString("day", "" + strDay[1]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 2:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[2]);
                        bundle.putString("day", "" + strDay[2]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 3:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[3]);
                        bundle.putString("day", "" + strDay[3]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();

                        break;
                    case 4:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[4]);
                        bundle.putString("day", "" + strDay[4]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 5:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[5]);
                        bundle.putString("day", "" + strDay[5]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 6:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[6]);
                        bundle.putString("day", "" + strDay[6]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 7:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[7]);
                        bundle.putString("day", "" + strDay[7]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 8:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[8]);
                        bundle.putString("day", "" + strDay[8]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 9:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[9]);
                        bundle.putString("day", "" + strDay[9]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 10:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[10]);
                        bundle.putString("day", "" + strDay[10]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 11:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[11]);
                        bundle.putString("day", "" + strDay[11]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 12:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[12]);
                        bundle.putString("day", "" + strDay[12]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 13:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[13]);
                        bundle.putString("day", "" + strDay[13]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    case 14:
                        bundle.putInt("pos", position);
                        bundle.putString("date", "" + strArray1[14]);
                        bundle.putString("day", "" + strDay[14]);

                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.flClass, fragment).commit();
                        break;
                    default:
                        break;

                }


                if (!isMapVisible) {

                    if (mMap != null) {

                        mMap.clear();

                    }
                    if (fragment != null && fragment.studiosList != null) {
                        MarkerOptions markerOptions = null;
                        for (int j = 0; j < fragment.studiosList.size(); j++) {

                            if (fragment.studiosList.get(j).getLatitude() != null && fragment.studiosList.get(j).getLongitude() != null) {
                                markerOptions = new MarkerOptions();
                                markerOptions.position(new LatLng(Double.parseDouble(fragment.studiosList.get(j).getLatitude())
                                        , Double.parseDouble(fragment.studiosList.get(j).getLongitude())))
                                        .title("newdestination")
                                        .snippet("islocated12miles east").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_location));

                                Marker m1 = mMap.addMarker(markerOptions);

                                if (j == 0)
                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(fragment.studiosList.get(j).getLatitude()),
                                            Double.parseDouble(fragment.studiosList.get(j).getLongitude()))));
                            }
                        }
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSearch:
                if (flag == 1) {
                    binding.searchLayout.setVisibility(View.VISIBLE);
                    flag = 0;
                } else {
                    binding.searchLayout.setVisibility(View.GONE);
                    flag = 1;
                }
                break;
            case R.id.ivLocation:

                if (isMapVisible) {
                    binding.llMap.setVisibility(View.VISIBLE);
                    loadMap();
                    isMapVisible = false;
                } else {
                    binding.llMap.setVisibility(View.GONE);
                    isMapVisible = true;
                }
                break;
        }

    }


    private void countPointConnection() {

        if (CommonUtils.isOnline(mContext)) {
            try {
                ProgressDialogUtils.show(mContext);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                CountPointRequest countPointRequest = new CountPointRequest();
                countPointRequest.setUserId("" + userModel.getId());
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.count_Point(countPointRequest).enqueue(new BaseCallback<CountPointResponse>(fragment) {
                    @Override
                    public void onSuccess(CountPointResponse response) {

                        if (response != null) {
                            if (response.getStatus() == 1) {
                                CountPointData countPointData = response.getResult().getData();
                                CountPointData countPoint = createCountPoint(countPointData);
                                PreferenceUtils.savePoint(countPoint);
                                pointData();
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<CountPointResponse> call, BaseResponse baseResponse) {
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

    private CountPointData createCountPoint(CountPointData countPointData) {
        CountPointData countPointData1 = new CountPointData();
        countPointData1.setFriend_count(countPointData.getFriend_count());
        countPointData1.setFavourite_studios(countPointData.getFavourite_studios());
        countPointData1.setCompleted_class(countPointData.getCompleted_class());
        countPointData1.setUpcoming_class(countPointData.getUpcoming_class());
        countPointData1.setUser_point(countPointData.getUser_point());


        return countPointData1;
    }

    private void pointData() {
        CountPointData point = PreferenceUtils.getPoint();

        tvRight.setText(point.getUser_point() + " Points ");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


}
