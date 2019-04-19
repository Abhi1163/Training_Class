package com.hobbyer.android.view.fragments.upcoming;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hobbyer.android.R;
import com.hobbyer.android.api.BaseCallback;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.RequestController;
import com.hobbyer.android.api.request.CountPointRequest;
import com.hobbyer.android.api.request.FavouriteStudioRequest;
import com.hobbyer.android.api.request.UpComingClassesRequest;
import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointResponse;
import com.hobbyer.android.api.response.auth.upcomingclasses.UpComingClassesContentList;
import com.hobbyer.android.api.response.auth.upcomingclasses.UpComingClassesResponse;
import com.hobbyer.android.api.retrofit.AuthWebServices;
import com.hobbyer.android.databinding.FragmentUpcomingBinding;
import com.hobbyer.android.databinding.RowUpcomingClassesBinding;
import com.hobbyer.android.prefs.PreferenceUtils;
import com.hobbyer.android.utils.CommonUtils;
import com.hobbyer.android.utils.ProgressDialogUtils;
import com.hobbyer.android.utils.StringUtils;
import com.hobbyer.android.utils.ToastUtils;
import com.hobbyer.android.widget.CustomDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;


public class UpComingFragment extends Fragment implements View.OnClickListener {

    FragmentUpcomingBinding fragmentUpcomingBinding;
    AdapterUpComingClasses adapterUpComingClasses;
    String favourite_key = null;
    String timeStart, timeEnd;
    int startDate, startTime;
    String timeStarts;
    private Context context;
    private Fragment fragment;
    private TextView tvTitle, tvRight;
    private ImageView ivBack, ivSearch, ivLocation;
    private CustomDialog dialog;
    private TextView tvOk, tvMessage;
    private DrawerLayout drawerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentUpcomingBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_upcoming, container, false);
        context = this.getContext();
        init();
        setToolbar();
        countPointConnection();

        return fragmentUpcomingBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();


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
        ivLocation.setVisibility(View.INVISIBLE);
        ivSearch.setVisibility(View.INVISIBLE);
        ivBack.setOnClickListener(this);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.upcoming);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    private void initViews() {

        upComingPageConnection();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        int numberOfColumns = 1;
        fragmentUpcomingBinding.rvUpComingPage.setLayoutManager(mLayoutManager);
        fragmentUpcomingBinding.rvUpComingPage.setLayoutManager(new GridLayoutManager(context, numberOfColumns));
    }

    private void upComingPageConnection() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                UpComingClassesRequest homeRequest = new UpComingClassesRequest();
                homeRequest.setUserId("" + userModel.getId());
                homeRequest.setPage_number(1);
                homeRequest.setPage_size(10);
                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.upComing_Classes(homeRequest).enqueue(new BaseCallback<UpComingClassesResponse>(fragment) {
                    @Override
                    public void onSuccess(UpComingClassesResponse response) {
                        //fragmentHomeBinding.swipeContainer.setRefreshing(false);
                        if (response != null) {
                            if (response.getResult().getContentList().size() > 0) {
                                if (response.getStatus() == 1) {
                                    fragmentUpcomingBinding.rvUpComingPage.setVisibility(View.GONE);
                                    fragmentUpcomingBinding.tvNoData.setVisibility(View.VISIBLE);
                                    ArrayList<UpComingClassesContentList> studiosList = response.getResult().getContentList();
                                    adapterUpComingClasses = new AdapterUpComingClasses((Activity) context, studiosList);
                                    fragmentUpcomingBinding.rvUpComingPage.setAdapter(adapterUpComingClasses);
                                }
                            }
                            else {
                                fragmentUpcomingBinding.rvUpComingPage.setVisibility(View.GONE);
                                fragmentUpcomingBinding.tvNoData.setVisibility(View.VISIBLE);
                            }
                        }

                        ProgressDialogUtils.dismiss();
                    }

                    @Override
                    public void onFail(Call<UpComingClassesResponse> call, BaseResponse baseResponse) {
                        ProgressDialogUtils.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                break;
        }

    }

    /*favouriteStudioConnection*/
    private void favouriteStudioConnection(String studioId) {
        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
                UserModel userModel = PreferenceUtils.getUserModel();
                if (userModel == null) {
                    return;
                }
                FavouriteStudioRequest favouriteStudioRequest = new FavouriteStudioRequest();
                favouriteStudioRequest.setUser_id("" + userModel.getId());
                favouriteStudioRequest.setStudio_id(studioId);
                favouriteStudioRequest.setFavourite_key(favourite_key);

                AuthWebServices webServices = RequestController.createService(AuthWebServices.class, false);
                webServices.favouriteStudio(favouriteStudioRequest).enqueue(new BaseCallback<BaseResponse>(this) {
                    @Override
                    public void onSuccess(BaseResponse response) {
                        if (response != null) {
                            if (response.getStatus() == 1) {
                                showDialog(response.getMessage());
                            } else {
                                ToastUtils.showToastLong(context, "  " + response.getMessage());
                            }
                        }

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
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void showDialog(String message) {
        dialog = new CustomDialog(context, R.layout.row_message_city);
        tvOk = dialog.findViewById(R.id.btnOk);
        tvMessage = dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();


    }

    private void countPointConnection() {

        if (CommonUtils.isOnline(context)) {
            try {
                ProgressDialogUtils.show(context);
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
            Toast.makeText(context, R.string.msg_network_error, Toast.LENGTH_SHORT).show();
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

    public class AdapterUpComingClasses extends RecyclerView.Adapter<AdapterUpComingClasses.DataObjectHolder> {


        Activity main;
        GoogleMap gMap;
        private ArrayList<UpComingClassesContentList> studiosList;
        /*private MapView mapView;*/
        private Activity context;

        public AdapterUpComingClasses(Activity context, ArrayList<UpComingClassesContentList> list) {
            this.context = context;
            this.studiosList = list;
        }

        @Override
        public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upcoming_classes, parent, false);
            DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
            return dataObjectHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onBindViewHolder(final DataObjectHolder holder, final int position) {
            if (position != 0) {
                holder.binding.layoutLocationsContainer.setVisibility(View.GONE);
                System.out.println(studiosList.get(position).getLikedStatus());
                if (studiosList.get(position).getLikedStatus() != null && !studiosList.get(position).getLikedStatus().equalsIgnoreCase("")) {
                    if (studiosList.get(position).getLikedStatus().equalsIgnoreCase("0")) {

                        holder.binding.ivLikeUpcoming.setVisibility(View.VISIBLE);
                        holder.binding.ivUnLikeUpcoming.setVisibility(View.GONE);
                    } else if (studiosList.get(position).getLikedStatus().equalsIgnoreCase("1")) {
                        holder.binding.ivLikeUpcoming.setVisibility(View.GONE);
                        holder.binding.ivUnLikeUpcoming.setVisibility(View.VISIBLE);
                    } else {

                    }
                }
                timeStart = StringUtils.timeFormat(studiosList.get(position).getStartTime());
                timeEnd = StringUtils.timeFormat(studiosList.get(position).getEndTime());
                holder.binding.tvUpComingStudioDays.setText(studiosList.get(position).getDay() + "," + studiosList.get(position).getBookingDate() + "," + timeStart.toString() + "-" + timeEnd.toString());
                holder.binding.tvUpComingStudioName.setText(studiosList.get(position).getStudioName());
                holder.binding.tvUpComingStudioDescription.setText(studiosList.get(position).getHowToPrepare());
                if (studiosList.get(position).getStudioImage() != null && !studiosList.get(position).getStudioImage().equalsIgnoreCase("")) {
                    Picasso.with(main).load(studiosList.get(position).getStudioImage()).placeholder(R.drawable.ic_image).fit().into(holder.binding.imStudioImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.binding.pbUpComing.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.binding.pbUpComing.setVisibility(View.GONE);
                        }
                    });
                } else {
                    holder.binding.imStudioImage.setImageResource(R.drawable.ic_image);
                    holder.binding.pbUpComing.setVisibility(View.GONE);
                }
                holder.binding.imStudioImage.setVisibility(View.GONE);
            } else {
                holder.binding.imStudioImage.setVisibility(View.VISIBLE);
                holder.binding.layoutLocationsContainer.setVisibility(View.VISIBLE);
                if (studiosList.get(position).getLatitude() != null && studiosList.get(position).getLongitude() != null && studiosList.get(position).getStudioName() != null) {
                    holder.mmLag = Double.parseDouble(studiosList.get(position).getLatitude());
                    holder.mmLng = Double.parseDouble(studiosList.get(position).getLongitude());
                }
                String timeStart = StringUtils.timeFormat(studiosList.get(position).getStartTime());
                String todayAsString = StringUtils.dateFormat(studiosList.get(position).getDays());
                if (studiosList.get(position).getLikedStatus().equalsIgnoreCase("0")) {

                    holder.binding.ivLikeUpcoming.setVisibility(View.VISIBLE);
                    holder.binding.ivUnLikeUpcoming.setVisibility(View.GONE);
                } else if (studiosList.get(position).getLikedStatus().equalsIgnoreCase("1")) {
                    holder.binding.ivLikeUpcoming.setVisibility(View.GONE);
                    holder.binding.ivUnLikeUpcoming.setVisibility(View.VISIBLE);
                } else {

                }
                String timeEnd = StringUtils.timeFormat(studiosList.get(position).getEndTime());
                holder.binding.tvUpComingStudioDays.setText(studiosList.get(position).getDay() + "," + studiosList.get(position).getBookingDate() + "," + timeStart.toString() + "-" + timeEnd.toString());
                holder.binding.tvUpComingStudioName.setText(studiosList.get(position).getStudioName());
                holder.binding.tvUpComingStudioDescription.setText(studiosList.get(position).getHowToPrepare());


            }
            holder.binding.ivLikeUpcoming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.binding.ivLikeUpcoming.setVisibility(View.GONE);
                    holder.binding.ivUnLikeUpcoming.setVisibility(View.VISIBLE);
                    favourite_key = "1";
                    favouriteStudioConnection(studiosList.get(position).getStudioId());
                }
            });
            holder.binding.ivUnLikeUpcoming.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.binding.ivLikeUpcoming.setVisibility(View.VISIBLE);
                    holder.binding.ivUnLikeUpcoming.setVisibility(View.GONE);
                    favourite_key = "0";
                    favouriteStudioConnection(studiosList.get(position).getStudioId());
                }
            });


            holder.binding.ivEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserModel userModel = PreferenceUtils.getUserModel();
                    if (userModel == null) {
                        return;
                    }
                    Calendar beginTime = Calendar.getInstance();
                    String[] mDate = studiosList.get(position).getBookingDate().split(" ");
                    int mon = StringUtils.changeDateFormat(mDate[0])[1];
                    int day = StringUtils.changeDateFormat(mDate[0])[2];
                    int year = StringUtils.changeDateFormat(mDate[0])[0];
                    timeStarts = StringUtils.timeFormatCalender(studiosList.get(position).getStartTime());
                    String[] mTime = timeStarts.split(" ");
                    int StartMin = StringUtils.changeTimeFormat(mTime[0])[1];
                    int StartHour = StringUtils.changeTimeFormat(mTime[0])[0];
                    beginTime.set(year, mon, day, StartHour, StartMin);
                    Calendar endTime = Calendar.getInstance();
                    endTime.set(year, mon, day, StartHour, StartMin);
                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                            .putExtra(CalendarContract.Events.TITLE, studiosList.get(position).getStudioName())
                            .putExtra(CalendarContract.Events.CAN_INVITE_OTHERS, 1)
                            .putExtra(CalendarContract.Events.GUESTS_CAN_INVITE_OTHERS, 1)
                            .putExtra(CalendarContract.Events.CALENDAR_DISPLAY_NAME, studiosList.get(position).getStudioName())
                            .putExtra(CalendarContract.Events.DESCRIPTION, studiosList.get(position).getStudioName())
                            .putExtra(CalendarContract.Events.EVENT_LOCATION, String.valueOf(studiosList.get(position).getLatitude() + "" + studiosList.get(position).getLongitude()))
                            .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
                            .putExtra(CalendarContract.Events.HAS_ALARM, 1)
                            .putExtra(Intent.EXTRA_EMAIL, userModel.getEmail());
                    startActivity(intent);
                }
            });

        }

        public void deleteItem(int index) {
            studiosList.remove(index);
            notifyItemRemoved(index);
        }

        @Override
        public int getItemCount() {
            return studiosList.size();
        }

        public void setCountries(ArrayList<UpComingClassesContentList> findClassModel) {
            this.studiosList = findClassModel;
            notifyDataSetChanged();
        }

        public class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnMapReadyCallback {
            RowUpcomingClassesBinding binding;
            /*ProgressBar pbUpComing;*/
/*
            TextView tvUpComingStudioName,tvUpComingStudioDays,tvUpComingStudioDescription;
*/
            /*private RelativeLayout layout_locations_container;*/
            double mmLag, mmLng;

            /*private ImageView imStudioImage;*/
            public DataObjectHolder(View itemView) {
                super(itemView);

                binding = DataBindingUtil.bind(itemView);

/*
                tvUpComingStudioName = (TextView) itemView.findViewById(R.id.tvUpComingStudioName);
*/
/*
                imStudioImage = (ImageView)itemView.findViewById(R.id.imStudioImage);
*/
/*
                tvUpComingStudioDays = (TextView) itemView.findViewById(R.id.tvUpComingStudioDays);
*/
/*
                tvUpComingStudioDescription = (TextView) itemView.findViewById(R.id.tvUpComingStudioDescription);
*/
/*
                pbUpComing = (ProgressBar) itemView.findViewById(R.id.pbUpComing);
*/
/*
                mapView =itemView.findViewById(R.id.map);
*/
                binding.map.onCreate(null);
                binding.map.onResume();
/*
                layout_locations_container =(RelativeLayout)itemView.findViewById(R.id.layout_locations_container);
*/
                if (binding.map != null) {
                    // Initialise the MapView
                    binding.map.onCreate(null);
                    // Set the map ready callback to receive the GoogleMap object
                    binding.map.getMapAsync(this);
                }
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
            }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                gMap = googleMap;
                gMap.setMinZoomPreference(9);
                gMap.setIndoorEnabled(true);
                UiSettings uiSettings = gMap.getUiSettings();
                LatLng ny = new LatLng(mmLag, mmLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(ny);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_icon));
                gMap.addMarker(markerOptions);
                gMap.moveCamera(CameraUpdateFactory.newLatLng(ny));
                uiSettings.setRotateGesturesEnabled(false);
                uiSettings.setScrollGesturesEnabled(false);
                uiSettings.setTiltGesturesEnabled(false);
                uiSettings.setZoomGesturesEnabled(false);
            }

        }


    }
}
